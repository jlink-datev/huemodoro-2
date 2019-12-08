import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {RouterModule} from '@angular/router';
import {SessionComponent} from './session/session.component';
import {PageNotFoundComponent} from './page-not-found/page-not-found.component';
import {BackendService} from './services/backend.service';
import {HttpClientModule} from '@angular/common/http';

@NgModule({
  declarations: [
    AppComponent,
    SessionComponent,
    PageNotFoundComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    RouterModule.forRoot([
      {path: '', redirectTo: '/session/1', pathMatch: 'full'},
      {path: 'session/:sessionId', component: SessionComponent},
      {path: '**', component: PageNotFoundComponent},
    ])
  ],
  exports: [RouterModule],
  providers: [BackendService],
  bootstrap: [AppComponent]
})
export class AppModule {
}
