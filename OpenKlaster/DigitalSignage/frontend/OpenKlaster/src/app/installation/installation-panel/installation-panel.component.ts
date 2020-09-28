import {Component, OnInit} from '@angular/core';
import {InstallationPanelService} from '../installation-panel.service';

import {Installation} from '../../model/Installation';
import {AppComponent} from "../../app.component";
import {InstallationDto} from "../../model/InstallationDto";
import {CookieService} from "ngx-cookie-service";

@Component({
  selector: 'app-installation-panel',
  templateUrl: './installation-panel.component.html',
  styleUrls: ['./installation-panel.component.css']
})
export class InstallationPanelComponent implements OnInit {
  //TODO unused formModel
  //MM-ANSWER done
  installations: Installation[] = [];
  //TODO ditto - value type
  //MM-ANSWER done
  cookieService: CookieService;

  //TODO installationService - more desriptive name
  //MM-ANSWER done
  constructor(public installationService: InstallationPanelService, private appComp: AppComponent) {
    this.cookieService = appComp.cookieService;
  }

  ngOnInit(): void {
    this.getInstallations();
  }


  //TODO ditto -sesion token

  async getInstallations() {
    this.installations = []
    let observableInstallations = this.installationService.getInstallations(this.cookieService);
    observableInstallations.subscribe(response => {
      for (let installation in response) {
        //TODO ditto - static keys
        //MM-ASNWER done
        this.installations.push(InstallationDto.fromDto(response[installation]))
      }
    })
  }

}
