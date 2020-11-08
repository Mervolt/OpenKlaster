import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {ChartService} from './chart.service';
import {DomSanitizer, SafeResourceUrl} from '@angular/platform-browser';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {CookieService} from 'ngx-cookie-service';
import {AppComponent} from '../../app.component';
import {InstallationService} from '../../service/installation/installation.service';
import {InstallationDto} from '../../model/InstallationDto';
import {Installation} from '../../model/Installation';
import {Chart} from '../../model/chart';
import {compareAsc, format, parse} from 'date-fns';

@Component({
  selector: 'app-charts',
  templateUrl: './charts.component.html',
  styleUrls: ['./charts.component.css'],
  encapsulation: ViewEncapsulation.None,
})
export class ChartsComponent implements OnInit {
  cookieService: CookieService;
  charts: Array<Chart> = new Array<Chart>();
  selectableDates: Array<Date>;
  firstFormGroup: FormGroup;
  secondFormGroup: FormGroup;
  installationId: string;
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
    this.installationService.getInstallations(this.cookieService).subscribe(response => {
      this.installationIDs = response.map(installation => installation['_id'])
      console.log(this.installationIDs)
    })
    console.log(this.installationIDs)
    this.chartService.getSelectableDates().subscribe(response => {
      this.selectableDates = response.map(date => new Date(date));
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
    this.secondFormGroup.get('secondCtrl').setValue('');
  }

  dateChoosen() {
    this.chartService.getChart().subscribe(response => {
      let dateKeys = Object.keys(response).sort((a, b) => compareAsc(this.parseDateTime(a), this.parseDateTime(b)));
      dateKeys.forEach(value =>
        this.charts.push(new Chart(this.parseDateTime(value),
          this.domSanitizer.bypassSecurityTrustResourceUrl(response[value]))));
    })

  }

  parseDateTime(date: string): Date {
    return parse(date, "yyyy-MM-dd-HHmmss", new Date());
  }

  formatTime(date: Date): string {
    return format(date, 'HH:mm:ss')
  }

  isSelectableDate(cellDate: Date): boolean {
    let returnValue = false;
    this.selectableDates.forEach(function(selectableDate) {
      if(cellDate.getDate() == selectableDate.getDate() && cellDate.getMonth() == selectableDate.getMonth() && cellDate.getFullYear() == selectableDate.getFullYear())
        returnValue = true;
    })
    return returnValue;
  }
}
