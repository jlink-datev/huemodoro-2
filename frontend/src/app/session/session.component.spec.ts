import {SessionComponent} from './session.component';
import {ComponentFixture, TestBed} from '@angular/core/testing';
import {BackendService} from '../services/backend.service';
import {RouterTestingModule} from '@angular/router/testing';

describe('SessionComponentTest', () => {

    let fixture: ComponentFixture<SessionComponent>;
    let backendServiceSpy: { runSession: jasmine.Spy };
    let component: SessionComponent;

    beforeEach((() => {
      backendServiceSpy = jasmine.createSpyObj('BackendService', ['runSession', 'cancelPolling']);
      fixture = TestBed.configureTestingModule({
        declarations: [SessionComponent],
        providers: [{provide: BackendService, useValue: backendServiceSpy}],
        imports: [RouterTestingModule]
      }).createComponent(SessionComponent);
      component = fixture.componentInstance;
    }));

    it('should create', () => {
      expect(component).toBeDefined();
    });

    it('should call backend run session', () => {
      component.runSession();

      expect(backendServiceSpy.runSession.calls.count()).toEqual(1, 'one call');
    });
  }
);
