package net.teamws.huemodoro.web.repository;

import java.time.*;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import net.teamws.huemodoro.domain.*;
import net.teamws.huemodoro.web.*;
import net.teamws.huemodoro.web.hue.*;

@Component
public class InMemorySessionRepository implements SessionRepository {
	private static Logger logger = LoggerFactory.getLogger(InMemorySessionRepository.class);


	private final HuemodoroSession session;

	public InMemorySessionRepository(@Autowired HueService hueService, @Autowired HuemodoroSessionConfiguration sessionConfiguration) {
		long time = sessionConfiguration.getTimeInMinutes();
		session = new HuemodoroSession(time);
		session.addStateObserver(hueService);
		logConfiguration(time);
	}
	private void logConfiguration(long time) {
		logger.info("Session timeInMinutes:   {}", time);
	}

	@Override
	public HuemodoroSession getSession() {
		return session;
	}

	@Override
	public HuemodoroSession runSession() {
		session.run();
		return session;
	}

	@Override
	public HuemodoroSession pauseSession() {
		session.pause();
		return session;
	}

	@Override
	public HuemodoroSession resetSession() {
		session.reset();
		return session;
	}

	@Override
	public void advanceTime(Duration advanceBy) {
		session.advanceTime(advanceBy);
	}
}
