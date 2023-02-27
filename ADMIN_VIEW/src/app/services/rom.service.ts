import { Injectable } from '@angular/core';
import { PagesRequest } from '../models/type';
import { AuthenticationService } from './authentication/authentication.service';
import { HttpBaseService } from './http-base.service';
import { Observable } from 'rxjs';
import { HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class RomService {

  constructor(private http: HttpBaseService, private authen: AuthenticationService) { }
  getPageRom(page: PagesRequest): Observable<any> {
    let query = {};
    query['page'] = page.page;
    query['size'] = page.size;

    let params = new HttpParams({ fromObject: query });
    return this.http.get<any>(`/rom`, params);
  }

  getRomById(id): Observable<any> {
    return this.http.get<any>(`/rom/` + id, null);
  }

  getListRom(): Observable<any> {
    return this.http.get<any>('/rom/list', null)
  }
}
