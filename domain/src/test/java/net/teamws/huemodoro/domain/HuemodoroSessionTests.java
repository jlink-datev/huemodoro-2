package net.teamws.huemodoro.domain;

import java.time.*;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class HuemodoroSessionTests {

	public static final int POMODORO_SESSION_RUN_TIME = 25;
	public static final int POMODORO_SESSION_BREAK_TIME = 5;
	HuemodoroSession session;

	@BeforeEach
	void initSession() {
		session = new HuemodoroSession();
	}

	@Nested
	class NewSession {

		@Test
		void isInStoppedState() {
			assertEquals(SessionState.INITIAL, session.getState());
		}

		@Test
		void timeLeftIs25Minutes() {
			assertEquals(Duration.ofMinutes(POMODORO_SESSION_RUN_TIME), session.getTimeLeft());
		}

	}

	@Nested
	class BreakSession {
		@BeforeEach
		void breakSession() {
			session.run();
			session.advanceTime(Duration.ofMinutes(POMODORO_SESSION_RUN_TIME));
		}

		@Test
		void isInBreakState() {
			assertEquals(SessionState.BREAK, session.getState());
		}

		@Test
		void isFinishedAfter5Minutes() {
			session.advanceTime(Duration.ofMinutes(POMODORO_SESSION_BREAK_TIME));
			assertEquals(SessionState.FINISHED, session.getState());
			assertEquals(Duration.ZERO, session.getTimeLeft());
		}

		@Test
		void canBeReset() {
			session.reset();
			assertEquals(SessionState.INITIAL, session.getState());
			assertEquals(Duration.ofMinutes(POMODORO_SESSION_RUN_TIME), session.getTimeLeft());
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
			assertEquals(SessionState.INITIAL, oldState);
			assertEquals(SessionState.RUNNING, newState);
		}

		@Test
		void fromRunningToFinished() {
			session.run();
			session.advanceTime(Duration.ofHours(1));
			assertEquals(SessionState.RUNNING, oldState);
			assertEquals(SessionState.BREAK, newState);
		}

		@Test
		void whenResetting() {
			session.run();
			session.reset();
			assertEquals(SessionState.RUNNING, oldState);
			assertEquals(SessionState.INITIAL, newState);
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
			assertEquals(SessionState.INITIAL, oldState);
			assertEquals(SessionState.RUNNING, newState);
			assertEquals(SessionState.INITIAL, oldState);
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
		session.pause();
		session.run();
		assertEquals(SessionState.RUNNING, session.getState());
	}

	@Test
	void runningSessionCanBePaused() {
		session.run();
		session.pause();
		assertEquals(SessionState.PAUSED, session.getState());
	}

	@Test
	void runningSessionCanBeReseted() {
		session.run();
		session.reset();
		assertEquals(SessionState.INITIAL, session.getState());
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
		session.pause();
		session.advanceTime(Duration.ofMinutes(5));
		assertEquals(Duration.ofMinutes(POMODORO_SESSION_RUN_TIME), session.getTimeLeft());
	}

	@Test
	void advancingTimeBeyondZeroSetsSessionToBreak() {
		session.run();
		session.advanceTime(Duration.ofMinutes(POMODORO_SESSION_RUN_TIME + 1));
		assertEquals(Duration.ofMinutes(POMODORO_SESSION_BREAK_TIME), session.getTimeLeft());
		assertEquals(SessionState.BREAK, session.getState());
	}

}
