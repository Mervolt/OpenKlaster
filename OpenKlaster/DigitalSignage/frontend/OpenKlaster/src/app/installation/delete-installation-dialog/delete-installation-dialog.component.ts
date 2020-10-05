import {Component, Input, OnInit} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {CookieHolder} from "../../model/CookieHolder";
import {EndpointHolder} from "../../model/EndpointHolder";
import {CookieService} from "ngx-cookie-service";

@Component({
  selector: 'app-delete-installation-dialog',
  templateUrl: './delete-installation-dialog.component.html',
  styleUrls: ['./delete-installation-dialog.component.css']
})
export class DeleteInstallationDialogComponent implements OnInit {
  id: number

  constructor(public cookieService: CookieService, public http: HttpClient) {
  }

  ngOnInit(): void {
  }

  deleteInstallation(id: number) {
    let params = new HttpParams().set('sessionToken', this.cookieService.get(CookieHolder.tokenKey)).set('installationId', 'installation:' + id);
    this.http.delete(EndpointHolder.installationEndpoint, {params: params}).subscribe();
  }

}
