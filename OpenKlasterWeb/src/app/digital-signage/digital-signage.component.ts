import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AppComponent} from "../app.component";
import {InstallationService} from "../service/installation/installation.service";
import {CookieService} from "ngx-cookie-service";
import {TokenService} from "../service/token/token.service";
import {TokenResponse} from "../token/token-panel/token-panel.component";

@Component({
  selector: 'app-digital-signage',
  templateUrl: './digital-signage.component.html',
  styleUrls: ['./digital-signage.component.css']
})
export class DigitalSignageComponent implements OnInit {
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
  introSelected: boolean = false;
  treesSelected: boolean = false;
  chartsSelected: boolean = false;


  constructor(private fb: FormBuilder,
              private appComp: AppComponent,
              private installationService: InstallationService,
              private tokenService: TokenService) {
    this.cookieService = appComp.cookieService;
  }

  ngOnInit(): void {
    this.loading = true;
    this.getFormOptions()

    this.installationForm = this.fb.group({
      installationId: [this.installationId, Validators.required],
      apiToken: [this.apiToken, Validators.required]
    });
  }

  getFormOptions(): void {
    this.installationService.getInstallations(this.cookieService).subscribe(response => {
      this.installationIDs = response.map(installation => installation['_id'])
      this.installationsLoaded = true;
      if(this.tokensLoaded === true)
        this.loading = false
    })

    this.tokenService.getTokens(this.appComp.cookieService).subscribe(response => {
      let tokensData: TokenResponse[] = <TokenResponse[]>(response["userTokens"])
      this.apiTokens = tokensData.map(token => token.data)
      this.tokensLoaded = true;
      if(this.installationsLoaded === true)
        this.loading = false;
    })
  }

  installationSelected() {
    if (this.installationForm.get('installationId').value != '' && this.installationForm.get('installationId').value != undefined) {
      this.installationId = this.installationForm.get('installationId').value;
      this.loading = true;
    }
  }

  downloadFile(): void {
    let res = "Wygenerowany plik :)";
    const blob = new Blob([res], {type: 'text/plain'});
    const url = window.URL.createObjectURL(blob);
    const downloadAnchor = document.createElement("a");
    downloadAnchor.href = url;
    downloadAnchor.download = "config.js";
    downloadAnchor.click();
  }

  viewsSelected() {
  }
}
