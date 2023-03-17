import { HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ImeiRequest, PageImei, PagesRequest } from '../models/type';
import { AuthenticationService } from './authentication/authentication.service';
import { HttpBaseService } from './http-base.service';

@Injectable({
  providedIn: 'root'
})
export class ImeiService {

  constructor(private http: HttpBaseService, private authen: AuthenticationService) { }

  getImei(page: PagesRequest, request: ImeiRequest): Observable<PageImei> {
    let query = {};
    query['page'] = page.page;
    query['size'] = page.size;
    if (request.imei) {
      query['imei'] = request.imei;
    }
    if (request.productId) {
      query['productId'] = request.productId;
    }
    let params = new HttpParams({ fromObject: query });
    return this.http.get<PageImei>(`/imei`, params);
  }

  getImeiById(id): Observable<any> {
    return this.http.get<any>(`/imei/` + id, null);
  }

  getListImeiByProductDetail(page: PagesRequest, data): Observable<any> {
    debugger;
    let query = {};
    if (page.page) query['page'] = page.page;
    if (page.size) query['size'] = page.size;
    query['sort'] = 'id,desc';
    if (data && data.imei && data.imei != '')
      query['imei'] = data.imei.toString();
    if (data && data.productId && data.productId != '')
      query['productId'] = data.productId.toString();
    let params = new HttpParams({ fromObject: query });

    return this.http.get<any>(`/imei/list/`, params);
  }

}
