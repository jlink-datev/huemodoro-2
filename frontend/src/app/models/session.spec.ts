import {Session, SessionState} from './session';

describe('Session', () => {

  it('can be created', () => {
    const session = new Session('42', SessionState.FINISHED, 1500);
    expect(session.id).toBe('42');
    expect(session.state).toBe(SessionState.FINISHED);
    expect(session.minutesLeft).toBe(25);
    expect(session.secondsLeft).toBe(0);
  });

  it('left time calculation', () => {

    function withTimeLeft(timeLeft: number) {
      return new Session('42', SessionState.FINISHED, timeLeft);
    }

    expect(withTimeLeft(60).minutesLeft).toBe(1);
    expect(withTimeLeft(60).secondsLeft).toBe(0);

    expect(withTimeLeft(59).minutesLeft).toBe(0);
    expect(withTimeLeft(59).secondsLeft).toBe(59);

    expect(withTimeLeft(1499).minutesLeft).toBe(24);
    expect(withTimeLeft(1499).secondsLeft).toBe(59);

    expect(withTimeLeft(1441).minutesLeft).toBe(24);
    expect(withTimeLeft(1441).secondsLeft).toBe(1);

    expect(withTimeLeft(1440).minutesLeft).toBe(24);
    expect(withTimeLeft(1440).secondsLeft).toBe(0);
  });


});
