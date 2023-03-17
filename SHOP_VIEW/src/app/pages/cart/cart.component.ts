import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NzModalService } from 'ng-zorro-antd/modal';
import { NzNotificationService } from 'ng-zorro-antd/notification';
import { Product } from 'src/app/entity/Product';
import { ProductDetail } from 'src/app/models/type';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css'],
})
export class CartComponent implements OnInit {
  totalPrice = 0;
  salePrice = 0;
  listOfProduct: ProductDetail[] = [];
  constructor(
    private notification: NzNotificationService,
    private modal: NzModalService,
    private router: Router
  ) {}
  createNotification(type: string, title: string, message: string): void {
    this.notification.create(type, title, message);
  }

  ngOnInit(): void {
    const cart = localStorage.getItem('cart') || '';
    if (cart) {
      // debugger;
      this.listOfProduct = JSON.parse(cart);
      console.log(this.listOfProduct);
      this.listOfProduct.forEach((element) => {
        this.totalPrice += element.quanlityBuy * element.productPrice;
        this.salePrice += (element.productMarketprice - element.productPrice) * element.quantity;
      });
    }
  }

  plusProduct(item) {
    debugger;
    console.log(item);
    this.listOfProduct.forEach((element) => {
      if (element.id == item.id &&  item.quanlityBuy < element.quantity ) {
        element.quanlityBuy += 1;
      }
    });
    this.updateCart();
  }

  minusProduct(item) {
    this.listOfProduct.forEach((element) => {
      if (element.id == item.id && element.quantity > 1) {
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
    this.listOfProduct.forEach((element) => {
      this.totalPrice += element.quanlityBuy * element.productPrice;
      this.salePrice += (element.productMarketprice - element.productPrice) * element.quanlityBuy;
    });
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

  changeQuanlity(e,index){
    console.log(index);
    console.log(e?.target?.value);

    this.listOfProduct.forEach((element,i)=>{
      // console.log(i);
      if(index==i){
        if(e?.target?.value > element.quantity){
          element.quanlityBuy = element.quantity
        }
        else if(e?.target?.value < 0){
          element.quanlityBuy = 1
        }
        else{
          element.quanlityBuy = e?.target?.value
        }
      }
    })
    this.updateCart();
  }
}
