package net.teamws.huemodoro.domain;

import java.time.*;
import java.util.*;

public class HuemodoroSession {

	private static final SessionState INITIAL_STATE = SessionState.INITIAL;
	private static final Duration DEFAULT_DURATION = Duration.ofMinutes(25);

	private Duration timeLeft = DEFAULT_DURATION;
	private SessionState state = INITIAL_STATE;

	private final List<SessionStateObserver> stateObservers = new ArrayList<>();

	public Duration getTimeLeft() {
		return timeLeft;
	}
	public boolean autoRestart = true;

	public void advanceTime(Duration advanceBy) {
		if(isFinished() && autoRestart) {
			restart();
		}

		if (state == SessionState.RUNNING || state == SessionState.BREAK) {
			timeLeft = timeLeft.minus(advanceBy);

			if (timeLeft.isNegative()) {
				timeLeft = Duration.ZERO;
			}
			if (timeLeft.isZero()) {
				if(state == SessionState.RUNNING) {
					timeLeft = Duration.ofMinutes(5);
					changeState(SessionState.BREAK);
				} else {
					SessionState finished = SessionState.FINISHED;
					changeState(finished);
				}
			}
		}
	}

	private boolean isFinished() {
		return state == SessionState.FINISHED;
	}

	public void restart() {
		timeLeft = DEFAULT_DURATION;
		changeState(SessionState.RUNNING);
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

	public void pause() {
		changeState(SessionState.PAUSED);
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
