import {Component, OnInit, ViewChild} from '@angular/core';
import {SingleInstallationPanelService} from "../single-installation-panel.service";
import {MatMenuTrigger} from "@angular/material/menu";
import {Router} from "@angular/router";

@Component({
  selector: 'app-single-installation-panel',
  templateUrl: './single-installation-panel.component.html',
  styleUrls: ['./single-installation-panel.component.css']
})
export class SingleInstallationPanelComponent implements OnInit {
  @ViewChild(MatMenuTrigger) trigger: MatMenuTrigger;
  constructor(public service: SingleInstallationPanelService,  private router: Router) { }

  ngOnInit(): void {
  }

  navigateToYourTokens(): void {
    this.router.navigate(['token']).then();
  }

  navigateToTokenGeneration() {
    this.router.navigate(['tokenGeneration']).then();
  }

  navigateToYourInstallations(): void {
    this.router.navigate(['installations']).then();
  }

  navigateToInstallationGeneration(): void {
    this.router.navigate(['installationGeneration']).then();
  }
}
