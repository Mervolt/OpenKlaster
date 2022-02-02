import {Component, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AppComponent} from '../app.component';
import {InstallationService} from '../service/installation/installation.service';
import {CookieService} from 'ngx-cookie-service';
import {TokenService} from '../service/token/token.service';
import {TokenResponse} from '../token/token-panel/token-panel.component';
import {MatHorizontalStepper} from '@angular/material/stepper';
import {ActivatedRoute, Route, Router} from '@angular/router';
import {EndpointHolder} from '../model/EndpointHolder';

@Component({
  selector: 'app-digital-signage',
  templateUrl: './digital-signage.component.html',
  styleUrls: ['./digital-signage.component.css']
})
export class DigitalSignageComponent implements OnInit {
  @ViewChild('stepper') private stepper: MatHorizontalStepper;
  installationsLoaded = false;
  tokensLoaded = false;
  loading = false;
  cookieService: CookieService;
  installationForm: FormGroup;
  installationIDs: Array<String>;
  installationId: String;
  apiTokens: Array<String>;
  apiToken: String;
  slidesForm: FormGroup;
  introSelected = true;
  treesSelected = true;
  chartsSelected = true;
  desiredTimePerSlide: number;
  couponText = EndpointHolder.digitalSignageEndpoint + '...';
  contenCopied = false;

  constructor(private fb: FormBuilder,
              private appComp: AppComponent,
              private installationService: InstallationService,
              private tokenService: TokenService,
              private route: ActivatedRoute,
              private router: Router) {
    this.cookieService = appComp.cookieService;
  }

  ngOnInit(): void {
    this.loading = true;
    this.getFormOptions();

    this.route.queryParams.subscribe(params => {
      if (params.installationId != undefined) {
        this.installationId = params.installationId[0];
      }
    });

    this.installationForm = this.fb.group({
      installationId: [this.installationId, Validators.required],
      apiToken: [this.apiToken, Validators.required]
    });

    this.desiredTimePerSlide = 10000;
  }

  getFormOptions(): void {
    this.installationService.getInstallations(this.cookieService).subscribe(response => {
      this.installationIDs = response.map(installation => installation.installationId);
      this.installationsLoaded = true;
      if (this.tokensLoaded === true) {
        this.loading = false;
      }
    });

    this.tokenService.getUserInfo(this.appComp.cookieService).subscribe(response => {
      const tokensData: TokenResponse[] = (response.tokens) as TokenResponse[];
      this.apiTokens = tokensData.map(token => token.data);
      this.tokensLoaded = true;
      if (this.installationsLoaded === true) {
        this.loading = false;
      }
    });
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
    const resJsonAny: any = {
      installationId: this.installationId,
      apiToken: this.apiToken,
      slides: {
        intro: this.introSelected,
        trees: this.treesSelected,
        power_chart: this.chartsSelected
      },
      slideChangeTimeout: this.desiredTimePerSlide
    };
    const resJson: JSON = resJsonAny as JSON;
    const res = 'const config = ' + JSON.stringify(resJson, null, 2);
    const blob = new Blob([res], {type: 'text/plain'});
    const url = window.URL.createObjectURL(blob);
    const downloadAnchor = document.createElement('a');
    downloadAnchor.href = url;
    downloadAnchor.download = 'config.js';
    downloadAnchor.click();
  }

  goToDigitalSignage() {
    this.router.navigate(['/installationSummary'],
      {queryParams: {id :this.installationId, apiToken: this.apiToken, interval: this.desiredTimePerSlide}}
      )
  }

  routeToDigitalSignageRepo() {
    window.open('https://github.com/Mervolt/OpenKlasterDigitalSignage', '_blank');
  }
}
