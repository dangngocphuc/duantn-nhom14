import { Injectable } from '@angular/core';
import { PagesRequest } from '../models/type';
import { AuthenticationService } from './authentication/authentication.service';
import { HttpBaseService } from './http-base.service';
import { Observable } from 'rxjs';
import { HttpParams } from '@angular/common/http';


@Injectable({
  providedIn: 'root'
})
export class GpuService {

  constructor(private http: HttpBaseService, private authen: AuthenticationService) { }
  getPageGpu(page: PagesRequest): Observable<any> {
    let query = {};
    query['page'] = page.page;
    query['size'] = page.size;

    let params = new HttpParams({ fromObject: query });
    return this.http.get<any>(`/gpu`, params);
  }

  getGpuById(id): Observable<any> {
    return this.http.get<any>(`/gpu/` + id, null);
  }

  getListGpu(): Observable<any> {
    return this.http.get<any>('/gpu/list', null)
  }

  saveGpu(gpu): Observable<any> {
    return this.http.post<any>(`/gpu`,gpu)
  }
}
