import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {CookieService} from "ngx-cookie-service";
import {Installation} from "../../model/Installation";
import {EndpointHolder} from "../../model/EndpointHolder";
import {CookieHolder} from "../../model/CookieHolder";
import {InstallationPostDto} from "../../model/InstallationPostDto";
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class InstallationService {

  constructor(public http: HttpClient) {
  }

  getInstallations(cookieService: CookieService): Observable<any> {
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
    return this.http.post(EndpointHolder.installationEndpoint,
      InstallationPostDto.fromInstallationWithUser(installation, cookieService.get(CookieHolder.usernameKey)),
      {params: params}).toPromise();
  }

  editInstallation(installation: Installation, cookieService: CookieService) {
    let params = new HttpParams().set('sessionToken', cookieService.get(CookieHolder.tokenKey));
    return this.http.put(EndpointHolder.installationEndpoint,
      InstallationPostDto.fromInstallationWithUser(installation, cookieService.get(CookieHolder.usernameKey)),
      {params:params}).toPromise()
  }

}
