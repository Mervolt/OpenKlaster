import { Component, OnInit } from '@angular/core';
import { InstallationPanelService } from '../installation-panel.service';

@Component({
  selector: 'app-installation-panel',
  templateUrl: './installation-panel.component.html',
  styleUrls: ['./installation-panel.component.css']
})
export class InstallationPanelComponent implements OnInit {
  installations;

  constructor(service: InstallationPanelService) {
    this.installations = service.getInstallations();
    this.installations.subscribe(response =>{
      return response.body.tokens;
    })
  }

  ngOnInit(): void {
  }

}
