import { HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { BrandRequest, PageBrand, PagesRequest } from '../models/type';
import { AuthenticationService } from './authentication/authentication.service';
import { HttpBaseService } from './http-base.service';

@Injectable({
  providedIn: 'root'
})
export class BrandService {

  constructor(private http: HttpBaseService, private authen: AuthenticationService) { }

  getPageBrand(page: PagesRequest, request: BrandRequest): Observable<PageBrand> {
    let query = {};
    query['page'] = page.page;
    query['size'] = page.size;
    if (request.brandName) {
      query['brandName'] = request.brandName;
    }
    if (request.brandMa) {
      query['brandMa'] = request.brandMa;
    }
    let params = new HttpParams({ fromObject: query });
    return this.http.get<PageBrand>(`/brand`, params);
  }

  getBrandById(id): Observable<any> {
    return this.http.get<any>(`/brand/` + id, null);
  }

  saveBrand(brand): Observable<any> {
    return this.http.post<any>(`/brand/save`, brand);
  }

  getListBrand():Observable<any> {
    return this.http.get<any>(`/brand/list`, null);
  }
}
