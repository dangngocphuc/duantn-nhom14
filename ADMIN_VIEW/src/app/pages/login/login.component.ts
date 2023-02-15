import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ERROR, ROLE } from 'src/app/commons/common';
import { LoginRequest, LoginResponse, User } from 'src/app/models/type';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import Swal from 'sweetalert2';
import { isBuffer } from 'util';
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  constructor(private fb: FormBuilder,
    private authenticationService: AuthenticationService, private activatedRoute: ActivatedRoute,
    private router: Router) {
  }
  userName: String;
  passWord: String;
  remember: boolean;
  loginRequest = new LoginRequest();
  validateForm: FormGroup;
  curUrl = "/dashboard";
  user = new User();
  loginResponse = new LoginResponse();
  currentUser = new User();
  submitForm() {
    debugger;
    // for (const i in this.validateForm.controls) {
    //   if (this.validateForm.controls.hasOwnProperty(i)) {
    //     this.validateForm.controls[i].markAsDirty();
    //     this.validateForm.controls[i].updateValueAndValidity();
    //   }
    // }
    // this.userName = this.validateForm.controls.userName.value;
    // this.passWord = this.validateForm.controls.password.value;
    // this.remember = this.validateForm.controls.remember.value;
    console.log(this.loginRequest)
    if (this.loginRequest) {
      this.authenticationService.loginAdmin(this.loginRequest?.username, this.loginRequest?.password, this.loginRequest.remember).subscribe(
        (data) => {
          if (data) {
            this.loginResponse = data;
            if (this.loginResponse.errorCode == ERROR.SUCCESS) {
              this.currentUser = this.loginResponse.userDetail;
              if (this.currentUser.permissions.includes(ROLE.ADMIN)) {
                localStorage.setItem("currentUser", JSON.stringify(this.loginResponse.userDetail));
                localStorage.setItem("Authorization", this.loginResponse.authorization)
                this.router.navigate([this.curUrl]);
              } else {
                Swal.fire('Tài Khoản không có quyền truy cập', '', 'error');
              }
            }
            else if(this.loginResponse.errorCode == ERROR.FAILURE){
              Swal.fire(this.loginResponse.errorMessage, '', 'error');
            }
            else{
              Swal.fire('Lỗi không xác định', '', 'error');
            }
          } else {
            Swal.fire('error', '', 'error');
          }
        },
        (error: HttpErrorResponse) => {
          if (error.error instanceof ProgressEvent) {
            Swal.fire('Lỗi kết nối', '', 'error');
          } else {
            const errorResponse: HttpErrorResponse = error.error;
            Swal.fire(errorResponse.message, '', 'error');
          }
        }
      );
    }
  }
  ngOnInit() {
    // debugger;
    this.validateForm = new FormGroup({
      name: new FormControl(null, [Validators.required]),
      pass: new FormControl(null, [Validators.required]),
      remember: new FormControl(null)
    });

    // this.activatedRoute.queryParams.subscribe(params => {
    //   if (params['returnUrl'])
    //     this.curUrl = params['returnUrl'];
    // });
  }
  // ngOnDestroy() {
  // }
}
