import {Component, OnInit, ViewChild} from '@angular/core';
import {SingleInstallationPanelService} from "../single-installation-panel.service";
import {MatMenuTrigger} from "@angular/material/menu";
import {ActivatedRoute, Router} from "@angular/router";
import {InstallationPanelService} from "../installation-panel.service";
import {InstallationDto} from "../../model/InstallationDto";
import {CookieService} from "ngx-cookie-service";

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
  //MM-ANSWER tokens to be refactored later
  constructor(public service: SingleInstallationPanelService, private router: Router,
              public installationsService: InstallationPanelService,
              private route: ActivatedRoute, public cookieService: CookieService) {
    this.installationId = route.snapshot.paramMap.get('id');
  }

  //TOdo RG Have you tested it? I'm pretty sure it won't work?
  // For example insteal of new Inverter(response['inverter']['description'], response['inverter']['manufacturer'],
  //           response['inverter']['credentials'], response['inverter']['modelType'])
  // The 2nd parenthesis will throw an exception everywhere
  // If you want inverter object just use response['inverter'] and you get the object
  //MM-ANSWER I think it worked but I changed it anyway since your solution also works and is used in Installations
  ngOnInit(): void {
    let observableInstallation = this.installationsService.getInstallation(this.cookieService, this.installationId);
    observableInstallation.subscribe(response => {
      //TODO ditto fromDto method
      //MM-ANSWER Done
      this.installation = InstallationDto.fromDto(response)
    })
  }

}
