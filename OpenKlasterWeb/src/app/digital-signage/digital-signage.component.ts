import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AppComponent} from "../app.component";
import {InstallationService} from "../service/installation/installation.service";
import {CookieService} from "ngx-cookie-service";

@Component({
  selector: 'app-digital-signage',
  templateUrl: './digital-signage.component.html',
  styleUrls: ['./digital-signage.component.css']
})
export class DigitalSignageComponent implements OnInit {
  loading: boolean = false;
  cookieService: CookieService;
  installationForm: FormGroup;
  installationIDs: Array<String>;
  installationId: String;

  constructor(private fb: FormBuilder,
              private appComp: AppComponent,
              private installationService: InstallationService) {
    this.cookieService = appComp.cookieService;
  }

  ngOnInit(): void {
    this.loading = true;
    this.installationService.getInstallations(this.cookieService).subscribe(response => {
      this.installationIDs = response.map(installation => installation['_id'])
      this.loading = false;
    })

    this.installationForm = this.fb.group({
      installationId: [this.installationId, Validators.required]
    });
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

}
