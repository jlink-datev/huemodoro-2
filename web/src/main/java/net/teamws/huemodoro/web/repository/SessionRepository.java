package net.teamws.huemodoro.web.repository;

import java.time.*;

import net.teamws.huemodoro.domain.*;

public interface SessionRepository {
	HuemodoroSession getSession();

	HuemodoroSession runSession();

	HuemodoroSession pauseSession();

	HuemodoroSession resetSession();

	void advanceTime(Duration advanceBy);
}
