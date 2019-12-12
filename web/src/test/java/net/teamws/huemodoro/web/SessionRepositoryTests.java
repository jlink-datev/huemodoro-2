package net.teamws.huemodoro.web;

import java.time.*;

import org.junit.jupiter.api.*;

import net.teamws.huemodoro.domain.*;
import net.teamws.huemodoro.web.hue.*;
import net.teamws.huemodoro.web.repository.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static net.teamws.huemodoro.domain.SessionState.*;

class SessionRepositoryTests {

	private HueService hueService = mock(HueService.class);
	private HuemodoroSessionConfiguration huemodoroSessionConfiguration =
			mock(HuemodoroSessionConfiguration.class);
	private SessionRepository repository =
			new InMemorySessionRepository(hueService, huemodoroSessionConfiguration);


	@Test
	void initiallySessionIsFresh() {
		HuemodoroSession session = repository.getSession();
		assertEquals(INITIAL, session.getState());
		assertEquals(Duration.ofMinutes(25), session.getTimeLeft());
	}

	@Test
	void startingSession() {
		HuemodoroSession session = repository.runSession();
		assertEquals(RUNNING, session.getState());
	}

	@Test
	void hueServiceIsRegisteredAsStateChangeObserver() {
		repository.runSession();
		verify(hueService).stateChanged(INITIAL, RUNNING);
	}

	@Test
	void pausingSession() {
		repository.runSession();
		HuemodoroSession session = repository.pauseSession();
		assertEquals(PAUSED, session.getState());
	}

	@Test
	void resettingSession() {
		repository.runSession();
		repository.advanceTime(Duration.ofMinutes(12));

		HuemodoroSession session = repository.resetSession();
		assertEquals(INITIAL, session.getState());
		assertEquals(Duration.ofMinutes(25), session.getTimeLeft());
	}

	@Test
	void breakStartsAfterPomodoroAndTimeLeftIsFiveMinutes() {
		repository.runSession();
		repository.advanceTime(Duration.ofHours(1));

		HuemodoroSession session = repository.getSession();
		assertEquals(SessionState.BREAK, session.getState());
		assertEquals(Duration.ofMinutes(5), session.getTimeLeft());
	}

	@Test
	void whenBreakIsOverStateIsFinished() {
		repository.runSession();
		repository.advanceTime(Duration.ofHours(1));
		repository.advanceTime(Duration.ofHours(1));

		HuemodoroSession session = repository.getSession();
		assertEquals(FINISHED, session.getState());
		assertEquals(Duration.ZERO, session.getTimeLeft());
	}
}
