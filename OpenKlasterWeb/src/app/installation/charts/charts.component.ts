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
  selectableDates: Array<Date>;

  dateClass = (cellDate) => {
    let returnValue = false;
    this.selectableDates.forEach(function(selectableDate) {
      if(cellDate.getDate() == selectableDate.getDate() && cellDate.getMonth() == selectableDate.getMonth() && cellDate.getFullYear() == selectableDate.getFullYear())
        returnValue = true;
    })
    return returnValue ? 'example-custom-date-class' : '';

  }
  constructor(private chartService: ChartService,
              private domSanitizer: DomSanitizer) { }

  ngOnInit(): void {
    this.chartService.getSelectableDates().subscribe(response => {
      this.selectableDates = response.map(date => new Date(date));
    })

    this.chartService.getChart().subscribe(response => {
      this.chart = this.domSanitizer.bypassSecurityTrustResourceUrl(response)
    })
  }
}
