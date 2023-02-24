import { Injectable } from '@angular/core';
import { User, LoginRequest, LoginResponse } from 'src/app/models/type';
import { Observable, BehaviorSubject } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { tap } from 'rxjs/internal/operators/tap';
import { CookieService } from 'ngx-cookie-service';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private urlRoot: String;
  currentUser: Observable<User>;
  // private rootUrl: String;
  constructor(private http: HttpClient, private cookieService: CookieService) {
    this.urlRoot = environment.urlServer;
  }
  getHeader(): HttpHeaders {
    var headers = { 'Content-Type': 'application/json' };
    return new HttpHeaders(headers);
  }
  getHeaderUpload(): HttpHeaders {
    var headers = {};
    // if (localStorage) {
    //   var authorization = localStorage.getItem("Authorization");
    //   if (authorization) {
    //     headers['Authorization'] = authorization;
    //   }
    // }
    return new HttpHeaders(headers);
  }

  login(loginRequest: LoginRequest): Observable<LoginResponse> {
    console.log(loginRequest)
    var httpOption = {
      headers: this.getHeader()
    }
    return this.http.post<LoginResponse>(this.urlRoot + '/authenticate/login', loginRequest, httpOption).pipe(
      tap(loginResponse => {
        if (loginResponse && loginResponse.authorization) {
          this.cookieService.set('DVCBEJSESSIONID', loginResponse.authorization, undefined, "/", undefined, undefined, "Strict");
          // localStorage.setItem("Authorization", loginResponse.authorization);
          // let user=loginResponse.userDetail;
          // localStorage.setItem("currentUser", JSON.stringify(user));
          // this.currentUser = new BehaviorSubject(user).asObservable();
        }
      }));
  }

  loginAdmin(userName, passWord, remember): Observable<any> {
    var httpOption = {
      headers: this.getHeader()
    }
    const user = { username: userName, password: passWord, remember: remember };
    return this.http.post<any>(`${this.urlRoot}/authenticate/admin/login`, user, httpOption).pipe(
      tap(data => {
        if (data && data.authorization) {
          this.cookieService.set('COOKIEID', data.authorization, undefined, "/", undefined, undefined, 'Strict');
        }
      }
    ));
  }

  getUserSession(): Observable<boolean> {
    var httpOption = {
      headers: { responseType: 'text' }
    }
    return this.http.get<boolean>(this.urlRoot + "/getsession", httpOption).pipe(
      tap(userSession => {
        if (!userSession) {
          window.location.href = '/login'
        }
      }))
  }

  getUserDetail(): Observable<User> {
    debugger;
    var httpOption = {
      headers: this.getHeader()
    }
    httpOption["withCredentials"] = true
    let DVCJSESSIONID = this.cookieService.get('COOKIEID');
    let user = JSON.parse(localStorage.getItem('currentUser'));
    if (user && user.tokenId === DVCJSESSIONID) {
      return new BehaviorSubject<User>(user).asObservable()
    } else {
      return this.http.get<User>(this.urlRoot + "/authenticate/userdetail", httpOption).pipe(
        tap(user => {
          if (user) {
            // this.cookieService.set( 'DVCJSESSIONID', loginResponse.authorization);
            localStorage.setItem("currentUser", JSON.stringify(user));
            // localStorage.setItem("Authorization", loginResponse.authorization);
            // let user=loginResponse.userDetail;
            // localStorage.setItem("currentUser", JSON.stringify(user));
            // this.currentUser = new BehaviorSubject(user).asObservable();
          }
        }));
    }
  }
  logout() {
    localStorage.removeItem("currentUser");
    this.currentUser = null;
    this.cookieService.delete('DVCBEJSESSIONID', "/");
    this.cookieService.delete('JSESSIONID', "/");
  }
}
