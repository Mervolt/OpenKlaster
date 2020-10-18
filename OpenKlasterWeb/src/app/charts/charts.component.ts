import { Component, OnInit } from '@angular/core';
import {ChartService} from './chart.service';
import {DomSanitizer, SafeResourceUrl} from '@angular/platform-browser';

@Component({
  selector: 'app-charts',
  templateUrl: './charts.component.html',
  styleUrls: ['./charts.component.css']
})
export class ChartsComponent implements OnInit {
  chart: SafeResourceUrl;
//TODO this whole package is placeholder for future feature
  constructor(private chartService: ChartService,
              private domSanitizer: DomSanitizer) { }

  ngOnInit(): void {
    this.chartService.getChart().subscribe(response => {
      this.chart = this.domSanitizer.bypassSecurityTrustResourceUrl(response)
    })
  }
}
