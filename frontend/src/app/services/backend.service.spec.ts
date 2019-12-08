import {BackendService} from './backend.service';
import {Session, SessionState} from '../models/session';
import {asyncData, asyncError} from '../testing/async-observable-helper';
import {fakeAsync, tick} from '@angular/core/testing';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';


const mockRunSessionResponse = (httpClientSpy, session) => {
  httpClientSpy.put.and.returnValue(asyncData(session));
};

const mockRunSessionResponseError = httpClientSpy => {
  const errorResponse = new HttpErrorResponse({
    error: 'test 404 error',
    status: 404, statusText: 'Not Found'
  });
  httpClientSpy.put.and.returnValue(asyncError(errorResponse));
};

describe('SessionComponentTest', () => {

    let httpClientSpy: { put: jasmine.Spy };
    let backendService: BackendService;

    beforeEach(() => {
      httpClientSpy = jasmine.createSpyObj('HttpClient', ['put']);
      backendService = new BackendService(httpClientSpy as any);
      spyOn(console, 'error');
    });

    it('should set session to RUNNING state after successful run session request', async () => {
      const stoppedSession = new Session('1', SessionState.RUNNING, 1337);
      backendService.updateSession(stoppedSession);
      mockRunSessionResponse(httpClientSpy, new Session('1', SessionState.RUNNING, 25 * 60));

      await backendService.runSession('1');

      expect(httpClientSpy.put.calls.count()).toBe(1, 'one call');
      backendService.sessionObservable.subscribe((session: Session) => {
        expect(session.state).toEqual(SessionState.RUNNING);
      });
    });

    it('[using fakeAsync] should set session to RUNNING state after successful run session request', fakeAsync(() => {
      const stoppedSession = new Session('1', SessionState.RUNNING, 1337);
      backendService.updateSession(stoppedSession);
      mockRunSessionResponse(httpClientSpy, new Session('1', SessionState.RUNNING, 25 * 60));

      backendService.runSession('1');
      tick(100);

      expect(httpClientSpy.put.calls.count()).toBe(1, 'one call');
      backendService.sessionObservable.subscribe((session: Session) => {
        expect(session.state).toEqual(SessionState.RUNNING);
      });
    }));

    it('should not change session state and return error on error', async () => {
      const stoppedSession = new Session('1', SessionState.RUNNING, 1337);
      backendService.updateSession(stoppedSession);
      mockRunSessionResponseError(httpClientSpy);

      await backendService.runSession('1');

      backendService.sessionObservable.subscribe(
        (session: Session) => expect(session.state).toEqual(SessionState.RUNNING),
        error => expect(error.message).toContain('test 400 error')
      );
      expect(console.error).toHaveBeenCalledTimes(1);
    });
  }
);
