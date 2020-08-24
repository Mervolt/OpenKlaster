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
    if (this.formToken == '')
      this.downloadToken();
    this.getInstallations();
  }


  async downloadToken() {
    await this.tokenService
      .getTokens(this.appComp.cookieService)
      .toPromise()
      .then(
        response => this.formToken = response['userTokens'][0]['data']
      );
  }

  async getInstallations() {
    this.installations = []
    let observableInstallations = this.service.getInstallations(this.formToken);
    observableInstallations.subscribe(response => {
      for (let install in response){
        this.installations.push(new Installation(this.formToken, install['installationType'], install['longitude'],
          install['latitude'], install['description'], new Load(install['load']['name'], install['load']['description']),
          new Inverter(response['inverter']['description'], install['inverter']['manufacturer'],
            install['inverter']['credentials'], install['inverter']['modelType']),
          new Source(install['source']['azimuth'], install['source']['tilt'], install['source']['capacity'],
            install['source']['description'])))
      }})
  }

}
