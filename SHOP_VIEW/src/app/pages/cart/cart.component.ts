import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NzModalService } from 'ng-zorro-antd/modal';
import { NzNotificationService } from 'ng-zorro-antd/notification';
import { Product } from 'src/app/entity/Product';
import { ProductDetail, Promotion } from 'src/app/models/type';
import { PromotionService } from 'src/app/services/promotion.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css'],
})
export class CartComponent implements OnInit {
  totalPrice = 0;
  salePrice = 0;
  discountPrice = 0;
  listOfProduct: ProductDetail[] = [];
  code: any;
  codeTemp: any;
  promotion = new Promotion();
  sum = 0;
  constructor(
    private notification: NzNotificationService, private promotionService: PromotionService,
    private modal: NzModalService,
    private router: Router
  ) { }
  createNotification(type: string, title: string, message: string): void {
    this.notification.create(type, title, message);
  }

  ngOnInit(): void {
    const cart = localStorage.getItem('cart') || '';
    if (cart) {
      // debugger;
      this.listOfProduct = JSON.parse(cart);
      // console.log(this.listOfProduct);
      this.listOfProduct.forEach((element) => {
        this.totalPrice += element.quanlityBuy * element.productPrice;
        this.salePrice += (element.productMarketprice - element.productPrice) * element.quantity;
      });
      this.sum = this.totalPrice;
    }
  }

  plusProduct(item) {
    debugger;
    console.log(item);
    this.listOfProduct.forEach((element) => {
      if (element.id == item.id && item.quanlityBuy < element.quantity) {
        element.quanlityBuy += 1;
      }
    });
    this.updateCart();
  }

  minusProduct(item) {
    this.listOfProduct.forEach((element) => {
      if (element.id == item.id && element.quanlityBuy > 1) {
        element.quanlityBuy -= 1;
      }
    });
    this.updateCart();
  }

  delProduct(id) {
    debugger;
    this.listOfProduct = this.listOfProduct.filter(
      (element) => element.id != id
    );
    this.updateCart();
    window.location.reload();
  }

  updateCart() {
    this.totalPrice = 0;
    this.sum = 0
    this.listOfProduct.forEach((element) => {
      this.totalPrice += element.quanlityBuy * element.productPrice;
      this.sum += element.quanlityBuy * element.productPrice;
      this.salePrice += (element.productMarketprice - element.productPrice) * element.quanlityBuy;
    });
    if (this.discountPrice) {
      this.totalPrice = this.sum - this.discountPrice;
    }
    localStorage.setItem('total', this.totalPrice.toString());
    localStorage.setItem('cart', JSON.stringify(this.listOfProduct));
    // window.location.reload();
  }

  payment() {
    const userAuth = localStorage.getItem('Authorization') || '';
    debugger;
    if (userAuth) {
      this.updateCart();
      this.router.navigate(['/payment']);
    } else {
      this.modal.confirm({
        nzTitle: 'Bạn cần đăng nhập để thanh toán!',
        nzContent: '',
        nzOkText: 'Đăng nhập',
        nzOkType: 'primary',
        nzOkDanger: false,
        nzOnOk: () => {
          this.router.navigate(['/login/1']);
          this.updateCart();
        },
        nzCancelText: 'Ở lại',
        nzOnCancel: () => console.log('Cancel'),
      });
    }
  }

  changeQuanlity(e, index, item) {
    // console.log(index);
    console.log(item);
    console.log(e?.target?.value);

    if (e?.target?.value > item.quantity) {
      item.quanlityBuy = item.quantity;
    } else if (e?.target?.value < 0) {
      item.quanlityBuy = 1;
    }
    else {
      item.quanlityBuy = e?.target?.value
    }

    this.listOfProduct.forEach((element, i) => {
      // console.log(i);
      // if (index == i) {
      //   if (e?.target?.value > element.quantity) {
      //     element.quanlityBuy = element.quantity
      //   }
      //   else if (e?.target?.value < 0) {
      //     element.quanlityBuy = 1
      //   }
      //   else {
      //     element.quanlityBuy = e?.target?.value
      //   }
      // }
    })
    this.updateCart();
  }

  addVorcher() {
    console.log(this.code);
    if (this.code) {

      if (this.code == this.promotion.code) {
        Swal.fire('', 'Mã không hợp lệ', 'error');
        // this.code=null;
      } else {
        this.promotionService.getPromotionByCode(this.code).subscribe((data) => {
          if (data) {
            // console.log(data);
            debugger;
            this.promotion = data;
            let date = new Date();
            let dateF = new Date(this.promotion.dateFrom);
            let dateT = new Date(this.promotion.dateTo);
            if (dateF.getTime() <= date.getTime() && date.getTime() <= dateT.getTime()) {
              if (this.promotion.count >= this.promotion.quantity) {
                Swal.fire('', 'Mã áp dụng đã hết', 'error')
              } else {
                this.totalPrice = 0;
                this.sum = 0
                this.listOfProduct.forEach((element) => {
                  this.totalPrice += element.quanlityBuy * element.productPrice;
                  this.sum += element.quanlityBuy * element.productPrice;
                  this.salePrice += (element.productMarketprice - element.productPrice) * element.quanlityBuy;
                });
                if (this.promotion.type == 0) {
                  this.discountPrice = (this.totalPrice * this.promotion.value * 0.01)
                  this.totalPrice = this.totalPrice - this.discountPrice;
                }
                else {
                  this.discountPrice = this.promotion.value;
                  this.totalPrice = this.totalPrice - this.promotion.value;
                }
                localStorage.setItem('promotion', JSON.stringify(this.promotion));
                localStorage.setItem('total', this.totalPrice.toString());
              }
            }
            else {
              Swal.fire('', 'Mã đã hết thời gian áp dụng', 'error');
              this.code=null;
            }
          } else {
            Swal.fire('', 'Mã không hợp lệ', 'error');
            this.code=null;
          }
        },
          (error) => {
            Swal.fire('', error, 'error')
          })
      }
    } else {
      Swal.fire('', 'vui lòng nhập mã khuyến mại', 'error');
      this.code=null;
    }
  }

  
}
