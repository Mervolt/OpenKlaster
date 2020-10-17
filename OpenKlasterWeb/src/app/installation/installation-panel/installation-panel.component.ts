import {Component, OnInit} from '@angular/core';
import {InstallationService} from '../../service/installation/installation.service';

import {Installation} from '../../model/Installation';
import {AppComponent} from "../../app.component";
import {InstallationDto} from "../../model/InstallationDto";
import {CookieService} from "ngx-cookie-service";
import {PageEvent} from "@angular/material/paginator";
import {Router} from "@angular/router";

@Component({
  selector: 'app-installation-panel',
  templateUrl: './installation-panel.component.html',
  styleUrls: ['./installation-panel.component.css']
})
export class InstallationPanelComponent implements OnInit {
  installations: Installation[] = [];
  displayedInstallations: Installation[] = [];
  cookieService: CookieService;

  constructor(public installationService: InstallationService, private appComp: AppComponent, private router: Router) {
    this.cookieService = appComp.cookieService;
  }

  ngOnInit(): void {
    this.getInstallations();
  }

  onPageChange(event: PageEvent) {
    let startIndex = event.pageIndex * event.pageSize;
    let endIndex = startIndex + event.pageSize;
    if (endIndex > this.installations.length) {
      endIndex = this.installations.length;
    }
    this.displayedInstallations = this.installations.slice(startIndex, endIndex);
  }

  async getInstallations() {
    this.installations = []
    let observableInstallations = this.installationService.getInstallations(this.cookieService);
    observableInstallations.subscribe(response => {
      for (let installation in response) {
        this.installations.push(InstallationDto.fromDto(response[installation]))
        this.displayedInstallations = this.installations.slice(0, 5);
      }
    })
  }

  navigateToInstallation(installationId: string) {
    this.router.navigate(['installations', installationId]).then()
  }
}
