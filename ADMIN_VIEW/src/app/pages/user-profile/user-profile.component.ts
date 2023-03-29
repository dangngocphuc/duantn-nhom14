import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NzMessageService } from 'ng-zorro-antd/message';
import { NzModalService } from 'ng-zorro-antd/modal';
import { NzNotificationService } from 'ng-zorro-antd/notification';
import { NzTableQueryParams } from 'ng-zorro-antd/table';
import { ROLE } from 'src/app/commons/common';
import { Role, User } from 'src/app/models/type';
// import { User } from 'src/app/entity/User.model';
import { UserService } from 'src/app/services/user.service';
@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss']
})
export class UserProfileComponent implements OnInit {
  searchForm!: FormGroup;
  userForm!: FormGroup;
  isVisible = false;
  total = 1;
  loading = true;
  pageSize = 5;
  imageUrl: string = null;
  pageIndex = 1;
  listOfData: User[] = [];
  user = new User();
  isEdit = false;
  isInsert = false;
  isView = false;
  controlArray: Map<string, any> = new Map<string, any>();
  users = new User();
  listOfRole: Role[] = [
    {id:1,name:"ADMIN"},
    {id:2,name:"USER"},
    {id:4,name:"STAFF"},
  ];

  listOfSelectedValue : Role[];

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private notification: NzNotificationService,
    private modal: NzModalService,
    private msg: NzMessageService
  ) {}

  ngOnInit(): void {
    //form search
    this.searchForm = this.fb.group({
      userName: [null],
      email: [null],
      phone: [0],
    });
    // form product
    this.userForm = this.fb.group({
      userID: [null],
      userName: [null, [Validators.required]],
      userEmail: [null, [Validators.required]],
      userPass: [null, [Validators.required]],
      userPhone: [0, [Validators.required]],
      enabled: [false, [Validators.required]],
      roles: [],
    });

    // const children: 
    // for (let i = 10; i < 36; i++) {
    //   children.push(`${i.toString(36)}${i}`);
    // }
    // this.listOfRole= children;
  }
  showModal(id, action): void {
    this.isVisible = true;
    if (action == 'EDIT') {
      this.isEdit = true;
      this.isInsert = false;
      this.isView = false;
      // this.userService.g
      
      console.log(this.listOfData);
      this.listOfData.forEach((item) => {
        debugger;
        if (item.userID == id) {
          item.userName = item.username 
          this.userForm.controls.userID.setValue(item.userID);
          this.userForm.controls.userName.setValue(item.userName);
          this.userForm.controls.userEmail.setValue(item.userEmail);
          this.userForm.controls.userPass.disable();
          // this.userForm.controls.userPass.setValue(item.userPass);
          this.userForm.controls.userPhone.setValue(item.userPhone);
          this.userForm.controls.enabled.setValue(item.enabled);
          this.listOfSelectedValue = item.roles;
          console.log(this.listOfSelectedValue );
        }
      });
    }
    if (action == 'INSERT') {
      this.isEdit = false;
      this.isInsert = true;
      this.isView = false;
      this.userForm.controls.userID.setValue(null);
      this.userForm.controls.userName.setValue(null);
      this.userForm.controls.userEmail.setValue(null);
      this.userForm.controls.userPass.setValue(null);
      this.userForm.controls.userPhone.setValue(0);
    }
  }

  handleOk(): void {
    debugger;
    if (this.isView) {
      this.isVisible = false;
      return;
    }
    for (const i in this.userForm.controls) {
      if (this.userForm.controls.hasOwnProperty(i)) {
        this.userForm.controls[i].markAsDirty();
        this.userForm.controls[i].updateValueAndValidity();
      }
    }
    this.user.userID =this.userForm.controls.userID.value;
    this.user.userName =this.userForm.controls.userName.value;
    this.user.userPhone =this.userForm.controls.userPhone.value;
    this.user.userEmail =this.userForm.controls.userEmail.value;
    this.user.userPass =this.userForm.controls.userPass.value;
    this.user.enabled =this.userForm.controls.enabled.value;
    this.user.roles = this.listOfSelectedValue;
    this.userService.saveUser(this.user).subscribe(
      (data) => {
        if (data && data.result) {
          if(this.isInsert){
            this.createNotification(
              'success',
              'Thêm thành công!',
              ''
            );  
          }else{
            this.createNotification(
              'success',
              'Sửa thành công!',
              ''
            );  
          }
        }
      },
      (error) => {
        debugger;
        // if(error.errorCode == '10003'){
          this.createNotification(
            'error',
             error.errorMessage,
            'Vui lòng liên hệ quản trị viên.'
          );
        // }
      },
      () => {
        this.isVisible = false;
        this.getUsers(this.pageIndex, this.pageSize, null, null);
      }
    );
  }

  handleCancel(): void {
    this.isVisible = false;
    this.userForm.reset();
    this.userForm.controls.userPass.clearValidators();
  }

  search() {
    const name = this.searchForm.controls.userName.value;
    const phone = this.searchForm.controls.phone.value;
    const email = this.searchForm.controls.email.value;
    this.controlArray.set('userName', name);
    this.controlArray.set('phone', phone);
    this.controlArray.set('email', email);
    this.getUsers(this.pageIndex, this.pageSize, null, null);
  }

  onQueryParamsChange(params: NzTableQueryParams): void {
    const { pageSize, pageIndex, sort } = params;
    const currentSort = sort.find((item) => item.value !== null);
    const sortField = (currentSort && currentSort.key) || null;
    const sortOrder = (currentSort && currentSort.value) || null;
    this.getUsers(pageIndex, pageSize, sortField, sortOrder);
  }

  resetForm(): void {
    this.searchForm.reset();
  }

  createNotification(type: string, title: string, message: string): void {
    this.notification.create(type, title, message);
  }

  getUsers(
    pageIndex: number,
    pageSize: number,
    sortField: string | null,
    sortOrder: string | null
  ) {
    this.controlArray.set('pageIndex', pageIndex);
    this.controlArray.set('pageSize', pageSize);
    this.controlArray.set('sortField', sortField);
    this.controlArray.set('sortOrder', sortOrder);
    // get product
    this.userService.getUsers(this.controlArray).subscribe(
      (data) => {
        if (data && data.results) {
          this.loading = false;
          this.listOfData = data.results;
          this.total = data.rowCount;
        }
      },
      (error) => {
        this.createNotification(
          'error',
          'Có lỗi xảy ra!',
          'Vui lòng liên hệ quản trị viên.'
        );
      }
    );
  }

  delUser(id) {
    this.modal.confirm({
      nzTitle: 'Bạn có chắc chắn muốn xóa?',
      nzContent: '',
      nzOkText: 'Yes',
      nzOkType: 'primary',
      nzOkDanger: true,
      nzOnOk: () =>
        this.userService.deleteUser(id).subscribe(
          (data) => {
            this.createNotification('success', 'Xoá thành công!', '');
          },
          (error) => {
            this.createNotification(
              'error',
              'Có lỗi xảy ra!',
              'Vui lòng liên hệ quản trị viên.'
            );
          },
          () => {
            this.getUsers(this.pageIndex, this.pageSize, null, null);
          }
        ),
      nzCancelText: 'No',
      nzOnCancel: () => console.log('Cancel'),
    });
  }

}
