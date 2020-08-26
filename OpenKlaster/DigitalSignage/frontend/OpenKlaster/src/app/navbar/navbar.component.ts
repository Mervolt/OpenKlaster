import {Component, OnInit, ViewChild} from '@angular/core';
//TODO unused import
import {LoginFormComponent} from '../login-form/login-form.component';
import {MatMenuTrigger} from "@angular/material/menu";
import {SingleInstallationPanelService} from "../single-installation-panel.service";
import {Router} from "@angular/router";
import {AppComponent} from "../app.component";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  @ViewChild(MatMenuTrigger) trigger: MatMenuTrigger;
  constructor(public service: SingleInstallationPanelService, private router: Router, private appComp: AppComponent) { }

  ngOnInit(): void {
  }

  //TODO Maybe some global navigation/routing service would be nice as in login-form we are also using routing
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

  logout() {
    this.appComp.cookieService.deleteAll();
    this.router.navigate(['login']).then();
  }
}
