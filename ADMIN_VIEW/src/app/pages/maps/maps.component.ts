import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { NzModalService } from 'ng-zorro-antd/modal';
import { NzNotificationService } from 'ng-zorro-antd/notification';
import { NzTableQueryParams } from 'ng-zorro-antd/table';
import { ModalManager } from 'ngb-modal';
import { Action } from 'src/app/commons/common';
import { Bill, ProductDetail } from 'src/app/models/type';
import { BillService } from 'src/app/services/bill.service';
import { ProductDetailService } from 'src/app/services/productDetail.service';
declare const google: any;

@Component({
  selector: 'app-maps',
  templateUrl: './maps.component.html',
  styleUrls: ['./maps.component.scss']
})
export class MapsComponent implements OnInit {
  searchForm!: FormGroup;
  isVisible = false;
  total = 1;
  loading = true;
  pageSize = 5;
  imageUrl: string = null;
  pageIndex = 1;
  listOfData: Bill[] = [];
  controlArray: Map<string, any> = new Map<string, any>();
  listOfBillDetail: any;
  billId: string;
  bill: Bill;
  Action = Action;
  action: Action;
  lstProductDetail: ProductDetail[];
  listOfProductDetail: ProductDetail[] = [];

  @ViewChild('myModal') myModal;
  private modalRef;
  constructor(private fb: FormBuilder, private notification: NzNotificationService,
    private modal: NzModalService, private modalService: ModalManager,
    private productDetailService: ProductDetailService,
    private billService: BillService) { }

  ngOnInit() {
    this.searchForm = this.fb.group({
      billID: [null],
      fromDate: [null],
      toDate: [null],
      priceFrom: [0],
      priceTo: [0],
      userName: [null],
    });
  }
  createNotification(type: string, title: string, message: string): void {
    this.notification.create(type, title, message);
  }

  search() {
    const billId = this.searchForm.controls.billID.value;
    const fromDate = this.searchForm.controls.fromDate.value;
    const toDate = this.searchForm.controls.toDate.value;
    const userName = this.searchForm.controls.userName.value;
    const priceFrom =
      this.searchForm.controls.priceFrom.value == 0
        ? null
        : this.searchForm.controls.priceFrom.value;
    const priceTo =
      this.searchForm.controls.priceTo.value == 0
        ? null
        : this.searchForm.controls.priceTo.value;
    this.controlArray.set('billID', billId);
    this.controlArray.set('fromDate', this.convertDate(fromDate));
    this.controlArray.set('toDate', this.convertDate(toDate));
    this.controlArray.set('userName', userName);
    this.controlArray.set('priceFrom', priceFrom);
    this.controlArray.set('priceTo', priceTo);
    console.log(this.controlArray);
    this.getBills(this.pageIndex, this.pageSize, null, null);
  }

  onQueryParamsChange(params: NzTableQueryParams): void {
    debugger;
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
    this.controlArray.set('pageIndex', pageIndex);
    this.controlArray.set('pageSize', pageSize);
    this.controlArray.set('sortField', sortField);
    this.controlArray.set('sortOrder', sortOrder);
    // get bill
    console.log(this.controlArray);
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

  convertDate(str) {
    if (str) {
      var date = new Date(str),
        mnth = ('0' + (date.getMonth() + 1)).slice(-2),
        day = ('0' + date.getDate()).slice(-2);
      return [date.getFullYear(), mnth, day].join('/');
    }
    return null;
  }


  showModal(id): void {
    this.isVisible = true;
    this.billId = id;
    this.getBillDetail(id);
  }

  getBillDetail(id) {
    this.billService.getBillDetail(id).subscribe(
      (data) => {
        if (data && data.results) {
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

  handleOk() {
    this.isVisible = false;
  }

  changeStatus(e, bill) {
    debugger;
    this.bill = bill;
    this.bill.status = e;
    this.bill.user = null;
    this.billService.saveBill(this.bill).subscribe(
      (data) => {
        this.createNotification('success', 'Thay đổi thành công!', '');
      },
      (error) => {
        this.createNotification(
          'error',
          'Có lỗi xảy ra!',
          'Vui lòng liên hệ quản trị viên.'
        );
      },
      () => {
        this.getBills(this.pageIndex, this.pageSize, null, null);
      }
    );
  }


  openModal(action) {
    // this.loadOption();
    this.getListProductDetail();
    this.action = action;
    this.modalRef = this.modalService.open(this.myModal, {
      size: "xl",
      modalClass: 'mymodal',
      hideCloseButton: false,
      centered: false,
      backdrop: true,
      animation: true,
      keyboard: false,
      closeOnOutsideClick: false,
      backdropClass: "modal-backdrop"
    })
  }

  getListProductDetail() {
    this.productDetailService.getListProductDetail().subscribe((response) => {
      this.lstProductDetail = response;
    })
  }

  plusProduct(item) {
    this.listOfProductDetail.forEach((element) => {
      // if (element.id == item.id &&  item.quanlityBuy < element.quantity ) {
      //   element.quanlityBuy += 1;
      // }
    });
    // this.updateCart();
  }

  minusProduct(item) {
    this.listOfProductDetail.forEach((element) => {
      // if (element.id == item.id && element.quantity > 1) {
      //   element.quanlityBuy -= 1;
      // }
    });
    // this.updateCart();
  }

  delProduct(id) {
    debugger;
    this.listOfProductDetail = this.listOfProductDetail.filter(
      (element) => element.id != id
    );
    // this.updateCart();
    // window.location.reload();
  }
}
