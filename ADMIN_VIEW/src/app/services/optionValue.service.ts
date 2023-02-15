import { HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { OptionRequest, OptionValueRequest, PageOption, PageOptionValue, PageProduct, PagesRequest } from '../models/type';
import { HttpBaseService } from './http-base.service';

@Injectable({
  providedIn: 'root'
})
export class OptionValueService {

  constructor(private http: HttpBaseService) { }
  getOptions(page: PagesRequest, request: OptionValueRequest): Observable<PageOptionValue> {
 
    let query = {};
    query['page'] = page.page;
    query['size'] = page.size;
    if (request.optionId) {
      query['optionId'] = request.optionId;
    }
    let params = new HttpParams({ fromObject: query });

    return this.http.get<PageOptionValue>(`/option/value`, params);
  }
  getOptionById(id): Observable<any> {
    return this.http.get<any>(`/option/value` + id, null);
  }
  deleteOption(id): Observable<any> {
    return this.http.delete<any>(`/option/delete/` + id);
  }
  saveOption(option): Observable<any> {
    return this.http.post<any>(`/option/value/save`, option);
  }

}
