import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-digital-signage',
  templateUrl: './digital-signage.component.html',
  styleUrls: ['./digital-signage.component.css']
})
export class DigitalSignageComponent implements OnInit {
  loading: boolean = false;
  installationForm: FormGroup;
  installationIDs: Array<String>;
  installationId: String;

  constructor(private fb: FormBuilder) {
  }

  ngOnInit(): void {
    this.installationForm = this.fb.group({
      installationId: [this.installationId, Validators.required]
    });
  }


  installationSelected() {

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
