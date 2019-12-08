import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Session, SessionState} from '../models/session';
import {BackendService} from '../services/backend.service';
import {Observable} from 'rxjs';

@Component({
  selector: 'app-session',
  templateUrl: './session.component.html',
  styleUrls: ['./session.component.css']
})
export class SessionComponent implements OnInit, OnDestroy {

  public readonly session: Observable<Session> = null;
  private sessionId;

  constructor(private backendService: BackendService, private route: ActivatedRoute) {
    this.session = backendService.sessionObservable;
  }

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      const sessionId = params.get('sessionId');
      this.sessionId = sessionId;

      this.backendService.pollSession(sessionId);
    });
  }

  runSession() {
    this.backendService.runSession(this.sessionId);
  }

  stopSession() {
    this.backendService.stopSession(this.sessionId);
  }

  resetSession() {
    this.backendService.resetSession(this.sessionId);
  }

  ngOnDestroy(): void {
    this.backendService.cancelPolling();
  }

  disableRunButton(): boolean {
    let isDisabled = false;
    this.session.subscribe(session => isDisabled = session.state !== SessionState.STOPPED);
    return isDisabled;
  }

  disableStopButton(): boolean {
    let isDisabled = false;
    this.session.subscribe(session => isDisabled = session.state !== SessionState.RUNNING);
    return isDisabled;
  }
}
