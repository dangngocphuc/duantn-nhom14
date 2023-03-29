import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpBaseService } from './http-base.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  constructor(private http: HttpBaseService) {}
  getUsers(params): Observable<any> {
    return this.http.getByParams<any>(`/user`, params);
  }
  deleteUser(id): Observable<any> {
    return this.http.delete<any>(`/user/` + id);
  }
  saveUser(user): Observable<any> {
    return this.http.post<any>(`/user/save`, user);
  }
}
