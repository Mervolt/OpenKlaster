import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {EndpointHolder} from '../../model/EndpointHolder';
import {CookieService} from "ngx-cookie-service";
import {CookieHolder} from "../../model/CookieHolder";

@Injectable({
  providedIn: 'root'
})
export class ChartService {

  constructor(private http: HttpClient) {}

  getChart(): Observable<any> {
    return this.http.get(EndpointHolder.chartEndpoint)
  }

  getSelectableDates(): Observable<any> {
    return this.http.get(EndpointHolder.selectableDatesEndpoint)
  }

  //TODO when proper backend api is ready use it instead of getChart()
  getChartForInstallation(installationId: string, cookieService: CookieService): Observable<any> {
    let params = new HttpParams()
      .set('username', cookieService.get(CookieHolder.usernameKey))
      .set('sessionToken', cookieService.get(CookieHolder.tokenKey))
      .set('installationId', installationId);
    return this.http.get(EndpointHolder.chartEndpoint, {params: params});
  }
}
