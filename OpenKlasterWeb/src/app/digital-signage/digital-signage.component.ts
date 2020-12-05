import {Component, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AppComponent} from "../app.component";
import {InstallationService} from "../service/installation/installation.service";
import {CookieService} from "ngx-cookie-service";
import {TokenService} from "../service/token/token.service";
import {TokenResponse} from "../token/token-panel/token-panel.component";
import {MatHorizontalStepper} from "@angular/material/stepper";
import {ActivatedRoute, Route} from "@angular/router";
import {EndpointHolder} from '../model/EndpointHolder';
import {ClipboardService} from 'ngx-clipboard';

@Component({
  selector: 'app-digital-signage',
  templateUrl: './digital-signage.component.html',
  styleUrls: ['./digital-signage.component.css']
})
export class DigitalSignageComponent implements OnInit {
  @ViewChild('stepper') private stepper: MatHorizontalStepper;
  installationsLoaded: boolean = false;
  tokensLoaded: boolean = false;
  loading: boolean = false;
  cookieService: CookieService;
  installationForm: FormGroup;
  installationIDs: Array<String>;
  installationId: String;
  apiTokens: Array<String>;
  apiToken: String;
  slidesForm: FormGroup;
  introSelected: boolean = true;
  treesSelected: boolean = true;
  chartsSelected: boolean = true;
  desiredTimePerSlide: number;
  couponText = EndpointHolder.digitalSignageEndpoint + "...";
  contenCopied = false;

  constructor(private fb: FormBuilder,
              private appComp: AppComponent,
              private installationService: InstallationService,
              private tokenService: TokenService,
              private route: ActivatedRoute) {
    this.cookieService = appComp.cookieService;
  }

  ngOnInit(): void {
    this.loading = true;
    this.getFormOptions()

    this.route.queryParams.subscribe(params => {
      if (params['installationId'] != undefined) {
        this.installationId = params['installationId'][0];
      }
    });

    this.installationForm = this.fb.group({
      installationId: [this.installationId, Validators.required],
      apiToken: [this.apiToken, Validators.required]
    });

    this.desiredTimePerSlide = 10000
  }

  contentCopied(e) {
    this.contenCopied = e.isSuccess;
  }

  ngAfterViewInit(): void {
    if (this.installationId != '') {
      this.installationForm.get('installationId').setValue(this.installationId);
    }
  }

  getFormOptions(): void {
    this.installationService.getInstallations(this.cookieService).subscribe(response => {
      this.installationIDs = response.map(installation => installation['_id'])
      this.installationsLoaded = true;
      if (this.tokensLoaded === true)
        this.loading = false
    })

    this.tokenService.getTokens(this.appComp.cookieService).subscribe(response => {
      let tokensData: TokenResponse[] = <TokenResponse[]>(response["userTokens"])
      this.apiTokens = tokensData.map(token => token.data)
      this.tokensLoaded = true;
      if (this.installationsLoaded === true)
        this.loading = false;
    })
  }

  installationSelected() {
    if (this.installationForm.get('installationId').value != '' && this.installationForm.get('installationId').value != undefined) {
      this.installationId = this.installationForm.get('installationId').value;
    }

    if (this.installationForm.get('apiToken').value != '' && this.installationForm.get('apiToken').value != undefined) {
      this.apiToken = this.installationForm.get('apiToken').value;
    }
  }

  downloadFile(): void {
    let resJsonAny: any = {
      "installationId": this.installationId,
      "apiToken": this.apiToken,
      "slides": {
        "intro": this.introSelected,
        "trees": this.treesSelected,
        "power_chart": this.chartsSelected
      },
      "slideChangeTimeout": this.desiredTimePerSlide
    };
    let resJson: JSON = <JSON>resJsonAny;
    let res = "const config = " + JSON.stringify(resJson, null, 2);
    const blob = new Blob([res], {type: 'text/plain'});
    const url = window.URL.createObjectURL(blob);
    const downloadAnchor = document.createElement("a");
    downloadAnchor.href = url;
    downloadAnchor.download = "config.js";
    downloadAnchor.click();
  }

  goToDigitalSignage() {
    window.location.href=this.getDigitalSignageLink();
  }

  getDigitalSignageLink(): string {
    return EndpointHolder.digitalSignageEndpoint + "?" +
      "installationId=" + this.installationId + "&" +
      "apiToken=" + this.apiToken + "&" +
      "intro=" + this.introSelected + "&" +
      "trees=" + this.treesSelected + "&" +
      "power_chart=" + this.chartsSelected + "&" +
      "slideChangeTimeout=" + this.desiredTimePerSlide;
  }
}
