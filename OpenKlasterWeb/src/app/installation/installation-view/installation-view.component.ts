import { Component, OnInit } from '@angular/core';
import {Installation} from "../../model/Installation";
import {ActivatedRoute, Router} from "@angular/router";
import {InstallationService} from "../../service/installation/installation.service";
import {CookieService} from "ngx-cookie-service";
import {MatDialog} from "@angular/material/dialog";
import {InstallationDto} from "../../model/InstallationDto";
import {DeleteInstallationDialogComponent} from "../delete-installation-dialog/delete-installation-dialog.component";
import {InstallationSummary} from "../../model/InstallationSummary";
import {ConfirmationDialogPopupComponent} from "../../components/confirmation-dialog-popup/confirmation-dialog-popup.component";

@Component({
  selector: 'app-installation-view',
  templateUrl: './installation-view.component.html',
  styleUrls: ['./installation-view.component.css']
})
export class InstallationViewComponent implements OnInit {

  installationId: string
  installationIdOnlyNumber: number
  installation: Installation
  installationSummary: InstallationSummary
  constructor(private router: Router, private installationsService: InstallationService,
              private route: ActivatedRoute, private cookieService: CookieService,
              private dialog: MatDialog) {
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
      this.installation = InstallationDto.fromDto(response)
      this.isLoaded = true
    })
    this.installationSummary = new InstallationSummary()
  }

  credentialsToString(credentials: JSON) {
    return JSON.stringify(credentials);
  }

  stripInstallationId(installationIdLink: string) {
    let splitInstallation = installationIdLink.split(":")
    return Number(splitInstallation[1])
  }

  openDeleteConfirmationWindow() {
    let dialog = this.dialog.open(DeleteInstallationDialogComponent, {
      width: '500px'
    });
    dialog.componentInstance.id = this.installationId
  }

  navigateToEditInstallation() {
    this.router.navigate(['editInstallation', this.installationIdOnlyNumber]).then()
  }
  navigateToCharts() {
    let dialog = this.dialog.open(ConfirmationDialogPopupComponent, {
      width: '500px'
    })
    dialog.componentInstance.popupContent = "Soon there will be redirection to charts panel for this installation."
  }

}
