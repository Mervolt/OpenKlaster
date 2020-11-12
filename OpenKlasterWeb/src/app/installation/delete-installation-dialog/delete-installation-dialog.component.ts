import {Component, Input, OnInit} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {CookieHolder} from "../../model/CookieHolder";
import {EndpointHolder} from "../../model/EndpointHolder";
import {CookieService} from "ngx-cookie-service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-delete-installation-dialog',
  templateUrl: './delete-installation-dialog.component.html',
  styleUrls: ['./delete-installation-dialog.component.css']
})
export class DeleteInstallationDialogComponent implements OnInit {
  id: string

  constructor(public cookieService: CookieService, public http: HttpClient, public router: Router) {
  }

  ngOnInit(): void {
  }

  deleteInstallation(id: string) {
    let params = new HttpParams().set('sessionToken', this.cookieService.get(CookieHolder.tokenKey)).set('installationId', id);
    this.http.delete(EndpointHolder.installationEndpoint, {params: params}).subscribe(response => {
      if( response) {
        this.router.navigate(['/installations']).then()
      }
    });
  }
}
