import { HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PagesRequest } from '../models/type';
import { AuthenticationService } from './authentication/authentication.service';
import { HttpBaseService } from './http-base.service';

@Injectable({
  providedIn: 'root'
})
export class PromotionService {
  constructor(private http: HttpBaseService, private authen: AuthenticationService) { }

  getPagePromotion(page: PagesRequest): Observable<any> {
    let query = {};
    query['page'] = page.page;
    query['size'] = page.size;
    let params = new HttpParams({ fromObject: query });
    return this.http.get<any>(`/promotion`, params);
  }

  getPromotionById(id): Observable<any> {
    return this.http.get<any>(`/promotion/` + id, null);
  }

  getListPromotion(): Observable<any> {
    return this.http.get<any>('/promotion/list', null)
  }

  savePromotion(ram): Observable<any> {
    return this.http.post<any>(`/promotion`, ram);
  }

  getPromotionByCode(code): Observable<any> {
    return this.http.get<any>(`/promotion/code/` + code, null);
  }
}
