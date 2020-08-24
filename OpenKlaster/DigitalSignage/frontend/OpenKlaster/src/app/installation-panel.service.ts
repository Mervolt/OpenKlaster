import { Injectable } from '@angular/core';
import {HttpClient, HttpParams, HttpResponse} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';
import {CookieService} from "ngx-cookie-service";
import {Installation} from "./model/Installation";

@Injectable({
  providedIn: 'root'
})
export class InstallationPanelService {

  constructor(public http: HttpClient) { }

  getInstallations(token: string){
    let result = [];
    for(let iterator = 0; iterator < 20; iterator++){
      let params = new HttpParams().set('apiToken', token).set('installationId', 'installation:' + iterator);
      result.push(this.http.get("http://localhost:8082/api/1/installations",{params : params}))
    }
    return result;
  }

  addInstallation(installation: Installation, cookieService: CookieService){
    let params = new HttpParams().set('apiToken', installation.apiToken);
    this.http.post("http://localhost:8082/api/1/installations", {
      'username': cookieService.get('username'),
      'installationType': installation.installationType,
      'longitude': installation.longitude,
      'latitude': installation.latitude,
      'description': installation.description,
      'load': {
        'name': installation.load.name,
        'description': installation.load.description
      },
      'inverter': {
        'description': installation.inverter.description,
        'manufacturer': installation.inverter.manufacturer,
        'credentials': installation.inverter.credentials,
        'modelType': installation.inverter.modelType
      },
      'source': {
        'azimuth': installation.source.azimuth,
        'tilt': installation.source.tilt,
        'capacity': installation.source.capacity,
        'description': installation.source.description
      }
    }, {params : params}).subscribe();
  }
}
