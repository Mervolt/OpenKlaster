import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-dashboard-properties',
  templateUrl: './dashboard-properties.component.html',
  styleUrls: ['./dashboard-properties.component.css']
})
export class DashboardPropertiesComponent implements OnInit {

  @Input() properties: Map<string, any>;
  @Input() singlePropertiesDisplay = false;
  constructor() { }

  getGridTemplateColumns(): string {
    if (this.singlePropertiesDisplay) {
      return '98%';
    } else {
      return '49% 49%';
    }
  }

  ngOnInit(): void {}

}
