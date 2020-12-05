import {Component, OnInit} from '@angular/core';
import {Installation} from "../../model/Installation";
import {InstallationAddPanelComponent} from "../installation-add-panel/installation-add-panel.component";
import {InstallationService} from "../../service/installation/installation.service";
import {ManufacturerCredentialService} from "../../service/installation/manufacturer-credential.service";
import {CookieService} from "ngx-cookie-service";
import {MatDialog} from "@angular/material/dialog";
import {ActivatedRoute, Router} from "@angular/router";
import {InstallationDto} from "../../model/InstallationDto";
import {QuestionBase} from '../../components/Question-boxes/question-base';
import {QuestionTextbox} from '../../components/Question-boxes/question-textbox';

@Component({
  selector: 'app-edit-installation',
  templateUrl: '../installation-add-panel/installation-add-panel.component.html',
  styleUrls: ['../installation-add-panel/installation-add-panel.component.css']
})
export class EditInstallationComponent extends InstallationAddPanelComponent implements OnInit {
  installationId: number
  oldModel: Installation
  isEditing = true;
  questions$: QuestionBase<any>[];
  questionsValues: Map<string, string> = new Map<string, string>();

  constructor(public installationService: InstallationService,
              public manufacturerCredentialService: ManufacturerCredentialService,
              public cookieService: CookieService,
              public dialog: MatDialog,
              private route: ActivatedRoute, public router: Router) {
    super(installationService, manufacturerCredentialService, cookieService, dialog, router)
    this.installationId = Number(route.snapshot.paramMap.get('id'));
  }

  ngOnInit(): void {
    this.getInstallation(this.installationId).then(() => {})
  }

  async getInstallation(id: number) {
    let observableInstallation = this.installationService.getInstallation(this.cookieService, id);
    observableInstallation.subscribe(response => {
      this.formModel = InstallationDto.fromDto(response)
      this.oldModel = InstallationDto.fromDto(response)

      let credentials = this.manufacturersMap.get(this.formModel.inverter.manufacturer);
      this.questions$ = this.credentialsToQuestionBase(credentials);
      this.questions$.map(question => {
        question.value = this.formModel.inverter.credentials[question.key]
      })
    })
  }

  onSubmit() {
    this.sendRequestState = 'waiting'
    this.formModel.inverter.credentials = this.oldModel.inverter.credentials;
    let editPromise = this.installationService.editInstallation(this.formModel, this.cookieService);
    editPromise
      .then(() => {
        this.sendRequestState = 'success'
        this.router.navigate(['installations', this.installationId]).then()
      })
      .catch(() => {
        this.sendRequestState = 'failure'
      })
  }

}
