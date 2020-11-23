import {Component} from '@angular/core';
import {CookieService} from 'ngx-cookie-service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'OpenKlaster';
  backgroundImages = ['url(/assets/img/background.jpg)',
    'url(/assets/img/background2.jpg)']
  background = 'url(/assets/img/background.jpg)';

  constructor(public cookieService: CookieService) {
  }

  refreshBackground(backgroundNumber: number) {
    this.background = this.backgroundImages[backgroundNumber]
    if (backgroundNumber == this.backgroundImages.length - 1)
      backgroundNumber = 0
    else
      backgroundNumber++
    setTimeout(() => this.refreshBackground(backgroundNumber), 10000)
  }
}
