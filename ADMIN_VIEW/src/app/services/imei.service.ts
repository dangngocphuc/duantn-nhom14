import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { ImeiRequest, PageImei, PagesRequest } from '../models/type';
import { AuthenticationService } from './authentication/authentication.service';
import { HttpBaseService } from './http-base.service';

@Injectable({
  providedIn: 'root'
})
export class ImeiService {

  httpOption: {};
  getHeaderForImport(): HttpHeaders {
    let header = {}
    // let jwtnamepmh = this.cookieService.get(this.environment.jwtName);
    // header[this.environment.jwtName] = jwtnamepmh;
    // console.log(jwtnamepmh);
    var headers = new HttpHeaders(header);
    return headers;
  }

  getHeader(): HttpHeaders {
    var headers = {};
    if (localStorage) {
      var authorization = localStorage.getItem('Authorization');
      if (authorization) {
        headers['Authorization'] = authorization;
      }
    }
    return new HttpHeaders(headers);
  }
  getOptionForImport() {
    this.httpOption = {
      reportProgress: true,
      responseType: 'json',
      headers: this.getHeader()
    }
    return this.httpOption
  }
  constructor(private http: HttpBaseService, private authen: AuthenticationService,private httpClient: HttpClient) { }

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

  importExcel(formData: FormData): Observable<any> {
    this.httpOption = this.getOptionForImport();
    return this.httpClient.post<any>(environment.urlServer.concat('/imei/import'), formData, this.httpOption);
  }

}
