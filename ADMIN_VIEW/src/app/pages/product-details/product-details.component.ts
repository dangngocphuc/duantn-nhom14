
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ModalDismissReasons, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { clippingParents } from '@popperjs/core';
import * as moment from 'moment';
import { ModalManager } from 'ngb-modal';
import { catchError, concat, debounceTime, distinctUntilChanged, forkJoin, lastValueFrom, Observable, of, Subject, switchMap, tap } from 'rxjs';
import { Action, Common } from 'src/app/commons/common';
import { Cpu, Gpu, Imei, Option, OptionValue, PageProductDetail, PagesRequest, Product, ProductDetail, ProductDetailRequest, ProductDetailValue, ProductOption, Ram, Rom } from 'src/app/models/type';
import { CpuService } from 'src/app/services/cpu.service';
import { GpuService } from 'src/app/services/gpu.service';
import { OptionService } from 'src/app/services/option.service';
import { ProductService } from 'src/app/services/product.service';
import { ProductDetailService } from 'src/app/services/productDetail.service';
import { RamService } from 'src/app/services/ram.service';
import { RomService } from 'src/app/services/rom.service'; 
import Swal from 'sweetalert2';
@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.scss']
})
export class ProductDetailsComponent implements OnInit {
  controlArray: Map<string, any> = new Map<string, any>();
  // modalRef: any;
  isVisible = false;
  closeResult = '';
  action: Action;
  isEdit = false;
  isInsert = false;
  isView = false;
  Action = Action;

  @ViewChild('myModal') myModal;

  people$: Observable<any[]>;
  formGroup: FormGroup;
  product = new Product();
  option = new Option();
  optionValue = new OptionValue();
  productDetail = new ProductDetail();
  pageProductDetail = new PageProductDetail();
  // productVariant = new ProductVariant();
  // pagesResponse = new PageResponse();
  common = new Common();
  lstDemand = this.common.lstDemand;
  lstTrangThai = this.common.lstTrangThai;
  lstProduct: Observable<any[]>;
  loadlstProduct = false;
  textInput_tenProduct$ = new Subject<string>();

  request = new ProductDetailRequest();

  pageRequest = new PagesRequest();
  pageSizes = [5, 10, 15, 20];
  

  listCpu : Cpu[];
  listRam : Ram[];
  listRom : Rom[];
  listGpu : Gpu[];
  

  private modalRef;

  constructor(
    private productService: ProductService,
    private cpuService : CpuService,
    private ramService : RamService,
    private romService : RomService,
    private gpuService : GpuService,
    private optionService: OptionService,
    private productDetailService: ProductDetailService,
    private modalService: ModalManager,
    private fb: FormBuilder
  ) { }

  ngOnInit() {
    // debugger;
    // this.getProducts(1, 5);
    // this.product.id = 4;
    // this.option.id = 1;
    // this.optionValue.id = 5;
    // this.productDetail.id = 2;
    // this.getProducts(1,5);
    this.loadProduct();
    this.getListCpu();
    this.getListRam();
    this.getListRom();
    this.getListGpu();
    this.getProducts(this.pageRequest.page, this.pageRequest.size);
    this.formGroup = this.fb.group({
      productDetail: this.fb.group({
        name: [{ value: '', }, Validators.required],
        code: [{ value: '', }, Validators.required],
        product: [{ value: '', }, Validators.required],
        demand : [{ value: '', }, Validators.required],
        cpu : [{ value: '', }, Validators.required],
        ram : [{ value: '', }, Validators.required],
        rom : [{ value: '', }, Validators.required],
        gpu : [{ value: '', }, Validators.required],
        productPrice : [{ value: '', }, Validators.required],
        productMarketprice : [{ value: '', }, Validators.required],
      }),
      listImei: this.fb.array([], Validators.required),
    });

    // this.getImei().disable();
    
  }

  getProducts(pageIndex: number, pageSize: number) {
    debugger;
    this.controlArray.set('pageIndex', pageIndex);
    this.controlArray.set('pageSize', pageSize);
    this.productDetailService.getPageProductDetail(this.controlArray).subscribe(
      (data) => {
        if (data) {
          console.log(data);
          this.pageProductDetail = data;
          this.pageProductDetail.number = ++this.pageProductDetail.number;
        }
      },
      (error) => {
        console.log(error)
      }
    );
  }

  search(){
    console.log(this.request.toDate);
    debugger;
    this.controlArray.set('pageIndex', this.pageRequest.page);
    this.controlArray.set('pageSize', this.pageRequest.size);
    this.controlArray.set('productId', this.request?.productId?.toString());
    this.controlArray.set('productCode', this.request?.productCode);
    this.controlArray.set('productName', this.request?.productName);
    this.controlArray.set('lstCpu', this.request?.lstCpu?.toString());
    this.controlArray.set('lstGpu', this.request?.lstGpu?.toString());
    this.controlArray.set('lstGpu', this.request?.lstGpu?.toString());
    this.controlArray.set('lstRom', this.request?.lstRom?.toString());
    this.controlArray.set('toDate', moment(this.request.toDate).format('DD/MM/yyyy'));
    this.controlArray.set('fromDate', moment(this.request.fromDate).format('DD/MM/yyyy'));

    this.productDetailService.getPageProductDetail(this.controlArray).subscribe(
      (data) => {
        if (data) {
          console.log(data);
          this.pageProductDetail = data;
          this.pageProductDetail.number = ++this.pageProductDetail.number;
        }
      },
      (error) => {
        console.log(error)
      }
    );
  }
  handlePageSizeChange(event: any) {
    this.pageRequest.size = event.target.value;
    this.pageRequest.page = 0;
    this.getProducts(this.pageRequest.page,this.pageRequest.size);
  }
  handlePageChange(event: any) {
    this.pageRequest.page = event - 1;
    this.getProducts(this.pageRequest.page,this.pageRequest.size);
  }

  loadProduct() {
    this.lstProduct = concat(
      this.productService.ngSelect(new PagesRequest(0, 10), null), // default items
      this.textInput_tenProduct$.pipe(
        debounceTime(500),
        distinctUntilChanged(),
        tap(() => (this.loadlstProduct = true)),
        switchMap((term) =>
          this.productService
            .ngSelect(new PagesRequest(0, 10), { productName: term })
            .pipe(
              catchError(() => of([])), // empty list on error
              tap(() => (this.loadlstProduct = false))
            )
        )
      )
    );
  }

  openModal(action,content) {
    this.loadProduct();
    this.getListCpu();
    this.getListRam();
    this.getListRom();
    this.getListGpu();
    // this.initForm();
    this.action = action;
    this.modalRef = this.modalService.open(content, {
      size: "xl",
      modalClass: 'mymodal',
      hideCloseButton: false,
      centered: false,
      backdrop: true,
      animation: true,
      keyboard: false,
      closeOnOutsideClick: false,
      backdropClass: "modal-backdrop"
    })
    this.formGroup.get("listImei").disable();
  }

  closeModal() {
    // debugger;
    this.getImei.clearValidators();
    this.getImei.clear();
    this.productDetail = new ProductDetail();
    this.modalRef.close();
  }

  view(item, content) {
    this.productDetailService.getProductDetailById(item.id).subscribe((respone) => {
      debugger;
      this.productDetail = respone.result;
      if(this.productDetail.listImei.length>0){
        this.productDetail.listImei.forEach((e)=>{
          this.addRow(e);
        })
      }
      // this.listOption = listProductTemp;
      this.openModal(Action.CAPNHAT,content);
    });
  }
  

  initForm(productDetail?: ProductDetail) {
    this.formGroup = this.fb.group({
      productDetail: this.fb.group({
        name: [{ value: this.productDetail.productName}, Validators.required],
        product: [{ value: this.productDetail.product, }, Validators.required],
        demand : [{ value: '', }, Validators.required],
      }),
      listImei: this.fb.array([], Validators.required),
    });
    if (productDetail?.listImei?.length && productDetail?.listImei?.length > 0) {
      productDetail?.listImei?.forEach((item) => {
        this.addRow(item);
      });
    }
  }


  async addRow(item: Imei = new Imei()) {
    if (!item.id) {
      this.productDetail.listImei.push(item);
    }
    await this.getImei.push(this.fb.group({
      imei: [{ value: item?.imei }],
      status: [{ value: item?.status }, Validators.required],
    }));
  }

  get getImei() {
    return this.formGroup.controls['listImei'] as FormArray;
  }

  save() {
    console.log(this.productDetail);
    this.productDetailService.saveProductDetail(this.productDetail).subscribe((res) => {
      if(res){
        Swal.fire('','','success');
        this.closeModal();
        this.getProducts(this.pageRequest.page,this.pageRequest.size);
      }
    })
    
  }
  
  lstproductOptionTemp: ProductOption[];

  async onSelect(item?) {
    // debugger;
    // this.product = item;
    this.productDetail.listProductDetailValue = [];
    if (item?.id) {
      let listProductDetailValue = [];
      this.product = await lastValueFrom(this.productService.getProductById(item.id));
      const observables = this.product.listProductOption.map((e) => {
        return this.optionService.getOptionById(e.option.id);
      });
      forkJoin(observables).subscribe((responses) => {
        responses.forEach((response, i) => {
          let poductDetailValue = new ProductDetailValue();
          poductDetailValue.option = response;
          poductDetailValue.listOptionValue = response.listOptionValue;
          listProductDetailValue.push(poductDetailValue);
        });

        this.productDetail.listProductDetailValue = listProductDetailValue;
        console.log(this.productDetail);
        this.productDetail?.listProductDetailValue?.forEach((item) => {
          // this.addRow();
          this.addRow();
        });
        // if (this.productDetail?.listProductDetailValue?.length && this.productDetail?.listProductDetailValue?.length > 0) {
        // }
      });
    }
  }

  getListCpu(){
    this.cpuService.getListCpu().subscribe((res)=>{
      this.listCpu = res;
    })
  }

  getListRam(){
    this.ramService.getListRam().subscribe((res)=>{
      this.listRam = res;
    })
  }

  getListRom(){
    this.romService.getListRom().subscribe((res)=>{
      this.listRom = res;
    })
  }

  getListGpu(){
    this.gpuService.getListGpu().subscribe((res)=>{
      this.listGpu = res;
    })
  }
}
