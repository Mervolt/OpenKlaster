import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {Installation} from "../../model/Installation";
import {InstallationService} from "../../service/installation.service";
import {CookieService} from "ngx-cookie-service";
import {CredentialsFormComponent} from "../../credentials/credentials-form/credentials-form.component";

@Component({
  selector: 'app-installation-generation-panel',
  templateUrl: './installation-generation-panel.component.html',
  styleUrls: ['./installation-generation-panel.component.css']
})
export class InstallationGenerationPanelComponent implements AfterViewInit {
  requestReceivedState = 'wait'
  sendRequestState = 'wait'
  submittedObjectName = 'Installation'
  manufacturers: string[] = ['Growatt', 'Just for option']

  @ViewChild(CredentialsFormComponent) credentials;
  formModel = new Installation();

  constructor(public installationService: InstallationService,
              private cookieService: CookieService) {
  }

  ngOnInit(): void {

  }

  ngAfterViewInit() {
  }

  onSubmit() {
    this.sendRequestState = 'sent'
    let addPromise = this.installationService.addInstallation(this.formModel, this.cookieService);
    addPromise
      .then(() => {
        this.sendRequestState = 'received'
        this.requestReceivedState = 'success'
      })
      .catch(() => {
        this.sendRequestState = 'received'
        this.requestReceivedState = 'fail'
      })
  }

  receiveMessage($event) {
    this.formModel.inverter.credentials = $event
  }

  convertManufacturerToCredentials() {
    switch (this.formModel.inverter.manufacturer) {
      case 'Growatt': {
        return 'userpass'
      }
      case 'Just for option' : {
        return 'token'
      }
      default : {
        return 'empty'
      }
    }
  }
}
