import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { TokenDto } from '../entity/types';
import { HttpBaseService } from './http-base.service';



@Injectable({
  providedIn: 'root',
})
export class UserService {
  Url=environment.urlServer
  constructor(private http: HttpBaseService) {}
  login(userName, passWord): Observable<any> {
    const user = { username: userName, password: passWord };
    return this.http.post<any>(`/authenticate/user/login`, user);
  }
  saveUser(user): Observable<any> {
    return this.http.post<any>(`/user/save`, user);
  }
  getUser(id): Observable<any> {
    return this.http.get<any>(`/user/` + id, null);
  }

  public google(tokenDto: TokenDto): Observable<any> {
    return this.http.post<TokenDto>('/oauth2/google', tokenDto);
  }
  updateUser(user): Observable<any> {
    return this.http.put<any>(`/user`, user);
  }
}
