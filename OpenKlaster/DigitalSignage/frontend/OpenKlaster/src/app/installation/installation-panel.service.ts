import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {CookieService} from "ngx-cookie-service";
import {Installation} from "../model/Installation";
import {EndpointHolder} from "../model/EndpointHolder";

@Injectable({
  providedIn: 'root'
})
//TODO What 'Panel' means here?
//MM-ANSWER: Z translatora: panel - pulpit
export class InstallationPanelService {

  constructor(public http: HttpClient) { }

  getInstallations(cookieService: CookieService, token: string){
    let params = new HttpParams().set('apiToken', token).set('username', cookieService.get('username'));
    return this.http.get(EndpointHolder.installationsEndpoint,{params : params})
  }

  getInstallation(token: string, id: number){
    let params = new HttpParams().set('apiToken', token).set('installationId', 'installation:' + id);
    return this.http.get(EndpointHolder.installationEndpoint,{params : params});
  }

  addInstallation(installation: Installation, cookieService: CookieService, token: string){
    //TODO  The only place we are using apiTokens for now is generating/deleting them.
    // Our website must use sessionToken for authentication...!!!
    // : )
    let params = new HttpParams().set('apiToken', token);
    // Todo RG Why don't you just send an installation object that you already have?
    //MM-ANSWER: How would I do that?
    this.http.post(EndpointHolder.installationEndpoint, {
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
