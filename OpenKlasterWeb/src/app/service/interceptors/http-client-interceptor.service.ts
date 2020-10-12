import {Injectable} from '@angular/core';
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {tap} from "rxjs/operators";
import {Router} from "@angular/router";

@Injectable()
export class HttpClientInterceptor implements HttpInterceptor {


  constructor(private router?: Router) {
    console.log("!!!!!!!" + router)
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request)
      .pipe(tap(() => {
        },
        (error: HttpErrorResponse) => {
          let errorMessage = error.message;
          let errorStatus = error.status;
          console.error("Error occurred while performing http request1: " + errorMessage)
          if (errorStatus !== HttpResponseCode.Unauthorized) {
            return throwError(error)
          }
          this.router.navigate(['login']).then();
        }));
  }
}

class HttpResponseCode {
  static Unauthorized = 401
}
