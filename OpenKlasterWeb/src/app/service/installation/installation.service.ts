import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {CookieService} from "ngx-cookie-service";
import {Installation} from "../../model/Installation";
import {EndpointHolder} from "../../model/EndpointHolder";
import {CookieHolder} from "../../model/CookieHolder";

@Injectable({
  providedIn: 'root'
})
export class InstallationService {

  constructor(public http: HttpClient) {
  }

  getInstallations(cookieService: CookieService) {
    let params = new HttpParams().set('sessionToken', cookieService.get(CookieHolder.tokenKey)).set('username', cookieService.get('username'));
    return this.http.get(EndpointHolder.installationsEndpoint, {params: params})
  }

  getInstallation(cookieService: CookieService, id: number) {
    let params = new HttpParams().set('sessionToken', cookieService.get(CookieHolder.tokenKey)).set('installationId', 'installation:' + id);
    return this.http.get(EndpointHolder.installationEndpoint, {params: params});
  }

  getInstallationSummary(cookieService: CookieService, id: number) {
    let params = new HttpParams().set('sessionToken', cookieService.get(CookieHolder.tokenKey)).set('installationId', 'installation:' + id);
    return this.http.get(EndpointHolder.summaryEndpoint, {params:params});
  }

  addInstallation(installation: Installation, cookieService: CookieService) {
    let params = new HttpParams().set('sessionToken', cookieService.get(CookieHolder.tokenKey));
    return this.http.post(EndpointHolder.installationEndpoint, {
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
    }, {params: params}).toPromise();
  }

}
