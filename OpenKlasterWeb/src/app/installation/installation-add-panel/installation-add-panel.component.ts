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
import {Router} from "@angular/router";
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-installation-generation-panel',
  templateUrl: './installation-add-panel.component.html',
  styleUrls: ['./installation-add-panel.component.css']
})
export class InstallationAddPanelComponent {
  sendRequestState = 'wait'
  manufacturersMap: Map<string, string[]> = new Map();
  manufacturers: string[]
  questions$: QuestionBase<any>[];

  @ViewChild(CredentialsFormComponent) credentialsForm;
  @ViewChild(DynamicFormComponent) credentialsDynamicForm;
  formModel: Installation;

  constructor(public installationService: InstallationService,
              public manufacturerCredentialService: ManufacturerCredentialService,
              public cookieService: CookieService, public dialog: MatDialog,
              public router: Router,
              public translateService: TranslateService) {
    this.formModel = new Installation();
    this.questions$ = [];
  }

  ngOnInit(): void {
    this.manufacturerCredentialService.getCredentials().toPromise().then(response => {
      for (let manufacturer in response) {
        let translated = response[manufacturer].map(entry => this.translateService.instant(entry))
        this.manufacturersMap.set(manufacturer, translated);
        this.manufacturers = Array.from(this.manufacturersMap.keys());
        this.formModel.installationType = 'Solar';
      }
    });
  }

  onSubmit() {
    this.sendRequestState = 'waiting'
    let addPromise = this.installationService.addInstallation(this.formModel, this.cookieService);
    addPromise
      .then(response  => {
        let id = response['installationId']
        this.sendRequestState = 'success'
        this.router.navigate(['installations', id]).then()
      })
      .catch(() => {
        this.sendRequestState = 'failure'
      })
  }
  handleCredentialsChange($event){
    this.formModel.inverter.credentials= $event
  }

  myCallbackFunction = (): void => {
    //TODO get rid of this - there was POST duplication due to calling it on button and form
  }

  changeCredentials(selectionChange: MatOptionSelectionChange) {
    if (!selectionChange.isUserInput) {
      return
    }
    let credentials = this.manufacturersMap.get(selectionChange.source.value);
    this.questions$ = this.credentialsToQuestionBase(credentials);
  }

  protected credentialsToQuestionBase(credentials: string[]): QuestionBase<string>[] {
    return credentials.map(credential => {
      return new QuestionTextbox(credential, credential);
    });
  }

  onWindInstallationType(event, group) {
    let dialog = this.dialog.open(ConfirmationDialogPopupComponent, {
      width: '500px'
    })
    dialog.componentInstance.popupContent = this.translateService.instant("WindInstallation");
    group.value = "";
    this.formModel.installationType = "";
  }
}
