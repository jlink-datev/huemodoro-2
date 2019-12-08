export enum SessionState {
  STOPPED = 'STOPPED',
  RUNNING = 'RUNNING',
  FINISHED = 'FINISHED'
}

export class Session {
  constructor(public id: string, public state: SessionState, private timeLeft: number) {
  }

  get minutesLeft(): number {
    return Math.floor(this.timeLeft / 60);
  }

  get secondsLeft(): number {
    return this.timeLeft % 60;
  }

}
