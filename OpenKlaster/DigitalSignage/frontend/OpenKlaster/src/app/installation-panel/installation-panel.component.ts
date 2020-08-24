import { Component, OnInit } from '@angular/core';
import { InstallationPanelService } from '../installation-panel.service';

import { Installation } from '../model/Installation';
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
  isFormHidden = true;
  cookieService;
  areInstallationsHidden = true;

  constructor(public tokenService: TokenPanelService, public service: InstallationPanelService, private appComp: AppComponent) {
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

  async getInstallations() {
    this.installations = []
    let observableInstallations = this.service.getInstallations(this.formToken);
    for(let install of observableInstallations){
      install.subscribe(response =>{
        this.installations.push(new Installation('', response['installationType'], response['longitude'],
          response['latitude'], response['description'],new Load(response['load']['name'], response['load']['description']),
          new Inverter(response['inverter']['description'], response['inverter']['manufacturer'],
            response['inverter']['credentials'], response['inverter']['modelType']),
          new Source(response['source']['azimuth'], response['source']['tilt'], response['source']['capacity'],
            response['source']['description'])))
      })
    }
  }

  onSubmit() {
    this.service.addInstallation(this.formModel, this.cookieService);
  }

  displayInstallations() {
    this.areInstallationsHidden = false;
  }

  hideInstallations() {
    this.areInstallationsHidden = true;
  }

  displayForm() {
    this.isFormHidden = false;
  }

  hideForm() {
    this.isFormHidden = true;
  }
}
