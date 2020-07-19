import { Component, OnInit } from '@angular/core';
import { InstallationPanelService } from '../installation-panel.service';

import { Installation } from '../model/Installation';
import {Load} from '../model/Load';
import {Source} from '../model/Source';
import {Inverter} from '../model/Inverter';

@Component({
  selector: 'app-installation-panel',
  templateUrl: './installation-panel.component.html',
  styleUrls: ['./installation-panel.component.css']
})
export class InstallationPanelComponent implements OnInit {
  formModel = new Installation('', 0, 0, '', new Load('', ''),
    new Inverter('', '', '', ''), new Source(0, 0, 0, ''))
  installations: Array<Installation>;
  isFormHidden = true;


  constructor(public service: InstallationPanelService) {
    let observableInstallations = service.getInstallations();
    observableInstallations.subscribe(response =>{

    })
  }

  ngOnInit(): void {
  }

  onSubmit() {
    //this.service.addInstallation(this.formModel);
  }

  displayForm() {
    this.isFormHidden = false;
  }

  hideForm() {
    this.isFormHidden = true;
  }
}
