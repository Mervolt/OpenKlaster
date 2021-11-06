import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-installation-details-box',
  templateUrl: './installation-details-box.component.html',
  styleUrls: ['./installation-details-box.component.css']
})
export class InstallationDetailsBoxComponent implements OnInit {
  @Input() item: string;
  @Input() itemDescription: string;

  constructor() {
  }

  ngOnInit(): void {
  }

}
