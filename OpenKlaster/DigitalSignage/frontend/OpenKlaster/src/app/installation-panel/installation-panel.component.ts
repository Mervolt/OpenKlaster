import {Component, OnInit} from '@angular/core';
import {InstallationPanelService} from '../installation-panel.service';

import {Installation} from '../model/Installation';
import {Load} from '../model/Load';
import {Source} from '../model/Source';
import {Inverter} from '../model/Inverter';
import {AppComponent} from "../app.component";
import {TokenPanelService} from "../token-panel.service";

@Component({
  selector: 'app-installation-panel',
  templateUrl: './installation-panel.component.html',
  styleUrls: ['./installation-panel.component.css']
})
export class InstallationPanelComponent implements OnInit {
  formModel = new Installation('', '', 0, 0, '', new Load('', ''),
    new Inverter('', '', '', ''), new Source(0, 0, 0, ''));
  formToken = '';
  installations: Installation[] = [];
  cookieService;

  constructor(public tokenService: TokenPanelService, public service: InstallationPanelService, private appComp: AppComponent) {
    this.cookieService = appComp.cookieService;
  }

  ngOnInit(): void {
    this.getInstallations();
  }


  async downloadToken() {
    await this.tokenService
      .getTokens(this.appComp.cookieService)
      .toPromise()
      .then(
        response => {
          this.formToken = response['userTokens'][0]['data']
          return this.formToken
        }
      );
  }

  async getInstallations() {
    await this.downloadToken();
    this.installations = []
    let observableInstallations = this.service.getInstallations(this.cookieService, this.formToken);
    observableInstallations.subscribe(response => {
      for (let install in response){
        install = response[install]
        this.installations.push(new Installation(install['_id'], install['installationType'], install['longitude'],
          install['latitude'], install['description'], install['load'], install['inverter'], install['source']))
      }})
  }

}
