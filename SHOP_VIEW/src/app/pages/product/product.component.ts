import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NzNotificationService } from 'ng-zorro-antd/notification';
import { Category } from 'src/app/entity/Category';
// import { Product } from 'src/app/entity/Product';

import { Brand, Cpu, Gpu, Option, PageProductDetail, Product, ProductDetail, Ram, Rom } from 'src/app/models/type';
import { BrandService } from 'src/app/services/brand.service';
import { CategoryService } from 'src/app/services/category.service';
import { CpuService } from 'src/app/services/cpu.service';
import { GpuService } from 'src/app/services/gpu.service';
import { OptionService } from 'src/app/services/option.service';
import { ProductService } from 'src/app/services/product.service';
import { ProductDetailService } from 'src/app/services/productDetail.service';
import { RamService } from 'src/app/services/ram.service';
import { RomService } from 'src/app/services/rom.service';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css'],
})
export class ProductComponent implements OnInit {
  categoryId: String;
  listOfCategory: Category[] = [];
  listOfBrand: Brand[] = [];
  listOfProduct: Product[] = [];
  listOfRam: Ram[] = [];
  listOfRom: Rom[] = [];
  listOfCpu: Cpu[] = [];
  listOfGpu: Gpu[] = [];
  listOfOption: Option[] = [];
  price = [0, 30000000];
  listOfData: ProductDetail[] = [];
  pageSize = 9;
  pageIndex = 0;
  totalProduct: Number;
  status: String = 'default';
  paramsBrand = [];
  paramsRam = [];
  paramsRom = [];
  paramsCpu = [];
  paramsGpu = [];
  paramsProduct = [];
  paramsOptionValue = [];
  listBrandId;
  controlArray: Map<string, any> = new Map<string, any>();
  totalPrice: number = 0;
  cart: ProductDetail[] = [];
  compare: ProductDetail[] = [];
  lenthCompare: number ;
  pageProductDetail = new PageProductDetail();
  productId;
  constructor(
    route: ActivatedRoute,
    private activatedRoute: ActivatedRoute,private ramService : RamService, 
    private categoryService: CategoryService,private cpuService :CpuService,
    private brandService: BrandService,private gpuService: GpuService,
    private productService: ProductService,private romService: RomService,
    private notification: NzNotificationService,
    private productDetailService: ProductDetailService,
    private optionService: OptionService
  ) {
    route.params.subscribe((val) => {
      this.productId = this.activatedRoute.snapshot.params.category;
    });
  }

  createNotification(type: string, title: string, message: string): void {
    this.notification.create(type, title, message);
  }

  ngOnInit(): void {
    debugger;
    // this.activatedRoute.parent.data.subscribe((data) => {
    //   this.productId = data['productId'];
    //   console.log(this.productId);
    // });
    // declare cart 

    console.log(this.productId);

    if(this.productId){
      this.filterByBrand(this.productId,null)
    }

    const cart = localStorage.getItem('cart') || '';
    if (cart) {
      this.cart = JSON.parse(cart);
    }
    const compare = localStorage.getItem('compare') || '';
     if (compare) {
       this.compare = JSON.parse(compare);
       this.lenthCompare =  this.compare.length;
      //  console.log(this.lenthCompare);
     }
    // this.getCategories();
    this.getBrands();
    this.getOption();
    this.getListRam();
    this.getListRom();
    this.getListCpu();
    this.getListGpu();
    this.getListProduct();
    // console.log(this.listOfOption);
    // if (this.categoryId || !(this.categoryId === '')) {
    //   this.controlArray.set('categoryID', this.categoryId);
    // }
    this.getProducts(this.pageIndex, this.pageSize, 'id', 'descend');
  }


  getProducts(
    pageIndex: number,
    pageSize: number,
    sortField: string | null,
    sortOrder: string | null
  ) {
    this.controlArray.set('pageIndex', pageIndex);
    this.controlArray.set('pageSize', pageSize);
    this.controlArray.set('sortField', sortField);
    this.controlArray.set('sortOrder', sortOrder);
    // his.controlArray.set('sortOrder', sortOrder);
    // get product
    this.productDetailService.getPageProductDetail(this.controlArray).subscribe(
      (data) => {
        if (data && data) {
          this.pageProductDetail = data;
          this.totalProduct = this.pageProductDetail.totalElements;
          this.listOfData = data.content;
          this.pageIndex = ++this.pageProductDetail.number;
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


  getProductSort(param: any) {
    // get product
    console.log(param)
    this.productDetailService.getPageProductDetail(param).subscribe(
      (data) => {
        if (data && data.content) {
          this.pageProductDetail = data;
          this.totalProduct = this.pageProductDetail.totalElements;
          this.listOfData = data.content;
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

  getListProduct() {
    this.productService.getListProduct().subscribe(
      (data) => {
        if (data) {
          this.listOfProduct = data;
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

  getBrands() {
    this.brandService.getListBrand().subscribe(
      (data) => {
        if (data) {
          this.listOfBrand = data;
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

  getListRam() {
    this.ramService.getListRam().subscribe(
      (data) => {
        if (data) {
          this.listOfRam = data;
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

  getListRom() {
    this.romService.getListRom().subscribe(
      (data) => {
        if (data) {
          this.listOfRom = data;
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

  getListCpu() {
    this.cpuService.getListCpu().subscribe(
      (data) => {
        if (data) {
          this.listOfCpu = data;
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

  getListGpu() {
    this.gpuService.getListGpu().subscribe(
      (data) => {
        if (data) {
          this.listOfGpu= data;
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

  getOption() {
    this.optionService.getListOption().subscribe(
      (data) => {
        // console.log(data);
        if (data) {
          this.listOfOption = data;
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

  // load by price
  priceChange(value: number[] | number): void {
    const priceFrom = value[0] || 0;
    const priceTo = value[1] || 0;
    this.controlArray.set('pageIndex', 0);
    this.controlArray.set('pageSize', 9);
    this.controlArray.set('priceFrom', priceFrom);
    this.controlArray.set('priceTo', priceTo);
    this.getProductSort(this.controlArray);
  }

  //load product
  onPageIndexChange(value: number): void {
    console.log(value);
    this.controlArray.set('pageIndex', value);
    this.getProductSort(this.controlArray);
  }

  onPageSizeChange(value: number): void {
    this.controlArray.set('pageSize', value);
    this.getProductSort(this.controlArray);
  }

  filterByBrand(id, e) {
    debugger;
    if(e?.target?.name == 'brand'){
      if (e?.target?.checked) {
        this.paramsBrand.push(id);
      } else {
        this.paramsBrand = this.paramsBrand.filter((item) => {
          return (
            item !== id 
          );
        });
      }
    }

    if(e?.target?.name == 'ram'){
      if (e?.target?.checked) {
        this.paramsRam.push(id);
      } else {
        this.paramsRam = this.paramsRam.filter((item) => {
          return (
            item !== id 
          );
        });
      }
    }

    if(e?.target?.name == 'rom'){
      if (e?.target?.checked) {
        this.paramsRom.push(id);
      } else {
        this.paramsRom = this.paramsRom.filter((item) => {
          return (
            item !== id 
          );
        });
      }
    }

    if(e?.target?.name == 'cpu'){
      if (e?.target?.checked) {
        this.paramsCpu.push(id);
      } else {
        this.paramsCpu = this.paramsCpu.filter((item) => {
          return (
            item !== id 
          );
        });
      }
    }

    if(e?.target?.name == 'gpu'){
      if (e?.target?.checked) {
        this.paramsGpu.push(id);
      } else {
        this.paramsGpu = this.paramsGpu.filter((item) => {
          return (
            item !== id 
          );
        });
      }
    }
    debugger;
    if(e?.target?.name == 'product'){
      if (e?.target?.checked) {
        this.paramsProduct.push(id);
      } else {
        this.paramsProduct = this.paramsProduct.filter((item) => {
          return (
            item !== id 
          );
        });
      }
    }

    if(!e){
      this.paramsProduct.push(id);
      // document.getElementById("product").checked  = false;
    }
    
    if (id) {   
      this.controlArray.set('pageIndex', 0);
      this.controlArray.set('pageSize', 9);
      this.controlArray.set('brandId', this.paramsBrand.join(','));
      this.controlArray.set('lstRam', this.paramsRam.join(','));
      this.controlArray.set('lstRom', this.paramsRom.join(','));
      this.controlArray.set('lstCpu', this.paramsCpu.join(','));
      this.controlArray.set('lstGpu', this.paramsGpu.join(','));
      this.controlArray.set('productId', this.paramsProduct.join(','));
      // this.controlArray.set('optionValueID', this.paramsOptionValue.join(','));
      this.getProductSort(this.controlArray);
    }
  }
  changeStatus(e) {
    if (e) {
      if (e == "p-des") {
        this.getProducts(this.pageIndex, this.pageSize, 'productPrice', 'descend');
      }
      if (e == "p-asc") {
        this.getProducts(this.pageIndex, this.pageSize, 'productPrice', 'ascend');
      }
      if (e == "default") {
        this.getProducts(this.pageIndex, this.pageSize, 'productID', 'descend');
      }
    }
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
      window.location.reload();
    } else {
      this.createNotification(
        'info',
        'Sản phẩm đã có trong giỏ hàng',
        ''
      );
    }
  }

  addToCompare(product:ProductDetail){
    this.lenthCompare = this.compare.length;
    let duplicate = false;
    this.compare.forEach((ele) => {
      if (ele.id == product.id ) {
        duplicate = true;
      }
    });
    // if no => add item
    if(!duplicate){
      if(this.lenthCompare > 1){
        this.createNotification(
          'info',
          'vui lòng xóa bớt sản phẩm để so sánh',
          ''
        );
        return;
      }else{
        this.compare.push(product);
        this.updateCompare();
        this.createNotification(
          'success',
          'Sản phẩm đã thêm vào so sánh',
          ''
        );
        window.location.reload();
      }
    }else{
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
