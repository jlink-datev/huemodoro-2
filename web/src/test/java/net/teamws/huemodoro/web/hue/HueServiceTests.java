package net.teamws.huemodoro.web.hue;

import org.junit.jupiter.api.*;
import org.mockito.*;

import net.teamws.huemodoro.domain.*;

import static org.mockito.Mockito.*;

class HueServiceTests {

	private HueBridge bridge;

	private HueService hueService;

	@BeforeEach
	void initService() {
		bridge = Mockito.mock(HueBridge.class);
		hueService = new HueService(bridge);
	}

	@Test
	void itShouldTurnLightOffOnStateStopped() {
		hueService.stateChanged(null, SessionState.INITIAL);
		verify(bridge).lightOff();
		verifyNoMoreInteractions(bridge);
	}

	@Test
	void itShouldTurnLightRedOnStateRunning() {
		hueService.stateChanged(null, SessionState.RUNNING);
		verify(bridge).lightOn(Colour.RED);
		verifyNoMoreInteractions(bridge);
	}

	@Test
	void itShouldTurnLightGreenOnStateFinished() {
		hueService.stateChanged(null, SessionState.BREAK);
		verify(bridge).lightOn(Colour.GREEN);
		verifyNoMoreInteractions(bridge);
	}

	@Test
	void itShouldTurnLightOrangeOnStatePaused() {
		hueService.stateChanged(null, SessionState.PAUSED);
		verify(bridge).lightOn(Colour.ORANGE);
		verifyNoMoreInteractions(bridge);
	}

	@Test
	void itShouldTurnLightOffOnStateFinished() {
		hueService.stateChanged(null, SessionState.FINISHED);
		verify(bridge).lightOff();
		verifyNoMoreInteractions(bridge);
	}
}