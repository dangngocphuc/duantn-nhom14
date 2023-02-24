import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { HttpBaseService } from './http-base.service';

@Injectable({
  providedIn: 'root',
})
export class LoginService {
  Url=environment.urlServer+"/authenticate"
  constructor(private http: HttpBaseService) {}
  loginAdmin(userName, passWord): Observable<any> {
    const user = { username: userName, password: passWord };
    return this.http.post<any>(`/admin/login`, user);
  }
}
