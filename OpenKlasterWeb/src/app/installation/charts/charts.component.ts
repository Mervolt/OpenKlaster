import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {ChartService} from './chart.service';
import {DomSanitizer} from '@angular/platform-browser';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {CookieService} from 'ngx-cookie-service';
import {AppComponent} from '../../app.component';
import {InstallationService} from '../../service/installation/installation.service';
import {Chart} from '../../model/chart';
import {compareAsc, format, parse} from 'date-fns';

@Component({
  selector: 'app-charts',
  templateUrl: './charts.component.html',
  styleUrls: ['./charts.component.css'],
  encapsulation: ViewEncapsulation.None,
})
export class ChartsComponent implements OnInit {
  loading: boolean = false;
  cookieService: CookieService;
  charts: Array<Chart> = new Array<Chart>();
  selectableDates: Array<Date>;
  firstFormGroup: FormGroup;
  secondFormGroup: FormGroup;
  installationId: string;
  selectedDate: string;
  installationIDs: Array<String>

  dateClass = (cellDate: Date) => this.isSelectableDate(cellDate) ? 'example-custom-date-class' : '';
  dateFilter = (cellDate: Date) => this.isSelectableDate(cellDate);

  constructor(private chartService: ChartService,
              private domSanitizer: DomSanitizer,
              private fb: FormBuilder,
              private appComp: AppComponent,
              private installationService: InstallationService) {
    this.cookieService = appComp.cookieService;
  }

  ngOnInit(): void {
    this.loading = true;
    this.installationService.getInstallations(this.cookieService).subscribe(response => {
      this.installationIDs = response.map(installation => installation['_id'])
      this.loading = false;
    })


    this.firstFormGroup = this.fb.group({
      firstCtrl: ['', Validators.required]
    });
    this.secondFormGroup = this.fb.group({
      secondCtrl: ['', Validators.required]
    });
  }

  print() {
    this.installationId = this.firstFormGroup.get('firstCtrl').value;
    this.loading = true;
    this.chartService.getSelectableDates(this.cookieService, this.installationId).subscribe(response => {
      this.selectableDates = response.map(date => new Date(date));
      this.loading = false;
    })
    this.secondFormGroup.get('secondCtrl').setValue('');
  }

  dateChoosen() {
    this.loading = true;
    this.selectedDate = this.formatDate(this.secondFormGroup.get('secondCtrl').value);
    let chartsArray = [];
    this.chartService.getChartsForInstallation(this.cookieService, this.installationId, this.selectedDate)
      .subscribe(response => {
        let dateKeys = Object.keys(response).sort((a, b) => compareAsc(this.parseDateTime(a), this.parseDateTime(b)));
        dateKeys.forEach(value =>
          chartsArray.push(new Chart(this.parseDateTime(value),
            this.domSanitizer.bypassSecurityTrustResourceUrl(response[value]))));
        this.charts = chartsArray;
        this.loading = false;
      })
  }

  parseDateTime(date: string): Date {
    return parse(date, "yyyy-MM-dd-HHmmss", new Date());
  }

  formatTime(date: Date): string {
    return format(date, 'HH:mm:ss')
  }

  formatDate(date: Date): string {
    return format(date, 'yyyy-MM-dd')
  }

  isSelectableDate(cellDate: Date): boolean {
    let returnValue = false;
    this.selectableDates.forEach(function (selectableDate) {
      if (cellDate.getDate() == selectableDate.getDate() && cellDate.getMonth() == selectableDate.getMonth() && cellDate.getFullYear() == selectableDate.getFullYear())
        returnValue = true;
    })
    return returnValue;
  }
}
