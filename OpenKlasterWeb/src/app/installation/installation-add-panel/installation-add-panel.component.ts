import {AfterViewChecked, AfterViewInit, ChangeDetectorRef, Component, ViewChild} from '@angular/core';
import {Installation} from "../../model/Installation";
import {InstallationService} from "../../service/installation/installation.service";
import {CookieService} from "ngx-cookie-service";
import {CredentialsFormComponent} from "../../credentials/credentials-form/credentials-form.component";
import {ManufacturerCredentialService} from "../../service/installation/manufacturer-credential.service";
import {Observable} from "rxjs";
import {QuestionBase} from "../../components/Question-boxes/question-base";
import {QuestionTextbox} from "../../components/Question-boxes/question-textbox";
import {DynamicFormComponent} from "../../components/Question-boxes/dynamic-form/dynamic-form.component";
import {$e} from "codelyzer/angular/styles/chars";
import {MatOptionSelectionChange} from "@angular/material/core";
import {ConfirmationDialogPopupComponent} from "../../components/confirmation-dialog-popup/confirmation-dialog-popup.component";
import {MatDialog} from "@angular/material/dialog";

@Component({
  selector: 'app-installation-generation-panel',
  templateUrl: './installation-add-panel.component.html',
  styleUrls: ['./installation-add-panel.component.css']
})
export class InstallationAddPanelComponent {
  sendRequestState = 'wait'
  manufacturersMap: Map<string, string[]> = new Map();

  questions$: QuestionBase<any>[];

  @ViewChild(CredentialsFormComponent) credentialsForm;
  @ViewChild(DynamicFormComponent) credentialsDynamicForm;
  formModel = new Installation();

  constructor(public installationService: InstallationService,
              public manufacturerCredentialService: ManufacturerCredentialService,
              private cookieService: CookieService, private dialog: MatDialog) {
    manufacturerCredentialService.getCredentials().toPromise().then(response => {
      for (let manufacturer in response) {
        this.manufacturersMap.set(manufacturer, response[manufacturer]);
      }
    });
    this.questions$ = [];
  }

  ngOnInit(): void {}

  onSubmit() {
    this.sendRequestState = 'waiting'
    let addPromise = this.installationService.addInstallation(this.formModel, this.cookieService);
    addPromise
      .then(() => {
        this.sendRequestState = 'success'
      })
      .catch(() => {
        this.sendRequestState = 'failure'
      })
  }
  handleCredentialsChange($event){
    this.formModel.inverter.credentials= $event
  }

  myCallbackFunction = (): void => {
    this.onSubmit();
  }

  changeCredentials(selectionChange: MatOptionSelectionChange) {
    if(!selectionChange.isUserInput){
      return
    }
    let credentials = this.manufacturersMap.get(selectionChange.source.value);
    this.questions$ = this.credentialsToQuestionBase(credentials);
  }

  private credentialsToQuestionBase(credentials: string[]): QuestionBase<string>[] {
    return credentials.map(credential => {
      return new QuestionTextbox(credential, credential);
    });
  }
  onWindInstallationType(event, group) {
    let dialog = this.dialog.open(ConfirmationDialogPopupComponent, {
      width: '500px'
    })
    dialog.componentInstance.popupContent = "There is no support for Wind installations yet.."
    group.value= "";
    this.formModel.installationType ="";
  }
}
