import { HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Option, OptionRequest, PageOption, PageProduct, PagesRequest } from '../models/type';
import { AuthenticationService } from './authentication/authentication.service';
import { HttpBaseService } from './http-base.service';

@Injectable({
  providedIn: 'root'
})
export class OptionService {
  constructor(private http: HttpBaseService,private authen: AuthenticationService) { }
  getOptions(page: PagesRequest, request: OptionRequest): Observable<PageOption> {
    let query = {};
    query['page'] = page.page;
    query['size'] = page.size;
    if (request.optionMa) {
      query['optionMa'] = request.optionMa;
    }
    if (request.optionTen) {
      query['optionTen'] = request.optionTen;
    }
    let params = new HttpParams({ fromObject: query });
    return this.http.get<PageOption>(`/option`, params);
  }
  getOptionById(id): Observable<any> {
    return this.http.get<any>(`/option/` + id, null);
  }
  deleteOption(id): Observable<any> {
    return this.http.delete<any>(`/option/delete/` + id);
  }
  saveOption(option): Observable<any> {
    return this.http.post<any>(`/option/save`, option);
  }
  public ngSelect(page: PagesRequest, data): Observable<any> {
    let query = {};
    if (page.page) query['page'] = page.page;
    if (page.size) query['size'] = page.size;
    query['sort'] = 'id,desc';
    if (data && data.optionTen && data.optionTen != '')
      query['optionTen'] = data.optionTen.toString();
    let params = new HttpParams({ fromObject: query });
    // this.getOption();
    // this.httpOption['params'] = params;
    return this.http.get<any>(`/option/ngselect`, params);
  }
  httpOption: {};
  getOption() {
    this.httpOption = {
      headers: this.authen.getHeader(),
    };
    this.httpOption['withCredentials'] = true;
    return this.httpOption;
  }
}
