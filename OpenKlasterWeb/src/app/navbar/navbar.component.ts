import {Component, OnInit, ViewChild} from '@angular/core';
import {MatMenuTrigger} from '@angular/material/menu';
import {SingleInstallationPanelService} from '../installation/single-installation-panel.service';
import {Router} from '@angular/router';
import {AppComponent} from '../app.component';
import {NavigationNameAddressTuple} from '../model/NavigationNameAddressTuple';
import {EndpointHolder} from '../model/EndpointHolder';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  @ViewChild(MatMenuTrigger) trigger: MatMenuTrigger;
  navbarPrefix: String = 'Navbar_';

  links: NavigationNameAddressTuple[] = [
    new NavigationNameAddressTuple(this.navbarPrefix + 'YourInstallations', this.navigateToYourInstallations.bind(this)),
    new NavigationNameAddressTuple(this.navbarPrefix + 'AddInstallation', this.navigateToInstallationGeneration.bind(this)),
    new NavigationNameAddressTuple(this.navbarPrefix + 'Charts', this.navigateToCharts.bind(this)),
    new NavigationNameAddressTuple(this.navbarPrefix + 'UserPanel', this.navigateToUserPanel.bind(this)),
    new NavigationNameAddressTuple(this.navbarPrefix + 'APIDocumentation', this.navigateToSwagger.bind(this)),
    new NavigationNameAddressTuple(this.navbarPrefix + 'DigitalSignage', this.navigateToDigitalSignage.bind(this)),
    new NavigationNameAddressTuple(this.navbarPrefix + 'Logout', this.logout.bind(this))
  ];

  constructor(public service: SingleInstallationPanelService, private router: Router, private appComp: AppComponent) {
  }

  ngOnInit(): void {
  }

  navigateToUserPanel(): void {
    this.router.navigate(['user']).then();
  }

  navigateToYourInstallations(): void {
    this.router.navigate(['installations']).then();
  }

  navigateToInstallationGeneration(): void {
    this.router.navigate(['installationGeneration']).then();
  }

  navigateToSwagger(): void {
    window.open(EndpointHolder.swaggerEndpoint);
  }

  navigateToCharts(): void {
    this.router.navigate(['charts']).then();
  }

  navigateToDigitalSignage(): void{
    this.router.navigate(['digitalSignage']).then();
  }

  logout() {
    this.appComp.cookieService.deleteAll();
    this.router.navigate(['login']).then();
  }

  route(navigation: Function) {
    navigation();
  }
}
