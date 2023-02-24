import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { finalize, Observable } from 'rxjs';
import { LoaderService } from '../services/loader/loader.service';

@Injectable()
export class LoaderInterceptor implements HttpInterceptor {

  constructor(public loaderService: LoaderService) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    this.loaderService.show();
    // req = req.clone({
    //     withCredentials: true,
    //   });
    return next.handle(request).pipe(
        finalize(() => this.loaderService.hide())
    );
  }
}
