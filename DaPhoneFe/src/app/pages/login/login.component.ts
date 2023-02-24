import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NzNotificationService } from 'ng-zorro-antd/notification';
import { CaptchaService } from 'src/app/captcha/captcha.service';
import { UserService } from 'src/app/services/user.service';
import { SocialAuthService, GoogleLoginProvider, SocialUser } from 'angularx-social-login';
import { TokenDto } from 'src/app/entity/types';
import { ERROR, ROLE } from 'src/app/commons/common';
import { LoginResponse, User } from 'src/app/models/type';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  validateForm!: FormGroup;
  userName: String;
  passWord: String;
  isPayment: Boolean = false;

  captchaStatus: any = '';
  captchaConfig: any = {
    type: 2,
    length: 6,
    cssClass: 'custom',
    back: {
      stroke: 'red',
      solid: '#f2efd2',
    },
    font: {
      color: '#000000',
      size: '35px',
    },
  };
  submitted = false;

  socialUser!: SocialUser;
  isLoggedin: boolean = false;

  user = new User();
  loginResponse = new LoginResponse();
  currentUser = new User();
  curUrl = "/home";
  constructor(
    route: ActivatedRoute,
    private notification: NzNotificationService,
    private userService: UserService,
    private activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    private router: Router,
    private captchaService: CaptchaService,
    private socialAuthService: SocialAuthService
  ) {
    route.params.subscribe((val) => {
      this.isPayment =
        (this.activatedRoute.snapshot.params.isPay || 0) == 1 ? true : false;
    });


  }

  createNotification(type: string, title: string, message: string): void {
    this.notification.create(type, title, message);
  }

  ngOnInit(): void {

    this.validateForm = this.fb.group({
      userName: [null, [Validators.required]],
      password: [null, [Validators.required]],
      remember: [true],
    });

    this.socialAuthService.authState.subscribe((user) => {
      this.socialUser = user;
      this.isLoggedin = (user != null);
      console.log(this.socialUser);
      if (this.socialUser) {
        debugger;
        const tokenGoogle = new TokenDto(this.socialUser.idToken);
        this.userService.google(tokenGoogle).subscribe(
          (res) => {
            this.loginResponse = res;
            console.log(this.loginResponse);
              if (this.loginResponse.errorCode == ERROR.SUCCESS) {
                this.currentUser = this.loginResponse.userDetail;
                // if (this.currentUser.permissions.includes(ROLE.)) {
                localStorage.setItem("currentUser", JSON.stringify(this.loginResponse.userDetail));
                localStorage.setItem("Authorization", this.loginResponse.authorization)
                // this.router.navigate([this.curUrl]);
                if (this.isPayment) {
                  this.router.navigate(['/mycart']);
                } else {
                  window.location.href = '/home';
                }
                // } else {
                //   this.createNotification('error','Lỗi','Tài Khoản không có quyền truy cập')
                //   // Swal.fire('Tài Khoản không có quyền truy cập', '', );
                // }
              }
              else if (this.loginResponse.errorCode == ERROR.FAILURE) {
                this.createNotification('error', 'Lỗi', this.loginResponse.errorMessage)
              }
              else {
                this.createNotification('error', 'Lỗi', 'Lỗi không xác định')
              }
          },
          err => {
            console.log(err);
            this.logOut();
          }
        );
        // if (this.isPayment) {
        //   this.router.navigate(['/mycart']);
        // } else {
        //   window.location.href = '/home';
        // }
      }
    });


  }
  submitForm(): void {
    if (this.validateForm.status === 'INVALID') {
      this.submitted = true;
      return;
    }
    for (const i in this.validateForm.controls) {
      if (this.validateForm.controls.hasOwnProperty(i)) {
        this.validateForm.controls[i].markAsDirty();
        this.validateForm.controls[i].updateValueAndValidity();
      }
    }
    this.userName = this.validateForm.controls.userName.value;
    this.passWord = this.validateForm.controls.password.value;
    this.captchaService.captchStatus.subscribe((status) => {
      this.submitted = true;
      this.captchaStatus = status;
      if (status == false) {
        this.createNotification(
          'warning',
          'Đăng nhập không thành công!',
          'Vui lòng kiểm tra lại mã captcha!.'
        );
      } else if (status == true) {
        if (this.userName && this.passWord) {
          this.userService.login(this.userName, this.passWord).subscribe(
            (data) => {
              this.loginResponse = data;
              if (this.loginResponse.errorCode == ERROR.SUCCESS) {
                this.currentUser = this.loginResponse.userDetail;
                // if (this.currentUser.permissions.includes(ROLE.)) {
                localStorage.setItem("currentUser", JSON.stringify(this.loginResponse.userDetail));
                localStorage.setItem("Authorization", this.loginResponse.authorization)
                // this.router.navigate([this.curUrl]);
                if (this.isPayment) {
                  this.router.navigate(['/mycart']);
                } else {
                  window.location.href = '/home';
                }
                // } else {
                //   this.createNotification('error','Lỗi','Tài Khoản không có quyền truy cập')
                //   // Swal.fire('Tài Khoản không có quyền truy cập', '', );
                // }
              }
              else if (this.loginResponse.errorCode == ERROR.FAILURE) {
                this.createNotification('error', 'Lỗi', this.loginResponse.errorMessage)
              }
              else {
                this.createNotification('error', 'Lỗi', 'Lỗi không xác định')
              }
            },
            (error) => {
              if (error.error instanceof ProgressEvent) {
                // Swal.fire('Lỗi kết nối', '', 'error');
              } else {
                this.createNotification(
                  'error',
                  'Đăng nhập không thành công!',
                  'Vui lòng kiểm tra lại tài khoản và mật khẩu.'
                );
              }

            }
          );
        }
      }
    })
  }

  // Initial implicite flow using OAuth2 protocol
  loginWithGoogle(): void {
    this.socialAuthService.signIn(GoogleLoginProvider.PROVIDER_ID).then(
      data => {
        this.socialUser = data;
        const tokenGoogle = new TokenDto(this.socialUser.idToken);
        this.userService.google(tokenGoogle).subscribe(
          (res) => {
            // this.tokenService.setToken(res.value);
            this.loginResponse = res;
              if (this.loginResponse.errorCode == ERROR.SUCCESS) {
                this.currentUser = this.loginResponse.userDetail;
                // if (this.currentUser.permissions.includes(ROLE.)) {
                localStorage.setItem("currentUser", JSON.stringify(this.loginResponse.userDetail));
                localStorage.setItem("Authorization", this.loginResponse.authorization)
                // this.router.navigate([this.curUrl]);
                if (this.isPayment) {
                  this.router.navigate(['/mycart']);
                } else {
                  window.location.href = '/home';
                }
                // } else {
                //   this.createNotification('error','Lỗi','Tài Khoản không có quyền truy cập')
                //   // Swal.fire('Tài Khoản không có quyền truy cập', '', );
                // }
              }
              else if (this.loginResponse.errorCode == ERROR.FAILURE) {
                this.createNotification('error', 'Lỗi', this.loginResponse.errorMessage)
              }
              else {
                this.createNotification('error', 'Lỗi', 'Lỗi không xác định')
              }
          },
          err => {
            console.log(err);
            this.logOut();
          }
        );
      }
    ).catch(
      err => {
        console.log(err);
      }
    );
  }


  // Logout the current session
  logOut(): void {
    this.socialAuthService.signOut();
  }


}
