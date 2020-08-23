import { Component, OnInit } from '@angular/core';
import {TokenPanelService} from "../token-panel.service";
import {AppComponent} from "../app.component";

@Component({
  selector: 'app-token-generation-panel',
  templateUrl: './token-generation-panel.component.html',
  styleUrls: ['./token-generation-panel.component.css']
})
export class TokenGenerationPanelComponent implements OnInit {
  cookieService;

  constructor(public service: TokenPanelService, public appComp: AppComponent) {
    this.cookieService = appComp.cookieService;
  }

  ngOnInit(): void {
  }

  addToken() {
    this.service.addToken(this.cookieService).subscribe();
  }

}
