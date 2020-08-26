import { Component, OnInit } from '@angular/core';
import {Installation} from "../model/Installation";
import {Load} from "../model/Load";
import {Inverter} from "../model/Inverter";
import {Source} from "../model/Source";
import {TokenPanelService} from "../token-panel.service";
import {InstallationPanelService} from "../installation-panel.service";
import {AppComponent} from "../app.component";
import {CookieService} from "ngx-cookie-service";

@Component({
  selector: 'app-installation-generation-panel',
  templateUrl: './installation-generation-panel.component.html',
  styleUrls: ['./installation-generation-panel.component.css']
})
export class InstallationGenerationPanelComponent implements OnInit {
  //TODO Angular does not have some kind of default constructor which? And some field won't be automatically assigned
  // to default values as in Java e.g. 0 for number-type values?
  formModel = new Installation('', '', 0, 0, '', new Load('', ''),
    new Inverter('', '', '', ''), new Source(0, 0, 0, ''));
  formToken = '';
  //TODO shouldn't there be some type of value e.g. `cookieService: CookieService;`? as this value is not defined by default
  // In addition - I think that some wrapper service around this cookieService would be nice so we could easily get sessionTokens
  cookieService: CookieService;

  //TODO you are injecting component here only to use cookieService it provides?
  // I am not angular expert but isn't component some kind of GUI puzzle on which we build our GUI?
  // We should have service injected here I think,

  //TODO#2 service: InstallationPanelService -> this name should be more descriptive e.g. InstallationService
  constructor(public tokenService: TokenPanelService, public service: InstallationPanelService,
              private appComp: AppComponent) {
    this.cookieService=appComp.cookieService;
  }

  ngOnInit(): void {
    if(this.formToken == '')
      this.downloadToken()
  }

  //TODO it is rather "getToken" than downloadToken - in addition we should not use apiToken for this purpose
  // It is even funnier to see that there is sessionToken used to get apiToken
  async downloadToken() {
    await this.tokenService
      .getTokens(this.appComp.cookieService)
      .toPromise()
      .then(
        response => this.formToken = response['userTokens'][0]['data']
      );
  }

  onSubmit() {
    //TODO addInstallation(this.formModel, this.cookieService, this.formToken)
    this.service.addInstallation(this.formModel, this.cookieService, this.formToken);
    this.cookieService.
  }
}
