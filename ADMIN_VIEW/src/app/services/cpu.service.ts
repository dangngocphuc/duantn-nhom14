import { HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PageCpu, PagesRequest } from '../models/type';
import { AuthenticationService } from './authentication/authentication.service';
import { HttpBaseService } from './http-base.service';

@Injectable({
  providedIn: 'root'
})
export class CpuService {

  constructor(private http: HttpBaseService, private authen: AuthenticationService) { }
  getPageCpu(page: PagesRequest): Observable<PageCpu> {
    let query = {};
    query['page'] = page.page;
    query['size'] = page.size;

    let params = new HttpParams({ fromObject: query });
    return this.http.get<PageCpu>(`/cpu`, params);
  }

  getCpuById(id): Observable<any> {
    return this.http.get<any>(`/cpu/` + id, null);
  }

  getListCpu(): Observable<any>{
    return this.http.get<any>('/cpu/list',null)
  }

  saveCpu(cpu): Observable<any> {
    return this.http.post<any>(`/cpu`, cpu);
  }

  deleteCpu(id): Observable<any> {
    return this.http.delete<any>(`/cpu/` + id);
  }
}
