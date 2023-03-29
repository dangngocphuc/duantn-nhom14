// import { Injectable } from '@angular/core';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { Observable } from 'rxjs';
import { User, UserLogin } from '../models/type';
import { AuthenticationService } from '../services/authentication/authentication.service';


@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(private authService: AuthenticationService, private router: Router,private cookieService: CookieService) { }
  currentUser: UserLogin;
  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
      // this.authService.getCurrentUser().subscribe(currentUser => {
      //   this.currentUser = currentUser;
      // });
      // console.log(this.currentUser);
      let Authorization = localStorage.getItem('Authorization');
      if (Authorization) {
          return true;
      }
      let COOKIEID=this.cookieService.get('COOKIEID');
      if (COOKIEID) {
          // logged in so return true
          // let user=JSON.parse(localStorage.getItem('currentUser'));
          // if( !state.url.includes('/dashboard') && user && user.menus.filter((e)=>e.duongDan.indexOf(state.url)>=0).length ==0){
          //   window.location.href='/dnvtbe/dashboard'
          //   return false;
          // }
          // logged in so return true
          // return this.authService.getUserSession();
          return true;
          // return this.authService.getUserSession();
      }
      // not logged in so redirect to login page with the return url
      // this.router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
      window.location.href='/login'
      return false;
  }
}
