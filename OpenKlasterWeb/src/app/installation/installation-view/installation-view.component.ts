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
  summaryProperties: Map<string, any>

  constructor(private router: Router, private installationsService: InstallationService,
              private route: ActivatedRoute, private cookieService: CookieService,
              private dialog: MatDialog) {}

  ngOnInit(): void {
    this.installationId = this.route.snapshot.paramMap.get('id');
    this.installationIdOnlyNumber = this.stripInstallationIdIfRequired(this.installationId)
    this.getInstallation(this.installationIdOnlyNumber).then(() => {
    })

    let observableSummary = this.installationsService.getInstallationSummary(this.cookieService, this.installationIdOnlyNumber);
    observableSummary.subscribe(response => {
      this.installationSummary = InstallationSummary.fromDto(response)
      let outp = new Map<string, any>();
      outp.set('Total energy', this.installationSummary.totalEnergy + " kWh")
      outp.set('Today energy', this.installationSummary.todayEnergy + " kWh")
      outp.set('Current power', this.installationSummary.currentPower + " kW")
      outp.set('Trees saved', this.installationSummary.environmentalBenefits.treesSaved)
      outp.set('Co2 reduced', this.installationSummary.environmentalBenefits.co2Reduced + " tons")
      this.summaryProperties = outp
    })
  }

  async getInstallation(id: number) {
    let observableInstallation = this.installationsService.getInstallation(this.cookieService, id);
    observableInstallation.subscribe(response => {
      this.installation = InstallationDto.fromDto(response)
    })

  }

  stripInstallationIdIfRequired(installationId: string) {
    if (!installationId.includes(":")) {
      return Number(installationId)
    } else {
      let splitInstallation = installationId.split(":")
      return Number(splitInstallation[1])
    }
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
    this.router.navigate(['charts'], {queryParams: {'installationId': [this.installationId]}}).then()
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
    return this.summaryProperties;
  }



}
