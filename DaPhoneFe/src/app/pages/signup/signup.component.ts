import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { NzNotificationService } from 'ng-zorro-antd/notification';
import { User } from 'src/app/entity/User';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css'],
})
export class SignupComponent implements OnInit {
  validateForm!: FormGroup;
  userName: String;
  passWord: String;
  clonePassWord: String;
  email: String;
  phone: String;
  confirm: boolean = false;
  msg: String = '';
  msg1: String = '';
  msg2: String = '';
  validPassword: boolean;
  user = new User();
  constructor(
    private notification: NzNotificationService,
    private userService: UserService,
    private fb: FormBuilder,
    private router: Router
  ) {}
  createNotification(type: string, title: string, message: string): void {
    this.notification.create(type, title, message);
  }
  ngOnInit(): void {
    this.validateForm = this.fb.group({
      userName: [null, [Validators.required]],
      password: [null, [Validators.required]],
      email: [null, [Validators.required]],
      clonePassword: [null, [Validators.required]],
      phone: [null, [Validators.required]],
    });
  }
  submitForm(): void {
    for (const i in this.validateForm.controls) {
      if (this.validateForm.controls.hasOwnProperty(i)) {
        this.validateForm.controls[i].markAsDirty();
        this.validateForm.controls[i].updateValueAndValidity();
      }
    }
    if (!this.confirm) {
      this.msg = 'Vui lòng đồng ý với các điều khoản!';
      return;
    }
    this.userName = this.validateForm.controls.userName.value;
    this.passWord = this.validateForm.controls.password.value;
    this.email = this.validateForm.controls.email.value;
    this.phone = this.validateForm.controls.phone.value;

    if (this.userName && this.passWord && this.email && this.phone) {
      this.user.userName = this.userName;
      this.user.userEmail = this.email;
      this.user.userPhone = this.phone;
      this.user.userPass = this.passWord;
      this.userService.saveUser(this.user).subscribe(
        (data) => {
          if (data && data.result) {
            this.createNotification('success', 'Đăng ký thành công!', '');
            this.router.navigate(['/login']);
          } else if (data.errorMessage == '10002') {
            this.msg2 = data.errorCode;
          } else {
            this.createNotification(
              'error',
              'Đăng ký thất bại!',
              'Vui lòng liên hệ quản trị viên.'
            );
          }
        },
        (error) => {
          this.createNotification(
            'error',
            'Đăng ký thất bại!',
            'Vui lòng liên hệ quản trị viên.'
          );
        }
      );
    }
  }
  validPass(input) {
    const pass = this.validateForm.controls.password.value;
    const rePass = input.target.value;
    if (pass == rePass) {
      this.validPassword = true;
      this.msg1 = '';
    } else {
      this.validPassword = false;
      this.msg1 = 'Mật khẩu không khớp!';
    }
  }
  validUser(input) {
    console.log(input.target.value);
  }
}
