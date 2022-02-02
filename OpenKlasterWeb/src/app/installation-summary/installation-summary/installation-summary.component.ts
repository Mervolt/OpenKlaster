import {Component} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {InstallationService} from "../../service/installation/installation.service";
import {InstallationSummary, PowerChartData} from "../../model/InstallationSummary";
import {Observable, timer} from "rxjs";
import {switchMap, tap} from "rxjs/operators";
import {AxisModel} from "@syncfusion/ej2-angular-charts";

@Component({
  selector: 'app-installation-summary',
  templateUrl: './installation-summary.component.html',
  styleUrls: ['./installation-summary.component.css']
})
export class InstallationSummaryComponent {

  installationId: string;
  apiToken: string;
  installationSummary: InstallationSummary;
  intervalTime: number;
  chartData: Array<PowerChartData>;
  public tooltip = {enable: true}
  public marker = {visible: true}
  public primaryXAxis: AxisModel = {
    valueType: 'DateTime',
    titleStyle: {
      color: '#FFFFFF'
    },
    labelStyle: {
      color: '#FFFFFF'
    },
    labelFormat: 'MM-dd HH:mm'
  };
  public primaryYAxis: AxisModel = {
    title: '[kW]',
    titleStyle: {
      color: '#FFFFFF'
    },
    labelStyle: {
      color: '#FFFFFF'
    }
  };

  constructor(
    private route: ActivatedRoute,
    private dialog: MatDialog,
    private router: Router,
    private installationService: InstallationService,
  ) {
    this.installationId = route.snapshot.queryParamMap.get('id');
    this.apiToken = route.snapshot.queryParamMap.get('apiToken');

    if (route.snapshot.queryParamMap.has('interval')) {
      this.intervalTime = Number(route.snapshot.queryParamMap.get('interval'));
    } else {
      this.intervalTime = 30 * 1000;
    }

    timer(0, this.intervalTime).pipe(
      switchMap(() => this.getInstallationSummary(this.installationId, this.apiToken)),
      tap(summary => {
        this.installationSummary = summary
        this.chartData = InstallationSummary.toChartData(this.installationSummary.power);
      })
    ).subscribe();
  }

  getInstallationSummary(installationId: string, apiToken: string): Observable<InstallationSummary> {
    return this.installationService.getInstallationSummaryByString(installationId, apiToken);
  }

  shouldDisplayPowerChart(): boolean {
    return this.chartData.length > 0;
  }

  shouldDisplayEnergy(): boolean {
    return this.installationSummary.environmentalBenefits.co2Reduced != 0 ||
      this.installationSummary.environmentalBenefits.treesSaved != 0;
  }

}
