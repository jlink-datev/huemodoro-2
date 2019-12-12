package net.teamws.huemodoro.web;

import org.springframework.boot.context.properties.*;
import org.springframework.stereotype.*;

@Component
@ConfigurationProperties("session")
public class HuemodoroSessionConfiguration {
	private long timeInMinutes;

	public long getTimeInMinutes() {
		return timeInMinutes;
	}

	public void setTimeInMinutes(long timeInMinutes) {
		this.timeInMinutes = timeInMinutes;
	}
}
