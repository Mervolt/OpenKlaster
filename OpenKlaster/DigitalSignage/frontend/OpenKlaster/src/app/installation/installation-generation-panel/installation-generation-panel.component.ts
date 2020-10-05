import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {Installation} from "../../model/Installation";
import {InstallationPanelService} from "../installation-panel.service";
import {CookieService} from "ngx-cookie-service";
import {CredentialsFormComponent} from "../../credentials-form/credentials-form.component";

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
  //TODO Angular does not have some kind of default constructor which? And some field won't be automatically assigned
  // to default values as in Java e.g. 0 for number-type values?
  //MM-ANSWER: undefined is set for everything
  formModel = new Installation();
  //TODO shouldn't there be some type of value e.g. `cookieService: CookieService;`? as this value is not defined by default
  // In addition - I think that some wrapper service around this cookieService would be nice so we could easily get sessionTokens
  //MM-ANSWER: I dont understand, did you mean formToken: string  = '';? cookieService: CookieService already has type

  //TODO you are injecting component here only to use cookieService it provides?
  // I am not angular expert but isn't component some kind of GUI puzzle on which we build our GUI?
  // We should have service injected here I think,
  //MM-ANSWER: Done

  //TODO#2 service: InstallationPanelService -> this name should be more descriptive e.g. InstallationService
  //MM-ANSWER done
  constructor(public installationService: InstallationPanelService,
              private cookieService: CookieService) {
  }

  ngOnInit(): void {

  }

  ngAfterViewInit() {
  }

  //TODO it is rather "getToken" than downloadToken - in addition we should not use apiToken for this purpose
  // It is even funnier to see that there is sessionToken used to get apiToken
  //MM-ANSWER still need to replace tokens

  onSubmit() {
    //TODO addInstallation(this.formModel, this.cookieService, this.formToken)
    //MM-ANSWER you must use this even in this case
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
    // Very beautiful code right here :)
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
