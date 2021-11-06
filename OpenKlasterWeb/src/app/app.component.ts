import {Component} from '@angular/core';
import {CookieService} from 'ngx-cookie-service';
import {TranslateService} from '@ngx-translate/core';
import {DateAdapter} from '@angular/material/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'OpenKlaster';
  backgroundImages = ['url(/assets/img/background.jpg)',
    'url(/assets/img/background2.jpg)'];
  background = 'url(/assets/img/background.jpg)';

  constructor(public cookieService: CookieService,
              public translateService: TranslateService,
              private dateAdapter: DateAdapter<Date>) {
    this.translateService.addLangs(['en-US', 'pl-PL', 'pl', 'en']);

    if (navigator.language.startsWith('en')) {
      this.translateService.setDefaultLang('en-US');
      this.dateAdapter.setLocale('en-US');
    }
    else {
      this.translateService.setDefaultLang('pl-PL');
      this.dateAdapter.setLocale('pl-PL');
    }
  }

  refreshBackground(backgroundNumber: number) {
    this.background = this.backgroundImages[backgroundNumber];
    if (backgroundNumber == this.backgroundImages.length - 1) {
      backgroundNumber = 0;
    }
    else {
      backgroundNumber++;
    }
    setTimeout(() => this.refreshBackground(backgroundNumber), 10000);
  }
}
