import {Injectable} from '@angular/core';
import {CookieService} from 'ngx-cookie-service';
import {Observable} from 'rxjs';
import {HttpClient, HttpParams} from '@angular/common/http';
import {CookieHolder} from '../../model/CookieHolder';
import {EndpointHolder} from '../../model/EndpointHolder';
import {UserDto} from '../../model/UserDto';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  constructor(public http: HttpClient) {
  }

  getUsers(cookieService: CookieService): Observable<any> {
    const params = new HttpParams().set('sessionToken', cookieService.get(CookieHolder.tokenKey));
    return this.http.get(EndpointHolder.adminUserEndpoint, {params});
  }

  putUser(user: UserDto, cookieService: CookieService): Observable<any> {
    const params = new HttpParams().set('sessionToken', cookieService.get(CookieHolder.tokenKey));
    return this.http.put(EndpointHolder.adminUserEndpoint, user, {params});
  }
}
