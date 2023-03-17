import { Component, OnInit } from '@angular/core';
import { NzNotificationService } from 'ng-zorro-antd/notification';
import { Product } from 'src/app/entity/Product';
import { PageProductDetail, ProductDetail } from 'src/app/models/type';
import { BrandService } from 'src/app/services/brand.service';
import { CategoryService } from 'src/app/services/category.service';
import { ProductService } from 'src/app/services/product.service';
import { ProductDetailService } from 'src/app/services/productDetail.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit {
  listOfData: ProductDetail[] = [];
  listOfDataSale: ProductDetail[] = [];
  pageSize = 8;
  pageIndex = 0;
  productDetail: ProductDetail = null;
  controlArray: Map<string, any> = new Map<string, any>();
  totalPrice: number = 0;
  cart: ProductDetail[] = [];
  compare: ProductDetail[] = [];
  lenthCompare: number;
  totalProduct: number;
  pageProductDetail = new PageProductDetail();

  constructor(
    private categoryService: CategoryService,
    private brandService: BrandService,
    private productService: ProductService,
    private notification: NzNotificationService,
    private productDetailService: ProductDetailService
  ) { }

  ngOnInit(): void {
    // declare cart
    //  debugger; 
    const cart = localStorage.getItem('cart') || '';
    if (cart) {
      this.cart = JSON.parse(cart);
      this.totalProduct = this.cart.length;
    }

    const compare = localStorage.getItem('compare') || '';
    if (compare) {
      this.compare = JSON.parse(compare);
      this.lenthCompare = this.compare.length;
      console.log(this.lenthCompare);
    }
    this.getProducts(this.pageIndex, this.pageSize, 'productID', 'descend')
    this.getProductSales(this.pageIndex, this.pageSize, 'productID', 'ascend');
  }
  createNotification(type: string, title: string, message: string): void {
    this.notification.create(type, title, message);
  }
  getProducts(
    pageIndex: number,
    pageSize: number,
    sortField: string | null,
    sortOrder: string | null
  ) {
    this.controlArray.set('pageIndex', pageIndex);
    this.controlArray.set('pageSize', pageSize);
    // this.controlArray.set('sortField', sortField);
    // this.controlArray.set('sortOrder', sortOrder);
    // get product
    this.productDetailService.getPageProductDetail(this.controlArray).subscribe(
      (data) => {
        if (data && data) {
          this.pageProductDetail = data;
          this.productDetail = this.pageProductDetail.content[0];
          // console.log(this.p);
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


  getProductSales(
    pageIndex: number,
    pageSize: number,
    sortField: string | null,
    sortOrder: string | null
  ) {
    this.controlArray.set('pageIndex', pageIndex);
    this.controlArray.set('pageSize', pageSize);
    // this.controlArray.set('sortField', sortField);
    // this.controlArray.set('sortOrder', sortOrder);
    // get product
    this.productDetailService.getPageProductDetail(this.controlArray).subscribe(
      (data) => {
        if (data && data) {
          this.listOfDataSale = data.content;
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

  addToCart(product: ProductDetail) {
    debugger
    let duplicate = false;
    this.cart.forEach((ele) => {
      if (ele.id == product.id) {
        duplicate = true;
      }
    });
    // if no => add item
    if (!duplicate) {
      debugger;
      product.quanlityBuy = 1;

      this.cart.push(product);
      this.updateCart();
      this.createNotification(
        'success',
        'Sản phẩm đã thêm vào giỏ hàng',
        ''
      );
      window.location.reload();
      // window.location.href = '/mycart'
    }
    else {
      this.cart.forEach((ele) => {
        if (ele.id == product.id) {
          if(ele.quanlityBuy == ele.quantity){
            this.createNotification(
              'warning',
              'bạn đã thêm tối đa sản phẩm',
              ''
            );
          }else{
            ele.quanlityBuy = ele.quanlityBuy + 1;
            this.createNotification(
              'success',
              'Sản phẩm đã thêm vào giỏ hàng',
              ''
            );
            this.updateCart();
            window.location.reload();
          }
        }
      });
    }
  }


  addToCompare(product: ProductDetail) {
    debugger;
    this.lenthCompare = this.compare.length;
    let duplicate = false;
    this.compare.forEach((ele) => {
      if (ele.id == product.id) {
        duplicate = true;
      }
    });
    // if no => add item
    if (!duplicate) {
      if (this.lenthCompare > 1) {
        this.createNotification(
          'info',
          'vui lòng xóa bớt sản phẩm để so sánh',
          ''
        );
        return;
      } else {
        this.productDetailService.getProductDetailById(product.id).subscribe((data) => {
          debugger;
          this.compare.push(data.result);
          this.updateCompare();
          this.createNotification(
            'success',
            'Sản phẩm đã thêm vào so sánh',
            ''
          );
          window.location.reload();
        })
      }
    } else {
      this.createNotification(
        'info',
        'Sản phẩm đã có trong so sánh',
        ''
      );
    }
  }

  updateCart() {
    this.totalPrice = 0;
    this.cart.forEach((ele) => {
      this.totalPrice += ele.quanlityBuy * ele.productPrice;
    });
    localStorage.setItem('total', this.totalPrice.toString());
    localStorage.setItem('cart', JSON.stringify(this.cart));
  }

  updateCompare() {
    localStorage.setItem('compare', JSON.stringify(this.compare));
  }
}
