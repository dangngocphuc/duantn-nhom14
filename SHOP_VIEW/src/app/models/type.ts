import { HighContrastModeDetector } from "@angular/cdk/a11y";
import { environment } from "src/environments/environment";

export class User {
  userID: number;
  username: string;
  tokenId: string;
  admin: boolean;
  maPhongBan: String;
  userPhone : String;
  permissions: string[];
}

export class LoginRequest {
  username: string;
  password: string;
  remember: boolean;
}

export class LoginResponse {
  errorCode: string;
  errorMessage: string;
  authenticated: string;
  authorization: string;
  userDetail: User;
}

export class Brand {
  id : number;
  maHang : string;
  tenHang : String;
}

export class PageBrand {
  content: Brand[];
  totalPages: number;
  totalElements: number;
  numberOfElements: number;
  size: number;
  number: number;
}

export class BrandRequest{
  brandMa: String;
  brandName: String;
}

export class Category {
  id :number;
  name : String;
  status : boolean;
  productList : Product[];
}

export class Voucher{
  id : number;
  createDate : Date;
  startDate : Date;
  endDate : Date;
  count : number;
  discount : number;
  type : number;
  status : boolean;
}

export class Option{
  id : number;
  optionName : String;
  optionCode:  String;
  status : Number;
  listOptionValue : OptionValue[] = [];
}

export class OptionValue{
  id : number;
  optionValue : String;
  code : String;
  status : boolean;
  option : Option;
}

export class Image {
  id : number;
  imgUrl : String;
  createDate : Date;
  imgType : String;
  status : boolean;
}

export class Product {
  id : number;
  maSanPham: String;
  tenSanPham : Date;
  brand : Brand;
  listProductDetail : ProductDetail[];
  listProductOption : ProductOption[];
}

export class ProductDetail {
  id : number;
  productName : String;
  product: Product;
  listProductDetailValue :ProductDetailValue[];
  quantity : number;
  listImei : Imei[];
  productPrice: number;
  productMarketprice:number;
  quanlityBuy:number;
}

export class ProductOption {
  id : number;
  option: Option;
  product: Product;
}

export class ProductDetailValue {
  id : number;
  productDetail : ProductDetail;
  option: Option;
  optionValue : OptionValue;
  listOptionValue : any;
}

export class PageProduct {
  content: Product[];
  totalPages: number;
  totalElements: number;
  numberOfElements: number;
  size: number;
  number: number;
}
export class PageProductDetail {
  content: ProductDetail[];
  totalPages: number;
  totalElements: number;
  numberOfElements: number;
  size: number;
  number: number;
}
export class PageOption {
  content: Option[];
  totalPages: number;
  totalElements: number;
  numberOfElements: number;
  size: number;
  number: number;
}

export class PageOptionValue {
  content: OptionValue[];
  totalPages: number;
  totalElements: number;
  numberOfElements: number;
  size: number;
  number: number;
}

export class OptionRequest {
  optionMa: String;
  optionTen :  String;
}

export class OptionValueRequest {
  optionId: String;
}

export class PagesRequest {
  page: number = 0;
  size: number = 10;
  sort: string;

  constructor(page: number = 0, size: number = 5, sort?: string) {
    this.page = page;
    this.size = size;
    this.sort = sort;
  }
}

export class Imei{
  id: number;
  imei: String;
  productDetail: ProductDetail; 
  status:  number;
}

export class PageImei {
  content: Imei[];
  totalPages: number;
  totalElements: number;
  numberOfElements: number;
  size: number;
  number: number;
}

export class ImeiRequest {
  imei : String;
  productId : number;
}


export class Bill {
  id: number;
  user: User;
  total: number;
  payment: string;
  address: string;
  date: Date;
  name: string;
  phone: string;
  status: string;
  listBillDetail: BillDetail[];
  products: String;
  billCode: String;
}


export class BillDetail {
  id: number;
  bill: Bill;
  productDetail: ProductDetail;
  price : number;
  quantity : number;
  listImei : Imei[];
}