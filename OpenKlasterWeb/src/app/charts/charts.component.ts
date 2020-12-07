import {Component, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
import {ChartService} from './chart.service';
import {DomSanitizer} from '@angular/platform-browser';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {CookieService} from 'ngx-cookie-service';
import {AppComponent} from '../app.component';
import {InstallationService} from '../service/installation/installation.service';
import {Chart} from '../model/chart';
import {compareAsc, format, parse} from 'date-fns';
import {ActivatedRoute} from '@angular/router';
import {MatHorizontalStepper} from '@angular/material/stepper';

@Component({
  selector: 'app-charts',
  templateUrl: './charts.component.html',
  styleUrls: ['./charts.component.css'],
  encapsulation: ViewEncapsulation.None,
})
export class ChartsComponent implements OnInit {
  @ViewChild('stepper') private stepper: MatHorizontalStepper;
  getChartsError: boolean = false;
  getSelectableDatesError: boolean = false;

  cookieService: CookieService;
  charts: Array<Chart> = new Array<Chart>();
  selectableDates: Array<Date>;
  installationForm: FormGroup;
  datePickerForm: FormGroup;
  installationId: string = '';
  selectedDate: string;
  installationIDs: Array<String>

  dateClass = (cellDate: Date) => this.isSelectableDate(cellDate) ? 'example-custom-date-class' : '';
  dateFilter = (cellDate: Date) => this.isSelectableDate(cellDate);

  constructor(private chartService: ChartService,
              private domSanitizer: DomSanitizer,
              private fb: FormBuilder,
              private appComp: AppComponent,
              private installationService: InstallationService,
              private route: ActivatedRoute) {
    this.cookieService = appComp.cookieService;
    this.route.queryParams.subscribe(params => {
      if (params['installationId'] != undefined) {
        this.installationId = params['installationId'];
      }
    });
  }

  ngOnInit(): void {
    this.installationService.getInstallations(this.cookieService).subscribe(response => {
      this.installationIDs = response.map(installation => installation['_id'])
    })

    this.installationForm = this.fb.group({
      installationId: [this.installationId, Validators.required]
    });
    this.datePickerForm = this.fb.group({
      date: ['', Validators.required]
    });

  }

  installationSelectedOption() {
    document.getElementById('ChooseInstallationNext').click()
  }

  dateSelectedOption() {
    document.getElementById('ChooseDateNext').click()
  }

  installationSelected() {
    if (this.installationForm.get('installationId').value != '' && this.installationForm.get('installationId').value != undefined) {
      this.getSelectableDatesError = false;
      this.installationId = this.installationForm.get('installationId').value;
      this.chartService.getSelectableDates(this.cookieService, this.installationId).subscribe(response => {
          this.selectableDates = response.map(date => new Date(date));
        }, () => {
          this.getSelectableDatesError = true;
        }
      )
      this.datePickerForm.get('date').setValue('');
    }
  }

  dateSelected() {
    if (this.datePickerForm.get('date').value != '' && this.datePickerForm.get('date').value != null) {
      this.getChartsError = false;
      this.selectedDate = this.formatDate(this.datePickerForm.get('date').value);
      let chartsArray = [];
      this.chartService.getChartsForInstallation(this.cookieService, this.installationId, this.selectedDate)
        .subscribe(response => {
            let dateKeys = Object.keys(response).sort((a, b) => compareAsc(this.parseDateTime(a), this.parseDateTime(b)));
            dateKeys.forEach(value =>
              chartsArray.push(new Chart(this.parseDateTime(value),
                this.domSanitizer.bypassSecurityTrustResourceUrl(response[value]))));
            this.charts = chartsArray;
          },
          () => {
            this.getChartsError = true
          }
        )
    }
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
