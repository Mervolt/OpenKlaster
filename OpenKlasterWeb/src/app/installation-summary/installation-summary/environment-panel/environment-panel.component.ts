import {Component, Input, OnInit} from '@angular/core';
import {EnvironmentalBenefits} from "../../../model/EnvironmentalBenefits";

@Component({
  selector: 'app-environment-panel',
  templateUrl: './environment-panel.component.html',
  styleUrls: ['./environment-panel.component.css']
})
export class EnvironmentPanelComponent implements OnInit {

  @Input() environmentalBenefits: EnvironmentalBenefits;

  constructor() { }

  ngOnInit(): void {
  }

}
