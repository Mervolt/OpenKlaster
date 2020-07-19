import { Component, OnInit } from '@angular/core';
import { TokenPanelService } from '../token-panel.service';
import {AppComponent} from '../app.component';

@Component({
  selector: 'app-token-panel',
  templateUrl: './token-panel.component.html',
  styleUrls: ['./token-panel.component.css']
})
export class TokenPanelComponent implements OnInit {
  tokens;

  constructor(service: TokenPanelService, public appComp: AppComponent) {
    this.tokens = service.getTokens(appComp.cookieService);
    this.tokens.subscribe(response =>{
      return response.body.tokens;
    })
  }

  ngOnInit(): void {
  }

}
