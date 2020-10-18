import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {EndpointHolder} from '../model/EndpointHolder';

@Injectable({
  providedIn: 'root'
})
export class ChartService {

  constructor(private http: HttpClient) {}

  getChart(): Observable<any> {
    return this.http.get(EndpointHolder.chartEndpoint)
  }
}
