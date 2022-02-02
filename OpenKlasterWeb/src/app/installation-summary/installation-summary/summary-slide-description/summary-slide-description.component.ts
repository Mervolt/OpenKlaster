import {Component, Input, OnInit} from '@angular/core';
import {InstallationSummary} from "../../../model/InstallationSummary";

@Component({
  selector: 'app-summary-slide-description',
  templateUrl: './summary-slide-description.component.html',
  styleUrls: ['./summary-slide-description.component.css']
})
export class SummarySlideDescriptionComponent implements OnInit {

  @Input() summary: InstallationSummary;

  constructor() { }

  ngOnInit(): void {
  }

}
