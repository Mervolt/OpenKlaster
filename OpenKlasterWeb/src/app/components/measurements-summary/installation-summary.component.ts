import {Component, Input, OnInit} from '@angular/core';
import {InstallationSummary} from "../../model/InstallationSummary";

@Component({
  selector: 'app-measurements-summary',
  templateUrl: './installation-summary.component.html',
  styleUrls: ['./installation-summary.component.css']
})
export class InstallationSummaryComponent implements OnInit {

  @Input() installationSummary: InstallationSummary

  constructor() { }

  ngOnInit(): void {
  }

}
