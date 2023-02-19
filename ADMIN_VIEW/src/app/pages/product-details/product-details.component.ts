
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ModalDismissReasons, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { clippingParents } from '@popperjs/core';
import { ModalManager } from 'ngb-modal';
import { catchError, concat, debounceTime, distinctUntilChanged, forkJoin, lastValueFrom, Observable, of, Subject, switchMap, tap } from 'rxjs';
import { Action } from 'src/app/commons/common';
import { Option, OptionValue, PageProductDetail, PagesRequest, Product, ProductDetail, ProductDetailValue, ProductOption } from 'src/app/models/type';
import { OptionService } from 'src/app/services/option.service';
import { ProductService } from 'src/app/services/product.service';
import { ProductDetailService } from 'src/app/services/productDetail.service';
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
  lstProduct: Observable<any[]>;
  loadlstProduct = false;
  textInput_tenProduct$ = new Subject<string>();

  private modalRef;

  constructor(
    private productService: ProductService,
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
    this.getProducts(1, 5);
    this.formGroup = this.fb.group({
      productDetail: this.fb.group({
        name: [{ value: '', }, Validators.required],
        product: [{ value: '', }, Validators.required],
      }),
      listProductDetailValue: this.fb.array([], Validators.required),
    });
  }

  getProducts(pageIndex: number, pageSize: number) {
    this.controlArray.set('pageIndex', pageIndex);
    this.controlArray.set('pageSize', pageSize);
    this.productDetailService.getPageProductDetail(this.controlArray).subscribe(
      (data) => {
        if (data) {
          console.log(data);
          this.pageProductDetail = data;
        }
      },
      (error) => {
        console.log(error)
      }
    );
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
  }

  closeModal() {
    // debugger;
    this.getListProductDetailValue.clearValidators();
    this.getListProductDetailValue.clear();
    this.productDetail = new ProductDetail();
    this.modalRef.close();
  }

  view(item, content) {
    this.productDetailService.getProductDetailById(item.id).subscribe((respone) => {
      debugger;
      this.productDetail = respone.result;
      const observables = [];
      if(this.productDetail.listProductDetailValue.length == 0){
        this.openModal(Action.CAPNHAT, content);
        return;
      }
      this.productDetail.listProductDetailValue.forEach((e) => {
        this.addRow(e);
        observables.push(this.optionService.getOptionById(e.option.id));
      });
      forkJoin(observables).subscribe((res) => {
        let i = 0;
        res.forEach((r) => {
          this.productDetail.listProductDetailValue[i].listOptionValue = r.listOptionValue;
          i++;
        });
        // this.initForm();
        this.openModal(Action.CAPNHAT, content);
      });
    });
  }
  

  initForm(productDetail?: ProductDetail) {
    this.formGroup = this.fb.group({
      productDetail: this.fb.group({
        name: [{ value: this.productDetail.productName}, Validators.required],
        product: [{ value: this.productDetail.product, }, Validators.required],
      }),
      listProductDetailValue: this.fb.array([], Validators.required),
    });
    if (productDetail?.listProductDetailValue?.length && productDetail?.listProductDetailValue?.length > 0) {
      productDetail?.listProductDetailValue?.forEach((item) => {
        this.addRow(item);
      });
    }
  }


  async addRow(item: ProductDetailValue = new ProductDetailValue()) {
    await this.getListProductDetailValue.push(this.fb.group({
      option: [{ value: item?.option?.optionName }],
      optionValue: [{ value: item?.optionValue }, Validators.required],
    }));
  }


  get getListProductDetailValue() {
    return this.formGroup.controls['listProductDetailValue'] as FormArray;
  }

  save() {
    this.productDetailService.saveProductDetail(this.productDetail).subscribe((res) => {
      if(res){
        Swal.fire('','','success');
        this.closeModal();
        this.getProducts(1, 5);
      }
    })
    console.log(this.productDetail);
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

}
