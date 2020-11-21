import {Injectable} from '@angular/core';
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {finalize, tap} from "rxjs/operators";
import {Router} from "@angular/router";
import {NgxSpinnerService} from 'ngx-spinner';
import {ToastrService} from 'ngx-toastr';

@Injectable()
export class HttpClientInterceptor implements HttpInterceptor {
  private count = 0;

  constructor(private router: Router,
              private spinner: NgxSpinnerService,
              private toastr: ToastrService) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    this.spinner.show();
    this.count++;

    return next.handle(request)
      .pipe(tap(() => {
        },
        (error: HttpErrorResponse) => {
          let errorMessage = error.message;
          let errorStatus = error.status;
          console.error("Error occurred while performing http request1: " + errorMessage)
          if (errorStatus !== HttpResponseCode.Unauthorized) {
            if (request.url.indexOf('user') === -1 ) {
              this.toastr.error('Wystąpił problem z połączeniem z systemtu.');
            }
            return throwError(error)
          }
          if (request.url.indexOf('login') !== -1 && request.url.indexOf('register') !== -1) {
            this.toastr.warning('Zaloguj się ponownie.');
          }
          this.router.navigate(['login']).then();
        }),
        finalize(() => {
          this.count--;
          if (this.count === 0) {
            this.spinner.hide();
          }
        })
      );
  }
}

class HttpResponseCode {
  static Unauthorized = 401
}
