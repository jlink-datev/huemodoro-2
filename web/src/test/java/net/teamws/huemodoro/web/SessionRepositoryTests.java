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
	private SessionRepository repository = new InMemorySessionRepository(hueService);

	@Test
	void initiallySessionIsFresh() {
		HuemodoroSession session = repository.getSession();
		assertEquals(STOPPED, session.getState());
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
		verify(hueService).stateChanged(STOPPED, RUNNING);
	}

	@Test
	void stoppingSession() {
		repository.runSession();
		HuemodoroSession session = repository.stopSession();
		assertEquals(STOPPED, session.getState());
	}

	@Test
	void resettingSession() {
		repository.runSession();
		repository.advanceTime(Duration.ofMinutes(12));

		HuemodoroSession session = repository.resetSession();
		assertEquals(STOPPED, session.getState());
		assertEquals(Duration.ofMinutes(25), session.getTimeLeft());
	}

	@Test
	void advanceTimeAdvancesTimeOfSession() {
		repository.runSession();
		repository.advanceTime(Duration.ofHours(1));

		HuemodoroSession session = repository.getSession();
		assertEquals(SessionState.FINISHED, session.getState());
		assertEquals(Duration.ZERO, session.getTimeLeft());
	}
}
