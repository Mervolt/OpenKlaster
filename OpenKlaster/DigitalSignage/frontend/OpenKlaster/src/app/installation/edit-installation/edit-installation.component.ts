import {Component, OnInit} from '@angular/core';
import {Installation} from "../../model/Installation";

@Component({
  selector: 'app-edit-installation',
  templateUrl: './edit-installation.component.html',
  styleUrls: ['./edit-installation.component.css']
})
export class EditInstallationComponent implements OnInit {
  installationId: string
  installation: Installation

  constructor() {
  }

  ngOnInit(): void {
  }

  onSubmit() {

  }
}
