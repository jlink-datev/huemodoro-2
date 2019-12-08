import {Session, SessionState} from '../models/session';
import {environment} from '../../environments/environment';
import {BehaviorSubject, interval, Observable, of, Subscription} from 'rxjs';
import {catchError, map, startWith, switchMap} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {Injectable, OnDestroy} from '@angular/core';

function mapToSession(json: any): Session {
  const state: SessionState = json.state;
  const id: string = json.id;
  const timeLeft: number = json.timeLeft;
  return new Session(id, state, timeLeft);
}

@Injectable()
export class BackendService implements OnDestroy {

  private readonly sessionSource: BehaviorSubject<Session> = new BehaviorSubject<Session>(
    new Session('0', SessionState.STOPPED, 25000)
  );
  readonly sessionObservable: Observable<Session> = this.sessionSource.asObservable();
  private sessionPolling: Subscription;


  constructor(private http: HttpClient) {
  }

  pollSession(sessionId) {
    this.sessionPolling = interval(1000)
      .pipe(
        startWith(0),
        switchMap(() => this.getSession(sessionId))
      )
      .subscribe(
        session => this.sessionSource.next(session)
      );
  }

  cancelPolling() {
    this.sessionPolling.unsubscribe();
  }

  getSession(sessionId: string): Observable<Session> {
    const url = `${environment.backendBaseUrl}/sessions/${sessionId}`;
    return this.http.get<Session>(url)
      .pipe(catchError(this.handleError), map(mapToSession));
  }

  runSession(sessionId: string): void {
    const url = `${environment.backendBaseUrl}/sessions/${sessionId}/run`;
    this.http.put<any>(url, null)
      .pipe(catchError(this.handleError), map(mapToSession))
      .subscribe(this.doNothingButTriggerSubscription);
  }

  stopSession(sessionId: string): void {
    const url = `${environment.backendBaseUrl}/sessions/${sessionId}/stop`;
    this.http.put<Session>(url, null)
      .pipe(catchError(this.handleError), map(mapToSession))
      .subscribe(this.doNothingButTriggerSubscription);
  }

  resetSession(sessionId: string): void {
    const url = `${environment.backendBaseUrl}/sessions/${sessionId}/reset`;
    this.http.put<Session>(url, null)
      .pipe(catchError(this.handleError), map(mapToSession))
      .subscribe(this.doNothingButTriggerSubscription);
  }

  updateSession(session: Session) {
    this.sessionSource.next(session);
  }

  handleError(error) {
    console.error(JSON.stringify(error));
    return of({});
  }

  ngOnDestroy(): void {
    this.cancelPolling();
  }

  private doNothingButTriggerSubscription(): any {
    return {};
  }
}
