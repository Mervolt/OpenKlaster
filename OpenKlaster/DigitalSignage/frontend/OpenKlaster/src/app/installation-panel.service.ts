import { Injectable } from '@angular/core';
import {HttpClient, HttpParams, HttpResponse} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';
import {CookieService} from "ngx-cookie-service";
import {Installation} from "./model/Installation";
import {TokenPanelService} from "./token-panel.service";
import {AppComponent} from "./app.component";

@Injectable({
  providedIn: 'root'
})
//TODO What 'Panel' means here?
export class InstallationPanelService {

  constructor(public http: HttpClient) { }

  getInstallations(cookieService: CookieService, token: string){
    let params = new HttpParams().set('apiToken', token).set('username', cookieService.get('username'));
    return this.http.get("http://localhost:8082/api/1/installations/all",{params : params})
  }

  getInstallation(token: string, id: number){
    let params = new HttpParams().set('apiToken', token).set('installationId', 'installation:' + id);
    return this.http.get("http://localhost:8082/api/1/installations",{params : params});
  }

  addInstallation(installation: Installation, cookieService: CookieService, token: string){
    //TODO  The only place we are using apiTokens for now is generating/deleting them.
    // Our website must use sessionToken for authentication...!!!
    let params = new HttpParams().set('apiToken', token);
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
