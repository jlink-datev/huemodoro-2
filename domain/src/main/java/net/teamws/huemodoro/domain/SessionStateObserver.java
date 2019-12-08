package net.teamws.huemodoro.domain;

@FunctionalInterface
public interface SessionStateObserver {

	void stateChanged(SessionState oldState, SessionState newState);
}
