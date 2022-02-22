import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {CookieService} from 'ngx-cookie-service';
import {Installation} from '../../model/Installation';
import {EndpointHolder} from '../../model/EndpointHolder';
import {CookieHolder} from '../../model/CookieHolder';
import {InstallationPostDto} from '../../model/InstallationPostDto';
import {Observable} from 'rxjs';
import {InstallationSummary} from "../../model/InstallationSummary";

@Injectable({
  providedIn: 'root'
})
export class InstallationService {

  constructor(public http: HttpClient) {
  }

  getInstallations(cookieService: CookieService): Observable<any> {
    const params = new HttpParams().set('sessionToken', cookieService.get(CookieHolder.tokenKey)).set('username', cookieService.get('username'));
    return this.http.get(EndpointHolder.installationsEndpoint, {params});
  }

  getInstallation(cookieService: CookieService, id: number) {
    const params = new HttpParams().set('sessionToken', cookieService.get(CookieHolder.tokenKey)).set('installationId', 'installation:' + id);
    return this.http.get(EndpointHolder.installationEndpoint, {params});
  }

  getInstallationSummary(cookieService: CookieService, id: number) {
    const params = new HttpParams().set('sessionToken', cookieService.get(CookieHolder.tokenKey)).set('installationId', 'installation:' + id);
    return this.http.get(EndpointHolder.summaryEndpoint, {params});
  }

  getInstallationSummaryByString(id: string, apiToken: string): Observable<InstallationSummary> {
    const params = new HttpParams().set('apiToken', apiToken).set('installationId', id);
    return this.http.get<InstallationSummary>(EndpointHolder.summaryEndpoint, {params});
  }

  addInstallation(installation: Installation, cookieService: CookieService) {
    const params = new HttpParams().set('sessionToken', cookieService.get(CookieHolder.tokenKey));
    return this.http.post(EndpointHolder.installationEndpoint,
      InstallationPostDto.fromInstallationWithUser(installation, cookieService.get(CookieHolder.usernameKey)),
      {params}).toPromise();
  }

  editInstallation(installation: Installation, cookieService: CookieService) {
    const params = new HttpParams().set('sessionToken', cookieService.get(CookieHolder.tokenKey));
    return this.http.put(EndpointHolder.installationEndpoint,
      InstallationPostDto.fromInstallationWithUser(installation, cookieService.get(CookieHolder.usernameKey)),
      {params}).toPromise();
  }

}
