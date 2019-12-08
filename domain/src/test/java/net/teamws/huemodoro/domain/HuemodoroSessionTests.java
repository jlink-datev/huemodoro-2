package net.teamws.huemodoro.domain;

import java.time.*;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class HuemodoroSessionTests {

	HuemodoroSession session;

	@BeforeEach
	void initSession() {
		session = new HuemodoroSession();
	}

	@Nested
	class NewSession {

		@Test
		void isInStoppedState() {
			assertEquals(SessionState.STOPPED, session.getState());
		}

		@Test
		void timeLeftIs25Minutes() {
			assertEquals(Duration.ofMinutes(25), session.getTimeLeft());
		}

	}

	@Nested
	class FinishedSession {
		@BeforeEach
		void finishSession() {
			session.run();
			session.advanceTime(Duration.ofMinutes(25));
		}

		@Test
		void isFinished() {
			assertEquals(SessionState.FINISHED, session.getState());
		}

		@Test
		void timeCanNoLongerAdvance() {
			session.advanceTime(Duration.ofMinutes(1));
			assertEquals(Duration.ZERO, session.getTimeLeft());
		}

		@Test
		void canBeReset() {
			session.reset();
			assertEquals(SessionState.STOPPED, session.getState());
			assertEquals(Duration.ofMinutes(25), session.getTimeLeft());
		}
	}

	@Nested
	class StateChangeCanBeObserved implements SessionStateObserver {

		private SessionState oldState;
		private SessionState newState;

		@Override
		public void stateChanged(SessionState oldState, SessionState newState) {
			this.oldState = oldState;
			this.newState = newState;
		}

		@BeforeEach
		void initializeObserver() {
			session.addStateObserver(this);
		}

		@Test
		void fromStoppedToRunning() {
			session.run();
			assertEquals(SessionState.STOPPED, oldState);
			assertEquals(SessionState.RUNNING, newState);
		}

		@Test
		void fromRunningToFinished() {
			session.run();
			session.advanceTime(Duration.ofHours(1));
			assertEquals(SessionState.RUNNING, oldState);
			assertEquals(SessionState.FINISHED, newState);
		}

		@Test
		void whenResetting() {
			session.run();
			session.reset();
			assertEquals(SessionState.RUNNING, oldState);
			assertEquals(SessionState.STOPPED, newState);
		}

		@Test
		void withMoreThanOneObserver() {
			SessionState[] states = new SessionState[2];
			SessionStateObserver second = (oldState2, newState2) -> {
				states[0] = oldState2;
				states[1] = newState2;
			};
			session.addStateObserver(second);
			session.run();
			assertEquals(SessionState.STOPPED, oldState);
			assertEquals(SessionState.RUNNING, newState);
			assertEquals(SessionState.STOPPED, oldState);
			assertEquals(SessionState.RUNNING, newState);
		}

		@Test
		void andObservingStopped() {
			session.removeStateObserver(this);
			session.run();
			assertNull(oldState);
			assertNull(newState);
		}

	}

	@Test
	void stoppedSessionCanBeRun() {
		session.stop();
		session.run();
		assertEquals(SessionState.RUNNING, session.getState());
	}

	@Test
	void runningSessionCanBeStopped() {
		session.run();
		session.stop();
		assertEquals(SessionState.STOPPED, session.getState());
	}

	@Test
	void advancingTimeWhileRunningChangesTimeLeft() {
		session.run();
		session.advanceTime(Duration.ofMinutes(5));
		assertEquals(Duration.ofMinutes(20), session.getTimeLeft());
		session.advanceTime(Duration.ofMinutes(5).plusSeconds(10));
		assertEquals(Duration.ofMinutes(14).plusSeconds(50), session.getTimeLeft());
	}

	@Test
	void advancingTimeWhileStoppedChangedNothing() {
		session.stop();
		session.advanceTime(Duration.ofMinutes(5));
		assertEquals(Duration.ofMinutes(25), session.getTimeLeft());
	}

	@Test
	void advancingTimeBeyondZeroSetsSessionToFinished() {
		session.run();
		session.advanceTime(Duration.ofMinutes(6));
		session.advanceTime(Duration.ofMinutes(6));
		session.advanceTime(Duration.ofMinutes(6));
		session.advanceTime(Duration.ofMinutes(6));
		session.advanceTime(Duration.ofMinutes(6));
		assertEquals(Duration.ZERO, session.getTimeLeft());
		assertEquals(SessionState.FINISHED, session.getState());
	}

}
