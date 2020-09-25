import { Component } from '@angular/core';
import {CookieService} from 'ngx-cookie-service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'OpenKlaster';
  backgroundImages = ['url(/assets/img/background.jpg)',
  'url(/assets/img/background2.jpg)',
  'url(/assets/img/background3.jpg)']
  background = 'url(/assets/img/background.jpg)';
  constructor(public cookieService: CookieService) {
    this.refreshBackground(0)
  }

  refreshBackground(backgroundNumber: number){
    this.background=this.backgroundImages[backgroundNumber]
    if(backgroundNumber == this.backgroundImages.length - 1)
      backgroundNumber=0
    else
      backgroundNumber++
    setTimeout(() => this.refreshBackground(backgroundNumber), 10000)
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
