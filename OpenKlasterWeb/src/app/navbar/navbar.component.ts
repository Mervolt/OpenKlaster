import {Component, OnInit, ViewChild} from '@angular/core';
import {MatMenuTrigger} from "@angular/material/menu";
import {SingleInstallationPanelService} from "../installation/single-installation-panel.service";
import {Router} from "@angular/router";
import {AppComponent} from "../app.component";
import {NavigationNameAddressTuple} from "../model/NavigationNameAddressTuple";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  @ViewChild(MatMenuTrigger) trigger: MatMenuTrigger;

  links: NavigationNameAddressTuple[] = [
    new NavigationNameAddressTuple('Tokens', this.navigateToYourTokens.bind(this)),
    new NavigationNameAddressTuple('Add installation', this.navigateToInstallationGeneration.bind(this)),
    new NavigationNameAddressTuple('Your installations', this.navigateToYourInstallations.bind(this)),
    new NavigationNameAddressTuple('API Documentation', this.navigateToSwagger.bind(this)),
    new NavigationNameAddressTuple('Logout', this.logout.bind(this))
  ]

  constructor(public service: SingleInstallationPanelService, private router: Router, private appComp: AppComponent) {
  }

  ngOnInit(): void {
  }


  navigateToYourTokens(): void {
    this.router.navigate(['token']).then();
  }

  navigateToYourInstallations(): void {
    this.router.navigate(['installations']).then();
  }

  navigateToInstallationGeneration(): void {
    this.router.navigate(['installationGeneration']).then();
  }

  navigateToSwagger(): void {
    this.router.navigate(['swagger-ui']).then();
  }

  logout() {
    this.appComp.cookieService.deleteAll();
    this.router.navigate(['login']).then();
  }

  route(navigation: Function) {
    navigation()
  }
}
