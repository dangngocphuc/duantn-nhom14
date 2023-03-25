import { HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PagesRequest } from '../models/type';
import { AuthenticationService } from './authentication/authentication.service';
import { HttpBaseService } from './http-base.service';

@Injectable({
  providedIn: 'root'
})
export class RamService {

  constructor(private http: HttpBaseService, private authen: AuthenticationService) { }
  getPageRam(page: PagesRequest): Observable<any> {
    let query = {};
    query['page'] = page.page;
    query['size'] = page.size;

    let params = new HttpParams({ fromObject: query });
    return this.http.get<any>(`/ram`, params);
  }

  getRamById(id): Observable<any> {
    return this.http.get<any>(`/ram/` + id, null);
  }

  getListRam(): Observable<any> {
    return this.http.get<any>('/ram/list', null)
  }

  saveRam(ram): Observable<any> {
    return this.http.post<any>(`/ram`, ram);
  }
}
