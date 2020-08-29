import { Component, OnInit } from '@angular/core';
import {TokenPanelService} from "../token-panel.service";
import {AppComponent} from "../app.component";

@Component({
  selector: 'app-token-generation-panel',
  templateUrl: './token-generation-panel.component.html',
  styleUrls: ['./token-generation-panel.component.css']
})
//Todo RG It seems to me that it is more obvious for the user to generate a token from a button.
//I would take this functionality beyond the dropdown
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
