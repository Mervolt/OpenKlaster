import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {ChartService} from './chart.service';
import {DomSanitizer, SafeResourceUrl} from '@angular/platform-browser';

@Component({
  selector: 'app-charts',
  templateUrl: './charts.component.html',
  styleUrls: ['./charts.component.css'],
  encapsulation: ViewEncapsulation.None,
})
export class ChartsComponent implements OnInit {
  chart: SafeResourceUrl;
  dateClass: any = (cellDate) => {
    // Only highligh dates inside the month view.
    const date = cellDate.getDate();

    // Highlight the 1st and 20th day of each month.
    return (date === 1 || date === 20) ? 'example-custom-date-class' : '';

  }
  constructor(private chartService: ChartService,
              private domSanitizer: DomSanitizer) { }

  ngOnInit(): void {
    this.chartService.getChart().subscribe(response => {
      this.chart = this.domSanitizer.bypassSecurityTrustResourceUrl(response)
    })
  }
}
