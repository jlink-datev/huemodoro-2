package net.teamws.huemodoro.web.sessions;

import java.time.*;

import net.teamws.huemodoro.domain.*;

public class SessionResponse {

	private final SessionState state;

	private final Duration timeLeft;

	public SessionResponse(HuemodoroSession session) {
		this.state = session.getState();
		this.timeLeft = session.getTimeLeft();
	}

	public String getId() {
		return "1";
	}

	public SessionState getState() {
		return state;
	}

	public Duration getTimeLeft() {
		return timeLeft;
	}

}
