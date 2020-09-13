import { Component } from '@angular/core';
import {CookieService} from 'ngx-cookie-service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'OpenKlaster';
  constructor(public cookieService: CookieService) {
  }
}

/**
 * TODO I shall leave common TODO here - I think that packages should be better nested. Maybe token package, user package,
 *  installation package etc.
 *  I see also some components not finished so I want only to say one - what will be differences between
 *  single-token-panel | token | token-panel? Good package nesting would make it easier to understand which component is used inside which one.
 *  Files in /src/app e.g. `app.component.css' should also be in some package
 *
 */
