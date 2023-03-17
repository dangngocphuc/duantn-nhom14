import { HttpClient } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { NzNotificationService } from 'ng-zorro-antd/notification';
import { ModalManager } from 'ngb-modal';
import { ICreateOrderRequest, IPayPalConfig } from 'ngx-paypal';
import { catchError, map, Observable, of } from 'rxjs';
// import { Bill } from 'src/app/entity/Bill';
import { Address, Bill, BillDetail, District, ProductDetail, Province, RequestFeeSevice, RequestLeadTime, RequestSevice, ResponseFeeSevice, Service, User, Ward } from 'src/app/models/type';
import { BillService } from 'src/app/services/bill.service';
import { GhnService } from 'src/app/services/ghn.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-payment',
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.css'],
})
export class PaymentComponent implements OnInit {
  // user: User = null;
  // bill = new Bill();
  bill = new Bill()
  userName: string;
  phone: string;
  address: string;
  userId: string;
  payment: string;
  validateForm!: FormGroup;
  total: number;
  cart: ProductDetail[];
  cartString: String;
  currentUser = new User();
  public payPalConfig?: IPayPalConfig;
  listProvince: Province[];
  provinceId: number;
  districtId: number;

  listDistrict: District[];
  district = new District();
  listWard: Ward[];
  wardCode: String;

  requestService = new RequestSevice();
  serviceId: number;
  listService: Service[];

  requestFeeSevice = new RequestFeeSevice();
  responseFeeSevice = new ResponseFeeSevice();

  requestLeadTime = new RequestLeadTime();
  leadTime: any;

  addresss = new Address();
  // check;
  // randomUserUrl = 'https://online-gateway.ghn.vn/shiip/public-api/master-data/province';
  // optionList: string[] = [];
  // selectedUser = null;
  // isLoading = false;
  // /* eslint-disable @typescript-eslint/no-explicit-any */
  // getRandomNameList: Observable<string[]> = this.http
  //   .get(`${this.randomUserUrl}`)
  //   .pipe(
  //     catchError(() => of({ results: [] })),
  //     map((res: any) => res.results)
  //   )
  //   .pipe(map((list: any) => list.map((item: any) => `${item.name.first}`)));

  // loadMore(): void {
  //   this.isLoading = true;
  //   this.getRandomNameList.subscribe(data => {
  //     this.isLoading = false;
  //     this.optionList = [...this.optionList, ...data];
  //   });
  // }
  @ViewChild('ListAddress') myModal;
  @ViewChild('Address') myModal1;
  private modalRef;

  constructor(
    private router: Router, private http: HttpClient, private ghnService: GhnService,
    private userService: UserService, private notification: NzNotificationService, private modalService: ModalManager,
    private fb: FormBuilder, private billService: BillService
  ) { }

  createNotification(type: string, title: string, message: string): void {
    this.notification.create(type, title, message);
  }

  getListDistrict() {
    // console.log(this.provinceId);
    this.districtId = null;
    this.wardCode = null;
    this.serviceId = null;
    this.responseFeeSevice = null;
    this.ghnService.getListDistrict(this.provinceId).subscribe((data) => {
      // console.log(data);
      if (data.code == 200) {
        this.listDistrict = data.data;
        // console.log(data);
      }
    })

  }

  getListWard() {
    // console.log(this.districtId);
    // this.wardCode = null;
    this.serviceId = null;
    this.responseFeeSevice = null;
    if (this.districtId) {
      this.ghnService.getListWard(this.districtId).subscribe((data) => {
        // console.log(data);
        if (data.code == 200) {
          this.listWard = data.data;
          // console.log(data);
        }
      })
      this.getService();
    } else {
      this.wardCode = null;
    }
  }

  getService() {
    this.requestService.shop_id = 122071;
    this.requestService.from_district = 1493;
    this.requestService.to_district = this.districtId;
    this.ghnService.getService(this.requestService).subscribe((data) => {
      if (data.code == 200) {
        this.listService = data.data;
      }
    });
  }
  getTime() {
    this.requestService.shop_id = 122071;
    this.requestService.from_district = 1493;
    this.requestService.to_district = this.districtId;

  }

  getFeeService() {
    this.requestFeeSevice.service_id = this.serviceId;
    this.requestFeeSevice.from_district_id = 1493;
    this.requestFeeSevice.to_district_id = this.districtId;
    this.requestFeeSevice.to_ward_code = Number(this.wardCode);
    this.requestFeeSevice.insurance_value = this.total;
    // console.log(this.requestFeeSevice);
    this.ghnService.getFeeService(this.requestFeeSevice).subscribe((data) => {
      if (data.code = 200) {
        this.responseFeeSevice = data.data;
      }
    })

    this.requestLeadTime.service_id = this.serviceId;
    this.requestLeadTime.from_district_id = 1493;
    this.requestLeadTime.from_ward_code = "1A0709";
    this.requestLeadTime.to_district_id = this.districtId;
    this.requestLeadTime.to_ward_code = this.wardCode;
    // console.log(this.requestLeadTime);
    this.ghnService.getLeadTime(this.requestLeadTime).subscribe((data) => {
      if (data.code == 200) {
        // console.log(data);
        if (data.code == 200) {
          this.leadTime = 1678751999;
          // console.log(new Date(this.leadTime));
          var d = new Date();
          this.leadTime = this.leadTime + d.getTime();

        }
        // this.listService = data.data;
      }
    });
  }

  ngOnInit(): void {
    this.ghnService.getListProvince().subscribe((data) => {
      console.log(data);
      if (data.code == 200) {
        this.listProvince = data.data;
      }
    })
    // debugger;
    // this.loadMore();
    const cart = localStorage.getItem('cart') || '';
    // this.userId = localStorage.getItem('userId') || '';
    if (cart) {
      this.cart = JSON.parse(cart);
    }
    this.cartString = localStorage.getItem('cart') || '';
    const user = localStorage.getItem('currentUser') || '';
    if (user) {
      this.currentUser = JSON.parse(user);
      this.userService.getUser(this.currentUser.userID).subscribe((data) => {
        console.log(data.result);
        this.currentUser = data.result;
        console.log(this.currentUser);
        if (this.currentUser) {
          this.getUser(this.currentUser);

        }
      })
    }
    // this.user 
    this.total = Number.parseInt(localStorage.getItem('total') || '');
    if (!this.cart) {
      this.router.navigateByUrl('/home');
    }
    this.validateForm = this.fb.group({
      userName: [null, [Validators.required]],
      phone: [null, [Validators.required]],
      address: [null,],
      payment: ['Live', [Validators.required]],
      specificAddress: [],
      province: [],
      district: [],
      ward: [],
      service: [],
    });

    this.initConfig();
    if (this.districtId) {
      this.requestService.shop_id = 122071;
      this.requestService.from_district = 1493;
      this.requestService.to_district = this.districtId;
      this.ghnService.getService(this.requestService).subscribe((data) => {
        if (data.code == 200) {
          this.listService = data.data;
        }
      });
    }
  }
  submitForm(): void {
    for (const i in this.validateForm.controls) {
      if (this.validateForm.controls.hasOwnProperty(i)) {
        this.validateForm.controls[i].markAsDirty();
        this.validateForm.controls[i].updateValueAndValidity();
      }
    }
    this.userName = this.validateForm.controls.userName.value;
    this.phone = this.validateForm.controls.phone.value;
    this.address = this.validateForm.controls.address.value;
    this.payment = this.validateForm.controls.payment.value;
    if (this.payment == 'Live') {
      if (this.userName && this.phone && this.payment) {
        this.bill.name = this.userName;
        this.bill.phone = this.phone;
        this.bill.address = this.address;
        this.bill.payment = this.payment;
        this.bill.total = this.total;
        this.bill.user = this.currentUser;
        let listBillDetailTemp = []
        this.cart.forEach((e) => {
          let billDetail = new BillDetail();
          // e.listImei = e.listImei.filter((e) => e.status == 1)
          billDetail.productDetail = e;
          billDetail.quantity = e.quanlityBuy;
          billDetail.price = e.productPrice;
          // billDetail.listImei = e.listImei.slice(0, e.quanlityBuy);
          listBillDetailTemp.push(billDetail);
        })
        this.bill.listBillDetail = listBillDetailTemp;
        console.log(this.bill);
        this.bill.products = this.cartString;
        this.saveBill(this.bill);
      }
    } else {
      this.paymentVnpay();
    }
    // this.user.
  }

  getUser(currentUser) {
    // debugger;
    this.validateForm.controls['userName'].setValue(currentUser.username);
    this.validateForm.controls['phone'].setValue(currentUser.phoneNumber);
    currentUser.listAddress.forEach((e) => {
      // debugger;
      if (e.defaults == 1) {
        console.log(e);
        this.provinceId = e.provinceID;
        this.getListDistrict();
        this.districtId = e.districtID;
        this.wardCode = e.wardCode;
        this.getListWard();
        this.validateForm.controls['specificAddress'].setValue(e.specificAddress);
        // this.validateForm.controls['province'].setValue();
        // this.validateForm.controls['ward'].setValue(e.wardCode);
      }
    })
  }


  saveBill(bill) {
    this.billService.saveBill(bill).subscribe(
      (data) => {
        if (data) {
          this.router.navigate(['/pay-success']);
          localStorage.removeItem('cart');
          localStorage.removeItem('total');
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

  paymentMethods(e) {
    console.log(e)
    console.log(this.payment);
    this.payment = this.validateForm.controls.payment.value;
  }

  private initConfig(): void {
    this.payPalConfig = {
      currency: 'EUR',
      clientId: 'AYHNIsRrwaChbHK2cJriKrw7vDzB0sBjT9eBZSJluYF9s7fuw94aXq9JkDlfJC04h0_yJOQ5Q04xMy5R',
      createOrderOnClient: (data) => <ICreateOrderRequest>{
        intent: 'CAPTURE',
        purchase_units: [{
          amount: {
            currency_code: 'EUR',
            value: '10',
            breakdown: {
              item_total: {
                currency_code: 'EUR',
                value: '10'
              }
            }
          },
          items: [{
            name: 'Enterprise Subscription',
            quantity: '1',
            category: 'DIGITAL_GOODS',
            unit_amount: {
              currency_code: 'EUR',
              value: '10',
            },
          }]
        }]
      },
      advanced: {
        commit: 'true'
      },
      style: {
        label: 'paypal',
        layout: 'vertical'
      },
      onApprove: (data, actions) => {
        console.log('onApprove - transaction was approved, but not authorized', data, actions);
        actions.order.get().then(details => {
          console.log('onApprove - you can get full order details inside onApprove: ', details);
        });

      },
      onClientAuthorization: (data) => {
        console.log('onClientAuthorization - you should probably inform your server about completed transaction at this point', data);
        // this.showSuccess = true;
      },
      onCancel: (data, actions) => {
        console.log('OnCancel', data, actions);
        // this.showCancel = true;

      },
      onError: err => {
        console.log('OnError', err);
        // this.showError = true;
      },
      onClick: (data, actions) => {
        console.log('onClick', data, actions);
        // this.resetStatus();
      }
    };
  }

  paymentVnpay() {
    this.bill.name = this.userName;
    this.bill.phone = this.phone;
    this.bill.address = this.address;
    this.bill.payment = this.payment;
    this.bill.total = this.total;
    this.bill.user = this.currentUser;
    let listBillDetailTemp = []
    this.cart.forEach((e) => {
      let billDetail = new BillDetail();
      e.listImei = e.listImei.filter((e) => e.status == 1)
      billDetail.productDetail = e;
      billDetail.quantity = e.quanlityBuy;
      billDetail.price = e.productPrice;
      billDetail.listImei = e.listImei.slice(0, e.quanlityBuy);
      listBillDetailTemp.push(billDetail);
    })
    this.bill.listBillDetail = listBillDetailTemp;
    console.log(this.bill);
    this.bill.products = this.cartString;
    this.billService.paymentBillByVnpay(this.bill).subscribe((data) => {
      console.log(data);
      if (data.code == '00') {
        // this.router.navigate([data.data]);
        window.open(data.data)
      }
      // this.router.navigate(['/pay-success']);
    })
  }

  async openModal() {
    if (this.currentUser?.listAddress) {
      for (let e of this.currentUser.listAddress) {
        try {
          const districtData = await this.ghnService.getListDistrict(e.provinceID).toPromise();
          e.listDistrict = districtData.data;
          const wardData = await this.ghnService.getListWard(e.districtID).toPromise();
          e.listWard = wardData.data;
          e.check = (e.defaults === 1) ? 1 : 0;
        } catch (error) {
          console.error(error);
        }
      }
      this.modalRef = this.modalService.open(this.myModal, {
        size: "lg",
        modalClass: 'mymodal',
        scrollable: true,
        hideCloseButton: false,
        centered: false,
        backdrop: true,
        animation: true,
        keyboard: false,
        closeOnOutsideClick: true,
        backdropClass: "modal-backdrop",
      });
    }
  }

  closeModal() {
    this.modalService.close(this.modalRef);
    //or this.modalRef.close();
  }

  confirm() {
    console.log(this.currentUser);
    this.currentUser.listAddress.forEach((e) => {
      e.user = null;
      e.check = null;
    });
    console.log(this.currentUser);
    this.userService.updateUser(this.currentUser).subscribe((res) => {
      console.log(res);
      if (res) {
        this.modalService.close(this.modalRef);
      }
    })
  }

  addAddress() {
    this.modalRef = this.modalService.open(this.myModal1, {
      size: "xl",
      modalClass: 'mymodal',
      hideCloseButton: false,
      centered: true,
      backdrop: true,
      animation: true,
      keyboard: false,
      closeOnOutsideClick: true,
      // backdropClass: "modal-backdrop"
    })
  }

  complete() {
    this.currentUser.listAddress.push(this.addresss);
    this.currentUser.listAddress.forEach((e) => {
      e.user = null;
    });
    console.log(this.currentUser);
    this.userService.updateUser(this.currentUser).subscribe((res) => {
      console.log(res);
      if (res) {
        this.modalService.close(this.modalRef);
      }
    })
  }

  updateSelectedOption(e) {
    console.log(e);
    // e.defaults = 1;
    this.currentUser.listAddress.forEach((item) => {
      if (e.id != item.id) {
        item.defaults = 0;
        item.check = 0;
      } else {
        item.defaults = 1;
        item.check = 1;
      }
    }
    );
    console.log(this.currentUser.listAddress);
  }
}
