package net.teamws.huemodoro.web.repository;

import java.time.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import net.teamws.huemodoro.domain.*;
import net.teamws.huemodoro.web.hue.*;

@Component
public class InMemorySessionRepository implements SessionRepository {

	private final HuemodoroSession session;

	public InMemorySessionRepository(@Autowired HueService hueService) {
		session = new HuemodoroSession();
		session.addStateObserver(hueService);
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
	public HuemodoroSession stopSession() {
		session.stop();
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
