import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {EndpointHolder} from "../../model/EndpointHolder";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ManufacturerCredentialService {

  constructor(public http: HttpClient) {
  }

  getCredentials(): Observable<any> {
    return this.http.get(EndpointHolder.credentialsEndpoint)
  }
}
