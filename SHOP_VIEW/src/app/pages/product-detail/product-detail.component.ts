import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { NzModalService } from 'ng-zorro-antd/modal';
import { NzNotificationService } from 'ng-zorro-antd/notification';
import { Category } from 'src/app/entity/Category';
import { Product } from 'src/app/entity/Product';
import { Review } from 'src/app/entity/Review.model';
import { ProductDetail, User } from 'src/app/models/type';
import { BrandService } from 'src/app/services/brand.service';
import { CategoryService } from 'src/app/services/category.service';
import { ProductService } from 'src/app/services/product.service';
import { ProductDetailService } from 'src/app/services/productDetail.service';
import { ReviewService } from 'src/app/services/review.service';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.css'],
})
export class ProductDetailComponent implements OnInit {
  productId: String;
  listOfCategory: Category[] = [];
  listOfData: Product[] = [];
  controlArray: Map<string, any> = new Map<string, any>();
  listOfData1: Product[] = [];
  controlArray1: Map<string, any> = new Map<string, any>();
  controlArray2: Map<string, any> = new Map<string, any>();
  product = new ProductDetail();
  currentUser = new User();
  productDetail = new ProductDetail();

  totalPrice: number = 0;
  cart: ProductDetail[] = [];

  listOfData2: Review[] = [];
  totalReview: number = 0;
  pageSize = 5;
  pageIndex = 1;
  validateForm!: FormGroup;
  constructor(
    route: ActivatedRoute,
    private activatedRoute: ActivatedRoute,
    private categoryService: CategoryService,
    private brandService: BrandService,
    private productService: ProductService,
    private notification: NzNotificationService,
    private reviewService: ReviewService,
    private fb: FormBuilder,
    private modal: NzModalService,
    private productDetailService: ProductDetailService
  ) {
    route.params.subscribe((val) => {
      this.productId = this.activatedRoute.snapshot.params.id;
    });
  }

  ngOnInit(): void {
    // declare cart 
    this.validateForm = this.fb.group({
      reviewStar: [0, [Validators.required]],
      reviewMessage: [null, [Validators.required]],
    });
    const cart = localStorage.getItem('cart') || '';
    if (cart) {
      this.cart = JSON.parse(cart);
    }

    // this.getCategories();
    console.log(this.productId);
    this.getProductDetail(this.productId);
    this.controlArray2.set('pageIndex', this.pageIndex);
    this.controlArray2.set('pageSize', this.pageSize);
    this.controlArray2.set('sortField', 'id');
    this.controlArray2.set('sortOrder', 'ascend');
    this.controlArray2.set('productId', this.productId);
    this.controlArray2.set('status', 1);
    this.getReviews(this.controlArray2)

  }
  createNotification(type: string, title: string, message: string): void {
    this.notification.create(type, title, message);
  }
  
  getCategories() {
    this.categoryService.getAllCategory().subscribe(
      (data) => {
        if (data && data.results) {
          this.listOfCategory = data.results;
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

  getProductsByCategory(
    pageIndex: number,
    pageSize: number,
    sortField: string | null,
    sortOrder: string | null,
    category: number
  ) {
    this.controlArray1.set('pageIndex', pageIndex);
    this.controlArray1.set('pageSize', pageSize);
    this.controlArray1.set('sortField', sortField);
    this.controlArray1.set('sortOrder', sortOrder);
    // this.controlArray1.set('categoryID', category);
    // get product
    this.productService.getProducts(this.controlArray1).subscribe(
      (data) => {
        if (data && data.results) {
          this.listOfData = data.results;
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
  getProductDetail(id) {
    this.productDetailService.getProductDetailById(id).subscribe(
      (data) => {
        if (data) {
          this.productDetail = data.result;
          this.productDetail.quantity = this.productDetail?.listImei.filter(e =>{return e.status == 1}).length
          console.log(this.productDetail);
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
        // this.getProductsByCategory(1,6,'productID', 'descend',this.product.category.categoryID);
      }
    );
  }

  addToCart(product: ProductDetail) {
    let duplicate = false;
    this.cart.forEach((ele) => {
      if (ele.id == product.id) {
        duplicate = true;
      }
    });
    // if no => add item
    if (!duplicate) {
      product.quanlityBuy = 1;
      this.cart.push(product);
      this.updateCart();
      this.createNotification(
        'success',
        'Sản phẩm đã thêm vào giỏ hàng',
        ''
      );
    } else {
      this.createNotification(
        'info',
        'Sản phẩm đã có trong giỏ hàng',
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

  onPageIndexChange(value: number): void {
    this.controlArray.set('pageIndex', value);
    this.getReviews(this.controlArray);
  }
  onPageSizeChange(value: number): void {
    this.controlArray.set('pageSize', value);
    this.getReviews(this.controlArray);
  }

  getReviews(param: any) {
    this.reviewService.getReviews(this.controlArray2).subscribe(
      (data) => {
        if (data && data.results) {
          this.listOfData2 = data.results;
          this.totalReview = data.rowCount;
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

  submitForm(review: Review) {
    debugger;
    const userAuth = localStorage.getItem('Authorization') || '';
    const currentUser = localStorage.getItem('currentUser') || '';
    // const userId = localStorage.getItem('userId') || '';
    this.currentUser =  JSON.parse(currentUser);
    if (userAuth) {
      review.productDetail = this.productDetail;
      review.reviewName = this.currentUser.username;
      review.userId = this.currentUser.userID;
      this.reviewService.saveReview(review).subscribe(
        (data) => {
          if (data && data.result) {
            window.location.href = `/product-detail/` + this.productId;
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

    } else {
      this.modal.confirm({
        nzTitle: 'Bạn cần đăng nhập để đánh giá!',
        nzContent: '',
        nzOkText: 'Đăng nhập',
        nzOkType: 'primary',
        nzOkDanger: false,
        nzOnOk: () => {
          window.location.href = `/login/1/` + this.productId;
        },
        nzCancelText: 'Ở lại',
        nzOnCancel: () => console.log('Cancel'),
      });
    }
  }
}
