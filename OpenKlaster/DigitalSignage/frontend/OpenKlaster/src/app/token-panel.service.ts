import { Injectable } from '@angular/core';
import {HttpClient, HttpParams, HttpResponse} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';
import {CookieService} from 'ngx-cookie-service';

@Injectable({
  providedIn: 'root'
})
export class TokenPanelService {

  constructor(public http: HttpClient) { }

  getTokens(cookies: CookieService): Observable<any>{
    let params = new HttpParams().set('username', cookies.get('username'));
    return this.http.get("http://localhost:8082/api/1/user",{params : params})
  }

  addToken(): Observable<any>{
    let params = new HttpParams().set('sessionToken', 'sessionToken');
    return this.http.post("http://localhost:8082/api/1/token",
      {'username' : 'username'},{params : params});
  }
}
