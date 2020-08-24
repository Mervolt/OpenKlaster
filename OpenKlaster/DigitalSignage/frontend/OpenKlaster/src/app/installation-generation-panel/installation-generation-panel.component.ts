import { Component, OnInit } from '@angular/core';
import {Installation} from "../model/Installation";
import {Load} from "../model/Load";
import {Inverter} from "../model/Inverter";
import {Source} from "../model/Source";
import {TokenPanelService} from "../token-panel.service";
import {InstallationPanelService} from "../installation-panel.service";
import {AppComponent} from "../app.component";

@Component({
  selector: 'app-installation-generation-panel',
  templateUrl: './installation-generation-panel.component.html',
  styleUrls: ['./installation-generation-panel.component.css']
})
export class InstallationGenerationPanelComponent implements OnInit {
  formModel = new Installation('', '', 0, 0, '', new Load('', ''),
    new Inverter('', '', '', ''), new Source(0, 0, 0, ''));
  formToken = '';
  cookieService;

  constructor(public tokenService: TokenPanelService, public service: InstallationPanelService,
              private appComp: AppComponent) {
    this.cookieService=appComp.cookieService;
  }

  ngOnInit(): void {
    if(this.formToken == '')
      this.downloadToken()
  }


  async downloadToken() {
    await this.tokenService
      .getTokens(this.appComp.cookieService)
      .toPromise()
      .then(
        response => this.formToken = response['userTokens'][0]['data']
      );
  }

  onSubmit() {
    this.service.addInstallation(this.formModel, this.cookieService, this.formToken);
  }
}
