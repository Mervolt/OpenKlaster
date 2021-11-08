import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {EndpointHolder} from '../model/EndpointHolder';
import {CookieService} from 'ngx-cookie-service';
import {CookieHolder} from '../model/CookieHolder';

@Injectable({
  providedIn: 'root'
})
export class ChartService {

  constructor(private http: HttpClient) {}

  getSelectableDates(cookieService: CookieService, installationId: string): Observable<any> {
    const params = new HttpParams()
      .set('sessionToken', cookieService.get(CookieHolder.tokenKey))
      .set('installationId', installationId);
    return this.http.get(EndpointHolder.selectableDatesEndpoint, {params});
  }

  getChartsForInstallation(cookieService: CookieService, installationId: string, date: string): Observable<any> {
    const params = new HttpParams()
      .set('sessionToken', cookieService.get(CookieHolder.tokenKey))
      .set('installationId', installationId)
      .set('date', date);
    return this.http.get(EndpointHolder.chartEndpoint, {params});
  }
}
