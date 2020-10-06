import {Component, Input, OnInit} from '@angular/core';
import {Installation} from "../../model/Installation";
import {Router} from "@angular/router";

@Component({
  selector: 'app-installation-list-item',
  templateUrl: './installation-list-item.component.html',
  styleUrls: ['./installation-list-item.component.css']
})
export class InstallationListItemComponent implements OnInit {
  @Input() installation: Installation

  constructor() {
  }

  ngOnInit(): void {
  }
}
