import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NzMessageService } from 'ng-zorro-antd/message';
import { NzModalService } from 'ng-zorro-antd/modal';
import { NzNotificationService } from 'ng-zorro-antd/notification';
import { NzTableQueryParams } from 'ng-zorro-antd/table';
// import { User } from 'src/app/entity/User.model';
// import { UserService } from 'src/app/services/user.service';
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
  // listOfData: User[] = [];
  // user = new User();
  isEdit = false;
  isInsert = false;
  isView = false;
  controlArray: Map<string, any> = new Map<string, any>();
  constructor(  private fb: FormBuilder,
    // private userService: UserService,
    private notification: NzNotificationService,
    private modal: NzModalService,
    private msg: NzMessageService) { }

  ngOnInit() {
  }

}
