import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { NzModalService } from 'ng-zorro-antd/modal';
import { NzNotificationService } from 'ng-zorro-antd/notification';
import { NzTableQueryParams } from 'ng-zorro-antd/table';
import { ModalManager } from 'ngb-modal';
import { Action } from 'src/app/commons/common';
import { Bill, BillDetail, District, PagesRequest, ProductDetail, Province, Ward } from 'src/app/models/type';
import { BillService } from 'src/app/services/bill.service';
import { ProductDetailService } from 'src/app/services/productDetail.service';
import { ImeiService } from 'src/app/services/imei.service';
import Swal from 'sweetalert2';
import { catchError, concat, debounceTime, distinctUntilChanged, map, Observable, of, Subject, switchMap, tap } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { GhnService } from 'src/app/services/ghn.service';
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
  billCode: String;
  bill: Bill;
  Action = Action;
  action: Action;
  lstProductDetail: ProductDetail[];
  listOfProductDetail: ProductDetail[] = [];
  productDetail = new ProductDetail();
  totalPrice = 0;
  newBill = new Bill();

  lstImei: Observable<any[]>;
  loadlstImei = false;
  textInput_imei$ = new Subject<string>();

  disabled;

  listProvince: Province[];
  listDistrict: District[];
  listWard: Ward[];
  discountPrice = 0;
  sum = 0;
  @ViewChild('myModal') myModal;

  @ViewChild('Modal') Modal;

  private modalRef;
  constructor(private fb: FormBuilder, private notification: NzNotificationService,
    private imeiService: ImeiService, private ghnService: GhnService,
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

  getListDistrict(e) {
    if (e) {
      this.ghnService.getListDistrict(e).subscribe((data) => {
        if (data.code == 200) {
          this.listDistrict = data.data;
        }
      })
    }

  }
  getListWard(e) {
    if (e) {
      this.ghnService.getListWard(e).subscribe((data) => {
        if (data.code == 200) {
          this.listWard = data.data;
        }
      })
    }
  }
  getListProvince() {
    this.ghnService.getListProvince().subscribe((data) => {
      console.log(data);
      if (data.code == 200) {
        this.listProvince = data.data;
      }
    })
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
    // debugger;
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


  showModal(data): void {
    Swal.fire({
      title: "Đang xử lý!",
      html: '',
      didOpen: () => {
        Swal.showLoading()
      },
    })
    this.billId = data.id;
    this.billCode = data.billCode;
    // this.getBillDetail(data.id);
    console.log(data);
    this.getListProvince();
    this.getListDistrict(data.provinceID);
    this.getListWard(data.districtID);
    this.billService.getBillById(data.id).subscribe((data) => {
      this.bill = data;
      if(this.bill.promotion){
       
        this.bill.listBillDetail.forEach((e)=>{
          this.sum=this.sum+e.price;
        })
        if (this.bill.promotion.type == 0) {
          this.discountPrice = (this.sum * this.bill.promotion.value * 0.01)
        }
        else {
          this.discountPrice = this.bill.promotion.value;
        }  
      }
     
      this.bill.listBillDetail.forEach((e) => {
        this.loadImei(e);
      })
      if (!(this.bill.status == 'Processed')) {
        this.disabled = true;
      } else {
        this.disabled = false;
      }
      Swal.close();
      this.isVisible = true;
    })
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
    this.bill = new Bill();
    this.isVisible = false;
  }

  changeStatus(e, bill) {
    debugger;
    let checkStatus = true;
    if (bill.status == 'Processed') {
      if (e == 'Waiting') {
        this.createNotification(
          'warning',
          'Trạng thái không hợp lệ!',
          'Vui lòng thử lại.'
        );
        checkStatus = false;
      }
    }
    if (bill.status == 'Delivery') {
      if (e == 'Waiting' || e == 'Processed') {
        this.createNotification(
          'warning',
          'Trạng thái không hợp lệ!',
          'Vui lòng thử lại.'
        );
        checkStatus = false;
      }
    }
    if (bill.status == 'Đã giao hàng') {
      if (e == 'Waiting' || e == 'Processed' || e == 'Delivery') {
        this.createNotification(
          'warning',
          'Trạng thái không hợp lệ!',
          'Vui lòng thử lại.'
        );
        checkStatus = false;
      }
    }
    if (checkStatus) {
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
    this.search();
  }


  openMyModal(modal?, action?) {
    // this.loadOption();
    this.getListProductDetail();
    this.action = action;
    this.modalRef = this.modalService.open(modal, {
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

  openModal(data) {
    // this.loadOption();

    // this.getListProductDetail();
    this.billId = data.id;
    this.billCode = data.billCode;
    // this.getBillDetail(data.id);
    this.billService.getBillById(data.id).subscribe((data) => {
      console.log(data);
      this.bill = data;
      this.bill.listBillDetail.forEach((e) => {
        this.loadImei(e);
      })
      if (!(this.bill.status == 'Process')) {
        this.disabled = true;
      } else {
        this.disabled = false;
      }
    })
    // this.action = action;
    this.modalRef = this.modalService.open(this.Modal, {
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
    debugger;
    this.listOfProductDetail.forEach((element) => {
      debugger;
      if (element.id == item.id && item.quanlityBuy < element.quantity) {
        element.quanlityBuy += 1;
      }
    });
    this.updateBill();
  }

  minusProduct(item) {
    this.listOfProductDetail.forEach((element) => {
      if (element.id == item.id && element.quantity > 1) {
        element.quanlityBuy -= 1;
      }
    });
    this.updateBill();
  }

  delProduct(id) {
    // debugger;
    this.listOfProductDetail = this.listOfProductDetail.filter(
      (element) => element.id != id
    );
    this.updateBill();
    // window.location.reload();
  }

  addProduct() {
    if (this.productDetail.id) {
      this.productDetail.quanlityBuy = 1;
      let duplicate = false;
      this.listOfProductDetail.forEach((e) => {
        if (e.id == this.productDetail.id) {
          duplicate = true;
        }
      })
      if (!duplicate) {
        // debugger;
        this.productDetail.quanlityBuy = 1;
        this.listOfProductDetail.push(this.productDetail);
        this.updateBill();
      } else {
        Swal.fire('', 'Sản phẩm đã có trong giỏ hàng', 'info');
      }
    }
  }
  updateBill() {
    this.totalPrice = 0;
    this.listOfProductDetail.forEach((element) => {
      this.totalPrice += element.quanlityBuy * element.productPrice;
      console.log(this.totalPrice);
      // this.salePrice += (element.productMarketprice - element.productPrice) * element.quantity;
    });
  }

  payment() {
    // console.log(this.newBill);
    debugger;
    let listBillDetailTemp = []
    this.listOfProductDetail.forEach((e) => {
      let billDetail = new BillDetail();
      // e.listImei = e.listImei.filter((e) => e.status == 1)
      billDetail.productDetail = e;
      billDetail.quantity = e.quanlityBuy;
      billDetail.price = e.productPrice;
      // billDetail.listImei = e.listImei.slice(0, e.quanlityBuy);
      listBillDetailTemp.push(billDetail);
    })
    this.newBill.total = this.totalPrice;
    this.newBill.listBillDetail = listBillDetailTemp;
    console.log(this.newBill);
    this.saveBill(this.newBill);
  }

  saveBill(bill) {
    this.billService.paymentBill(bill).subscribe(
      (data) => {
        if (data) {
          this.handleOk();
          window.location.reload();
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

  cancelBill(data) {
    this.modal.confirm({
      nzTitle: 'Bạn có chắc chắn muốn hủy đơn?',
      nzContent: '',
      nzOkText: 'Yes',
      nzOkType: 'primary',
      nzOkDanger: true,
      nzOnOk: () => {
        if (data.status === 'Delivered') {
          this.createNotification(
            'warning',
            'Đơn hàng đã giao không thể hủy!',
            ''
          );
        } else {
          this.billService.cancelBill(data.id).subscribe(
            (data) => {
              if (data && data.errorCode === '1002') {
                this.createNotification('success', 'Hủy thành công!', '');
              }
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
      },
      nzCancelText: 'No',
      nzOnCancel: () => console.log('Cancel'),
    });
  }


  loadImei(e) {
    console.log(e.productDetail.id);
    this.lstImei = concat(
      this.imeiService.getListImeiByProductDetail(new PagesRequest(0, 10), { imei: null, productId: e.productDetail.id }), // default items
      this.textInput_imei$.pipe(
        debounceTime(500),
        distinctUntilChanged(),
        tap(() => (this.loadlstImei = true)),
        switchMap((term) =>
          this.imeiService
            .getListImeiByProductDetail(new PagesRequest(0, 10), { imei: term, productId: e.productDetail.id })
            .pipe(
              catchError(() => of([])), // empty list on error
              tap(() => (this.loadlstImei = false))
            )
        )
      )
    );
    this.lstImei.subscribe(data => {
      e.listImeiValue = data;
    })
  }

  handle() {
    console.log(this.bill);
    if (this.bill.listBillDetail) {
      this.bill.listBillDetail.forEach((e) => {
        if (e.listImei.length != e.quantity) {
          this.createNotification(
            'warning',
            'Lưu thất bại!',
            'Nhập Imei bằng với số lượng sản phẩm'
          );
        }
        else {
          this.bill.user = null;
          this.saveBill(this.bill);
        }
      })
    }
  }

  handlePrint(): void {
    let printContents, popupWin;
    printContents = document.getElementById('print-section').innerHTML;
    popupWin = window.open('', '_blank');
    popupWin.document.open();
    popupWin.document.write(`
      <html>
        <head>
          <title>Hóa đơn Lapware</title>
        </head>
        <style>
          .ant-table table {
            width: 100%;
            text-align: left;
            border-radius: 2px 2px 0 0;
            border-collapse: separate;
            border-spacing: 0;
          }
        </style>
      <body onload="window.print();window.close()">${printContents}</body>
      </html>`
    );
    popupWin.document.close();
  }

  checkButton() {
    if (!(this.bill.status == 'Processed')) {
      return false;
    }
    return true;
  }
}
