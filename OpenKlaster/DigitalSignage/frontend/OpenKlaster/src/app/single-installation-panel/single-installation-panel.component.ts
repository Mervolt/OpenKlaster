import {Component, OnInit, ViewChild} from '@angular/core';
import {SingleInstallationPanelService} from "../single-installation-panel.service";
import {MatMenuTrigger} from "@angular/material/menu";
import {ActivatedRoute, Router} from "@angular/router";
import {Installation} from "../model/Installation";
import {Load} from "../model/Load";
import {Inverter} from "../model/Inverter";
import {Source} from "../model/Source";
import {InstallationPanelService} from "../installation-panel.service";
import {InstallationPanelComponent} from "../installation-panel/installation-panel.component";

@Component({
  selector: 'app-single-installation-panel',
  templateUrl: './single-installation-panel.component.html',
  styleUrls: ['./single-installation-panel.component.css']
})
export class SingleInstallationPanelComponent implements OnInit {
  @ViewChild(MatMenuTrigger) trigger: MatMenuTrigger;
  installationId;
  installation;

  //TODO ent: InstallationPanelComp is only used to get form token... services are for injecting
  // if you want to pass some methods/args as params. Here it shall be deleted at all as it used to get formToken
  // which should not exists
  constructor(public service: SingleInstallationPanelService,  private router: Router,
              public installationsService: InstallationPanelService,
              public installationPanelComponent: InstallationPanelComponent,
              private route: ActivatedRoute) {
    this.installationId = route.snapshot.paramMap.get('id');
  }

  ngOnInit(): void {
    let observableInstallation = this.installationsService.getInstallation(this.installationPanelComponent.formToken, this.installationId);
    observableInstallation.subscribe(response =>{
      //TODO ditto fromDto method
      this.installation = new Installation(this.installationPanelComponent.formToken, response['installationType'], response['longitude'],
        response['latitude'], response['description'],new Load(response['load']['name'], response['load']['description']),
        new Inverter(response['inverter']['description'], response['inverter']['manufacturer'],
          response['inverter']['credentials'], response['inverter']['modelType']),
        new Source(response['source']['azimuth'], response['source']['tilt'], response['source']['capacity'],
          response['source']['description']))
    })
  }

}
