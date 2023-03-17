import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { HttpBaseService } from './http-base.service';

@Injectable({
  providedIn: 'root'
})
export class GhnService {

  constructor(private http: HttpClient) {

  }

  getHeader(): HttpHeaders {
    let headers = new HttpHeaders();
    headers = headers.set('Content-Type', 'application/json; charset=utf-8');
    headers = headers.set('token', '0d0df72c-bfe8-11ed-ab31-3eeb4194879e');
    headers = headers.set('shop_id', '122071')
    return headers;
  }

  getListProvince(): Observable<any> {
    var httpOptions: Object = {
      headers: this.getHeader(),
    };
    console.log(httpOptions);
    return this.http.get<any>('https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/province', httpOptions);
  }

  getListDistrict(provinceId): Observable<any> {
    var httpOptions: Object = {
      headers: this.getHeader(),
    };
    console.log(httpOptions);
    return this.http.post<any>('https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/district', { "province_id": provinceId }, httpOptions);
  }

  getListWard(districtId): Observable<any> {
    var httpOptions: Object = {
      headers: this.getHeader(),
    };
    console.log(httpOptions);
    return this.http.post<any>('https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/ward', { "district_id": districtId }, httpOptions);
  }

  getService(data) {
    var httpOptions: Object = {
      headers: this.getHeader(),
    };
    return this.http.post<any>('https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/available-services', data, httpOptions);
  }

  getFeeService(data) {
    var httpOptions: Object = {
      headers: this.getHeader(),
    };
    return this.http.post<any>('https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee', data, httpOptions);
  }

  getLeadTime(data) {
    var httpOptions: Object = {
      headers: this.getHeader(),
    };
    return this.http.post<any>('https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/leadtime', data, httpOptions);
  }

}
