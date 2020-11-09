import {Component, OnInit} from '@angular/core';
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
    this.installationSummary.currentPower= 32.4;
    this.installationSummary.totalEnergy= 3202.4;
    this.installationSummary.todayEnergy= 603.4;
    this.installationSummary.environmentalBenefits.co2Reduced= 463;
    this.installationSummary.environmentalBenefits.treesSaved= 321;
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

  getInverterProperties(): Map<string, any> {
    let outp = new Map<string, any>();
    outp.set('Model type', this.installation.inverter.modelType)
    outp.set('Manufacturer', this.installation.inverter.manufacturer)
    return outp
  }

  getSourceProperties(): Map<string, any> {
    let outp = new Map<string, any>();
    outp.set('Latitude', this.installation.latitude)
    outp.set('Longitude', this.installation.longitude)
    outp.set('Tilt', this.installation.source.tilt)
    outp.set('Azimuth', this.installation.source.azimuth)
    outp.set('Capacity', this.installation.source.capacity)
    return outp
  }

  getLoadProperties(): Map<string, any> {
    let outp = new Map<string, any>();
    outp.set('Name', this.installation.load.name)
    return outp
  }

  getSummaryProperties(): Map<string, any> {
    let outp = new Map<string, any>();
    outp.set('Total energy', this.installationSummary.totalEnergy + " kW")
    outp.set('Today energy', this.installationSummary.todayEnergy + " kW")
    outp.set('Current power', this.installationSummary.currentPower + " W")
    outp.set('Trees saved', this.installationSummary.environmentalBenefits.treesSaved)
    outp.set('Co2 reduced', this.installationSummary.environmentalBenefits.co2Reduced + " tons")
    return outp;
  }

}