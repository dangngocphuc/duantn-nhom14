import { Component, OnInit } from '@angular/core';
import { NzModalService } from 'ng-zorro-antd/modal';
import { NzNotificationService } from 'ng-zorro-antd/notification';
import { NzTableQueryParams } from 'ng-zorro-antd/table';
import { Bill } from 'src/app/entity/Bill';
import { User } from 'src/app/models/type';
import { BillService } from 'src/app/services/bill.service';

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.css'],
})
export class OrderComponent implements OnInit {
  isVisible = false;
  total = 1;
  loading = true;
  pageSize = 5;
  pageIndex = 1;
  listOfData: Bill[] = [];
  controlArray: Map<string, any> = new Map<string, any>();
  listOfBillDetail: any;
  billId: string;
  bill: Bill;
  status: number;
  currentUser = new User();
  constructor(
    private notification: NzNotificationService,
    private modal: NzModalService,
    private billService: BillService
  ) {
    const user = localStorage.getItem('currentUser') || '';
    if (user) {
      this.currentUser = JSON.parse(user);
    }
  }

  createNotification(type: string, title: string, message: string): void {
    this.notification.create(type, title, message);
  }

  ngOnInit(): void {
    
  }

  showModal(data): void {
    this.isVisible = true;
    debugger;
    this.billId = data.id;
    this.getBillDetail(data.id);
    this.status =
      data.status == 'Process'
        ? 1
        : data.status == 'Delivery'
        ? 2
        : data.status == 'Delivered'
        ? 3
        : 4;
  }

  handleOk() {
    this.isVisible = false;
  }

  onQueryParamsChange(params: NzTableQueryParams): void {
    
    const { pageSize, pageIndex, sort } = params;
    const currentSort = sort.find((item) => item.value !== null);
    const sortField = (currentSort && currentSort.key) || null;
    const sortOrder = (currentSort && currentSort.value) || null;
    this.getBills(pageIndex, pageSize, sortField, sortOrder);
  }

  getBills(
    pageIndex: number,
    pageSize: number,
    sortField: string | null,
    sortOrder: string | null
  ) {
    debugger;
    this.controlArray.set('pageIndex', pageIndex);
    this.controlArray.set('pageSize', pageSize);
    this.controlArray.set('sortField', sortField);
    this.controlArray.set('sortOrder', sortOrder);
    this.controlArray.set('userId', this.currentUser.userID);
    // get bill
    this.billService.getBills(this.controlArray).subscribe(
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

  getBillDetail(id) {
    this.billService.getBillDetail(id).subscribe(
      (data) => {
        if (data && data.results) {
          console.log(data.results);
          this.listOfBillDetail = data.results;
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

  
}
