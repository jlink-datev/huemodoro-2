package net.teamws.huemodoro.web.hue;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import net.teamws.huemodoro.domain.*;

@Service
public class HueService implements SessionStateObserver {

	private static Logger logger = LoggerFactory.getLogger(HueService.class);
	private HueBridge hueBridge;

	public HueService(@Autowired HueBridge hueBridge) {
		this.hueBridge = hueBridge;
	}

	@Override
	public void stateChanged(SessionState oldState, SessionState newState) {
		logger.debug("state change: {} -> {}", oldState, newState);
		if (SessionState.RUNNING.equals(newState))
			hueBridge.lightOn(Colour.RED);
		else if (SessionState.FINISHED.equals(newState))
			hueBridge.lightOn(Colour.GREEN);
		else if (SessionState.STOPPED.equals(newState))
			hueBridge.lightOn(Colour.ORANGE);

	}

}
