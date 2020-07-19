import { Injectable } from '@angular/core';
import {HttpClient, HttpParams, HttpResponse} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class InstallationPanelService {

  constructor(public http: HttpClient) { }

  getInstallations(){
    let params = new HttpParams();
    return this.http.get("localhost:8082/api/1/installations",{params : params});
  }

  addInstallation(){
    let params = new HttpParams().set('username', 'username');
    this.http.post("localhost:8082/api/1/installation", null, {params : params});
  }
}
