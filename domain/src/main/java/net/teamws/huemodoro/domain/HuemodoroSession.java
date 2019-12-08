package net.teamws.huemodoro.domain;

import java.time.*;
import java.util.*;

public class HuemodoroSession {

	private static final SessionState INITIAL_STATE = SessionState.STOPPED;
	private static final Duration DEFAULT_DURATION = Duration.ofMinutes(25);

	private Duration timeLeft = DEFAULT_DURATION;
	private SessionState state = INITIAL_STATE;

	private final List<SessionStateObserver> stateObservers = new ArrayList<>();

	public Duration getTimeLeft() {
		return timeLeft;
	}

	public void advanceTime(Duration advanceBy) {
		if (state == SessionState.RUNNING) {
			timeLeft = timeLeft.minus(advanceBy);
			if (timeLeft.isNegative()) {
				timeLeft = Duration.ZERO;
			}
			if (timeLeft.isZero()) {
				SessionState finished = SessionState.FINISHED;
				changeState(finished);
			}
		}
	}

	public void reset() {
		changeState(INITIAL_STATE);
		timeLeft = DEFAULT_DURATION;
	}

	public SessionState getState() {
		return state;
	}

	public void run() {
		changeState(SessionState.RUNNING);
	}

	public void stop() {
		changeState(SessionState.STOPPED);
	}

	public void addStateObserver(SessionStateObserver stateObserver) {
		stateObservers.add(stateObserver);
	}

	public void removeStateObserver(SessionStateObserver stateObserver) {
		stateObservers.remove(stateObserver);
	}

	private void changeState(SessionState newState) {
		SessionState oldState = this.state;
		this.state = newState;
		stateObservers.forEach(observer -> observer.stateChanged(oldState, newState));
	}
}
