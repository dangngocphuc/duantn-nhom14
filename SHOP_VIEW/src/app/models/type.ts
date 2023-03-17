export class User {
  userID: number;
  username: string;
  tokenId: string;
  admin: boolean;
  maPhongBan: String;
  phoneNumber: String;
  permissions: string[];
  listAddress: Address[];
}

export class Address {
  id: number;
  user:  User;
  provinceID: number;
  districtID: number;
  wardCode: String;
  specificAddress: String;
  defaults: number;
  listProvince: any;
  listDistrict: any;
  listWard: any;
  check = 0;
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
  id: number;
  maHang: string;
  tenHang: String;
}

export class PageBrand {
  content: Brand[];
  totalPages: number;
  totalElements: number;
  numberOfElements: number;
  size: number;
  number: number;
}

export class BrandRequest {
  brandMa: String;
  brandName: String;
}

export class Category {
  id: number;
  name: String;
  status: boolean;
  productList: Product[];
}

export class Voucher {
  id: number;
  createDate: Date;
  startDate: Date;
  endDate: Date;
  count: number;
  discount: number;
  type: number;
  status: boolean;
}

export class Option {
  id: number;
  optionName: String;
  optionCode: String;
  status: Number;
  listOptionValue: OptionValue[] = [];
}

export class OptionValue {
  id: number;
  optionValue: String;
  code: String;
  status: boolean;
  option: Option;
}

export class Image {
  id: number;
  imgUrl: String;
  createDate: Date;
  imgType: String;
  status: boolean;
}

export class Product {
  id: number;
  maSanPham: String;
  tenSanPham: Date;
  brand: Brand;
  listProductDetail: ProductDetail[];
  listProductOption: ProductOption[];
}

export class ProductDetail {
  id: number;
  productName: String;
  product: Product;
  listProductDetailValue: ProductDetailValue[];
  quantity: number;
  listImei: Imei[];
  productPrice: number;
  productMarketprice: number;
  quanlityBuy: number;
  demand: String;
  cpu: Cpu;
  ram: Ram;
  gpu: Gpu;
  rom: Rom;
  productSize: String;
  productWeight: String;
}

export class Cpu {
  id: number;
  cpu: String;
  createDate: Date;
  updateDate: Date;
  status: number;
}

export class Ram {
  id: number;
  ram: String;
  createDate: Date;
  updateDate: Date;
  status: number;
}

export class Rom {
  id: number;
  rom: String;
  createDate: Date;
  updateDate: Date;
  status: number;
}

export class Gpu {
  id: number;
  gpu: String;
  createDate: Date;
  updateDate: Date;
  status: number;
}

export class ProductOption {
  id: number;
  option: Option;
  product: Product;
}

export class ProductDetailValue {
  id: number;
  productDetail: ProductDetail;
  option: Option;
  optionValue: OptionValue;
  listOptionValue: any;
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
  optionTen: String;
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

export class Imei {
  id: number;
  imei: String;
  productDetail: ProductDetail;
  status: number;
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
  imei: String;
  productId: number;
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
  price: number;
  quantity: number;
  listImei: Imei[];
}

export class Province {
  CanUpdateCOD: boolean;
  Code: String;
  CountryID: number;
  CreatedAt: number;
  IsEnable: number;
  NameExtension: [];
  ProvinceID: number;
  ProvinceName: String;
  RegionCPN: number;
  RegionID: number;
  Status: number;
  UpdatedAt: number;
  UpdatedBy: number;
}

export class District {
  DistrictID: number;
  ProvinceID: number;
  DistrictName: String;
  Code: String;
  Type: number;
  SupportType: number;
  NameExtension: [];
  IsEnable: number;
  UpdatedBy: number;
  CreatedAt: Date;
  UpdatedAt: Date;
  CanUpdateCOD: boolean;
  Status: number;
  WhiteListClient: any;
  WhiteListDistrict: any;
  ReasonCode: String;
  ReasonMessage: String;
  OnDates: null;
  UpdatedIP: String;
  UpdatedEmployee: number;
  UpdatedSource: String;
  UpdatedDate: Date;
}

export class Ward {
  CanUpdateCOD: boolean;
  CreatedAt: Date;
  DistrictID: number;
  IsEnable: 1
  NameExtension: [];
  OnDates: null;
  ReasonCode: String;
  ReasonMessage: String;
  Status: number;
  SupportType: number;
  UpdatedAt: Date;
  UpdatedBy: number;
  UpdatedDate: Date;
  WardCode: String;
  WardName: String;
  WhiteListClient: any;
  WhiteListWard: any;
}

export class RequestSevice {
  shop_id: number;
  from_district: number;
  to_district: number;
}

export class Service {
  service_id: number;
  service_type_id: number;
  short_name: String;
}

export class RequestFeeSevice {
  service_id: number;
  insurance_value: number;
  coupon: null;
  from_district_id: number;
  to_district_id: number;
  to_ward_code: number;
  height = 15;
  length = 15;
  weight = 1000;
  width = 15;
}

export class ResponseFeeSevice {
  total: number;
  insurance_value: number;
  service_fee: number;
  coupon_value: number;
  r2s_fee: number;
}

export class RequestLeadTime {
  from_district_id: number;
  from_ward_code: String;
  to_district_id: number;
  to_ward_code: String;
  service_id: number;
}