import {Component, OnInit, ViewChild} from '@angular/core';
import {SingleInstallationPanelService} from "../single-installation-panel.service";
import {MatMenuTrigger} from "@angular/material/menu";
import {ActivatedRoute, Router} from "@angular/router";
import {InstallationPanelService} from "../installation-panel.service";
import {InstallationDto} from "../../model/InstallationDto";
import {CookieService} from "ngx-cookie-service";
import {Installation} from "../../model/Installation";
import {MatDialog} from "@angular/material/dialog";
import {DeleteInstallationDialogComponent} from "../delete-installation-dialog/delete-installation-dialog.component";

@Component({
  selector: 'app-single-installation-panel',
  templateUrl: './single-installation-panel.component.html',
  styleUrls: ['./single-installation-panel.component.css']
})
export class SingleInstallationPanelComponent implements OnInit {
  @ViewChild(MatMenuTrigger) trigger: MatMenuTrigger;
  installationId: string
  installationIdOnlyNumber: number
  installation: Installation
  isPopupShown: boolean = false;

  //TODO ent: InstallationPanelComp is only used to get form token... services are for injecting
  // if you want to pass some methods/args as params. Here it shall be deleted at all as it used to get formToken
  // which should not exists
  //MM-ANSWER tokens to be refactored later
  constructor(public service: SingleInstallationPanelService, private router: Router,
              public installationsService: InstallationPanelService,
              private route: ActivatedRoute, public cookieService: CookieService,
              public dialog: MatDialog) {
    this.installationId = route.snapshot.paramMap.get('id');
    this.installationIdOnlyNumber = this.stripInstallationId(this.installationId)
  }

  //TOdo RG Have you tested it? I'm pretty sure it won't work?
  // For example insteal of new Inverter(response['inverter']['description'], response['inverter']['manufacturer'],
  //           response['inverter']['credentials'], response['inverter']['modelType'])
  // The 2nd parenthesis will throw an exception everywhere
  // If you want inverter object just use response['inverter'] and you get the object
  //MM-ANSWER I think it worked but I changed it anyway since your solution also works and is used in Installations
  isLoaded: boolean;

  ngOnInit(): void {
    this.getInstallation(this.installationIdOnlyNumber)
  }

  async getInstallation(id: number) {
    let observableInstallation = this.installationsService.getInstallation(this.cookieService, id);
    observableInstallation.subscribe(response => {
      //TODO ditto fromDto method
      //MM-ANSWER Done
      this.installation = InstallationDto.fromDto(response)
      this.isLoaded = true
    })
  }

  stripInstallationId(installationIdLink: string) {
    let splitInstallation = installationIdLink.split(":")
    return Number(splitInstallation[1])
  }

  openDialog() {
    let dialog = this.dialog.open(DeleteInstallationDialogComponent, {
      width: '500px'
    });
    dialog.componentInstance.id = this.installationId
  }

  openChartPopup() {
    this.isPopupShown = true;
  }

  navigateToEditInstallation() {
    this.router.navigate(['editInstallation', this.installationIdOnlyNumber]).then()
  }
}
