import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-dashboard-properties',
  templateUrl: './dashboard-properties.component.html',
  styleUrls: ['./dashboard-properties.component.css']
})
export class DashboardPropertiesComponent implements OnInit {

  @Input() properties: Map<string, any>

  constructor() { }

  ngOnInit(): void {}

}
