import { Injectable } from '@angular/core';
import {HttpClient, HttpParams, HttpResponse} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class TokenPanelService {

  constructor(public http: HttpClient) { }

  getTokens(): Observable<any>{
    let params = new HttpParams().set('username', 'username');
    return this.http.get("localhost:8082/api/1/user",{params : params})
  }

  addToken(): Observable<any>{
    let params = new HttpParams().set('username', 'username');
    return this.http.post("localhost:8082", null,{params : params});
  }
}
