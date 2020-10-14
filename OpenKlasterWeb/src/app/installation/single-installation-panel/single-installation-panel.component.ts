import {Component, OnInit, ViewChild} from '@angular/core';
import {SingleInstallationPanelService} from "../single-installation-panel.service";
import {MatMenuTrigger} from "@angular/material/menu";
import {ActivatedRoute, Router} from "@angular/router";
import {InstallationService} from "../../service/installation.service";
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

  constructor(public service: SingleInstallationPanelService, private router: Router,
              public installationsService: InstallationService,
              private route: ActivatedRoute, public cookieService: CookieService,
              public dialog: MatDialog) {
    this.installationId = route.snapshot.paramMap.get('id');
    this.installationIdOnlyNumber = this.stripInstallationId(this.installationId)
  }

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

  credentialsToString(credentials: JSON) {
    return JSON.stringify(credentials);
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

  navigateToEditInstallation() {
    this.router.navigate(['editInstallation', this.installationIdOnlyNumber]).then()
  }
}
