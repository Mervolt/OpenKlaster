import {Component, OnInit} from '@angular/core';
import {TokenPanelService} from '../token-panel.service';
import {AppComponent} from '../../app.component';

@Component({
  selector: 'app-token-panel',
  templateUrl: './token-panel.component.html',
  styleUrls: ['./token-panel.component.css']
})
export class TokenPanelComponent implements OnInit {
  tokens;
  cookieService;

  //TODO change `service` variable name
  //MM-ANSWER Done
  constructor(public tokenPanelService: TokenPanelService, public appComp: AppComponent) {
    this.cookieService = appComp.cookieService;
    let request = tokenPanelService.getTokens(appComp.cookieService);
    request.subscribe(response => {
      this.tokens = response["userTokens"]
    })
  }

  ngOnInit(): void {
  }
}
