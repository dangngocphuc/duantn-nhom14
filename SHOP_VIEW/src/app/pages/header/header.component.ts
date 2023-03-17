import { Component, OnInit } from '@angular/core';
import { NzModalService } from 'ng-zorro-antd/modal';
import { NzNotificationService } from 'ng-zorro-antd/notification';
import { catchError, concat, debounceTime, distinctUntilChanged, map, Observable, of, Subject, switchMap, tap } from 'rxjs';
import { Category } from 'src/app/entity/Category';
import { Product, ProductParam } from 'src/app/entity/Product';
import { CategoryService } from 'src/app/services/category.service';
import { ProductService } from 'src/app/services/product.service';
import { SocialAuthService, GoogleLoginProvider, SocialUser } from 'angularx-social-login';
import { CookieService } from 'ngx-cookie-service';
import { Brand, User } from 'src/app/models/type';
import { BrandService } from 'src/app/services/brand.service';
@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
})
export class HeaderComponent implements OnInit {
  listOfCategory: Category[] = [];
  userName: string;
  productName: string;

  listOfBrand: Brand[] = [];

  loadLstProducts = false;
  textInput_tenProduct$ = new Subject<string>();
  lstProducts: Observable<any[]>;

  productParam = new ProductParam();

  controlArray: Map<string, any> = new Map<string, any>();
  pageSize = 5;
  pageIndex = 1;
  cart: Product[] = [];
  totalProduct : number; 

  user = new User();

  constructor(
    private categoryService: CategoryService,
    private brandService: BrandService,
    private notification: NzNotificationService,
    private modal: NzModalService,
    private productService: ProductService,
    private socialAuthService: SocialAuthService,
    private cookieService: CookieService
  ) { }

  ngOnInit(): void {
    // this.getCategories();
    this.getProducts();
    this.getBrand();
    // debugger;
    const user = localStorage.getItem('currentUser') || '';
    if(user){
      this.user = JSON.parse(user);
      this.userName = this.user.username;
    }
    // console.log(this.user);
    const cart = localStorage.getItem('cart') || '';
     if (cart) {
       this.cart = JSON.parse(cart);
       this.totalProduct = this.cart.length;
      //  console.log(this.totalProduct)
     }
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

  getBrand(){
    this.brandService.getListBrand().subscribe((res)=>{
      this.listOfBrand = res;
      // console.log(this.listOfBrand);
    })
  }

  logout() {
    this.modal.confirm({
      nzTitle: 'Bạn có muốn đăng xuất!',
      nzContent: '',
      nzOkText: 'Có',
      nzOkType: 'primary',
      nzOkDanger: true,
      nzOnOk: () => {
        localStorage.removeItem('username');
        localStorage.removeItem('auth');
        localStorage.removeItem('userId');
        localStorage.clear();
        this.cookieService.delete('COOKIEID', "/");
        location.reload();
        this.socialAuthService.signOut();
      },
      nzCancelText: 'No',
      nzOnCancel: () => console.log('Cancel')
    });

  }
  getProducts() {
    this.lstProducts = concat(
      this.productService.getProductSearch( new ProductParam(1, 5)).pipe(
      ), // default items
      this.textInput_tenProduct$.pipe(
        debounceTime(500),
        distinctUntilChanged(),
        tap(() => (this.loadLstProducts = true)),
        switchMap((term) =>
          this.productService
            .getProductSearch(new ProductParam(1, 5, null, null, term))
            .pipe(
              catchError(() => of([])), // empty list on error
              tap(() => (this.loadLstProducts = false))
            )
        )
      )
    );
  }

}
