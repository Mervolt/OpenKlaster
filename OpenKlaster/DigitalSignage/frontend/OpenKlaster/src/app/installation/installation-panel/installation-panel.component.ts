import {Component, OnInit} from '@angular/core';
import {InstallationPanelService} from '../installation-panel.service';

import {Installation} from '../../model/Installation';
import {Load} from '../../model/Load';
import {Source} from '../../model/Source';
import {Inverter} from '../../model/Inverter';
import {AppComponent} from "../../app.component";
import {TokenPanelService} from "../../token/token-panel.service";
import {InstallationDto} from "../../model/InstallationDto";
import {CookieService} from "ngx-cookie-service";
import {CookieHolder} from "../../model/CookieHolder";

@Component({
  selector: 'app-installation-panel',
  templateUrl: './installation-panel.component.html',
  styleUrls: ['./installation-panel.component.css']
})
export class InstallationPanelComponent implements OnInit {
  //TODO unused formModel
  //MM-ANSWER done
  formToken = '';
  installations: Installation[] = [];
  //TODO ditto - value type
  //MM-ANSWER done
  cookieService: CookieService;

  //TODO installationService - more desriptive name
  //MM-ANSWER done
  constructor(public tokenService: TokenPanelService, public installationService: InstallationPanelService,
              private appComp: AppComponent) {
    this.cookieService = appComp.cookieService;
  }

  ngOnInit(): void {
    this.getInstallations();
  }


  //TODO ditto -sesion token
  async downloadToken() {
    await this.tokenService
      //Todo you have cookieService variable declared in this class
      //MM-ANSWER done
      .getTokens(this.cookieService)
      .toPromise()
      .then(
        response => {
          this.formToken = this.appComp.cookieService.get(CookieHolder.tokenKey)
          return this.formToken
        }
      );
  }

  async getInstallations() {
    await this.downloadToken();
    this.installations = []
    let observableInstallations = this.installationService.getInstallations(this.cookieService, this.formToken);
    observableInstallations.subscribe(response => {
      for (let installation in response){
        //TODO ditto - static keys
        //MM-ASNWER done
        this.installations.push(InstallationDto.fromDto(response[installation]))
      }})
  }

}
