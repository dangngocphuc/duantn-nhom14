import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { NzNotificationService } from 'ng-zorro-antd/notification';
import { ICreateOrderRequest, IPayPalConfig } from 'ngx-paypal';
// import { Bill } from 'src/app/entity/Bill';
import { Bill, BillDetail, ProductDetail, User } from 'src/app/models/type';
import { BillService } from 'src/app/services/bill.service';
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

  constructor(
    private router: Router,
    private userService: UserService,
    private notification: NzNotificationService,
    private fb: FormBuilder,
    private billService: BillService
  ) { }

  createNotification(type: string, title: string, message: string): void {
    this.notification.create(type, title, message);
  }

  ngOnInit(): void {
    debugger;
    const cart = localStorage.getItem('cart') || '';
    // this.userId = localStorage.getItem('userId') || '';
    if (cart) {
      this.cart = JSON.parse(cart);
    }
    this.cartString = localStorage.getItem('cart') || '';
    const user = localStorage.getItem('currentUser') || '';
    if (user) {
      this.currentUser = JSON.parse(user);
    }
    // this.user 
    this.total = Number.parseInt(localStorage.getItem('total') || '');
    if (!this.cart) {
      this.router.navigateByUrl('/home');
    }
    this.validateForm = this.fb.group({
      userName: [null, [Validators.required]],
      phone: [null, [Validators.required]],
      address: [null, [Validators.required]],
      payment: ['Live', [Validators.required]],
    });
    if (this.currentUser) {
      this.getUser(this.currentUser);
    }
    this.initConfig();
  }
  submitForm(): void {
    debugger;
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

    // this.user.

    if (this.userName && this.phone && this.address && this.payment) {
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
      this.saveBill(this.bill);
    }
  }

  getUser(currentUser) {
    this.validateForm.controls['userName'].setValue(this.currentUser.username);
    this.validateForm.controls['phone'].setValue(this.currentUser.userPhone);
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
      if(data.code == '00'){
        // this.router.navigate([data.data]);
        window.open(data.data)
      }
      // this.router.navigate(['/pay-success']);
    })
  }
}
