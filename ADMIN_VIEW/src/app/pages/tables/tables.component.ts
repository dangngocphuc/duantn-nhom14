import { Component, OnInit, ViewChild } from '@angular/core';
import { ModalDismissReasons, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ModalManager } from 'ngb-modal';
import { Action, Common } from 'src/app/commons/common';
import { Brand, Option, PageProduct, PagesRequest, Product, ProductOption } from 'src/app/models/type';
import { ProductService } from 'src/app/services/product.service';
import * as ClassicEditor from '@ckeditor/ckeditor5-build-classic';
import { catchError, concat, debounceTime, distinctUntilChanged, Observable, of, Subject, switchMap, tap } from 'rxjs';
import { OptionService } from 'src/app/services/option.service';
import { clippingParents } from '@popperjs/core';
import { BrandService } from 'src/app/services/brand.service';

@Component({
  selector: 'app-tables',
  templateUrl: './tables.component.html',
  styleUrls: ['./tables.component.scss']
})
export class TablesComponent implements OnInit {

  controlArray: Map<string, any> = new Map<string, any>();
  // modalRef: any;
  isVisible = false;
  closeResult = '';
  action: Action;
  isEdit = false;
  isInsert = false;
  isView = false;
  Action = Action;
  pageProduct: PageProduct;
  product = new Product();
  listOption: Option[];
  common: Common = new Common();
  lstTrangThai = this.common.lstTrangThai
  listBrand : Brand [];

  @ViewChild('myModal') myModal;
  private modalRef;
  constructor(private productService: ProductService, private optionService: OptionService,private brandService: BrandService,
    private modalService: ModalManager) { }

  public Editor = ClassicEditor;

  lstOption: Observable<any[]>;
  loadlstOption = false;
  textInput_tenOption$ = new Subject<string>();

  ngOnInit() {
    this.getProducts(1, 5);
    this.getListBrand();
  }

  getListBrand(){
    this.brandService.getListBrand().subscribe(
      (data) => {
        if (data) {
          this.listBrand = data;
          console.log(this.pageProduct);
        }
      },
      (error) => { }
    );
  }

  getProducts(pageIndex: number, pageSize: number) {
    this.controlArray.set('pageIndex', pageIndex);
    this.controlArray.set('pageSize', pageSize);
    // get product
    this.productService.getProducts(this.controlArray).subscribe(
      (data) => {
        if (data) {
          this.pageProduct = data;
          console.log(this.pageProduct);
        }
      },
      (error) => { }
    );
  }

  openModal(action) {
    this.loadOption();
    this.action = action;
    this.modalRef = this.modalService.open(this.myModal, {
      size: "md",
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

  view(id) {
    this.productService.getProductById(id).subscribe((respone) => {
      debugger;
      this.product = respone;
      // this.initForm(this.option);
      let listProductTemp = [];
      this.product.listProductOption.forEach((e)=>{
        listProductTemp.push(e.option);
      })
      this.listOption = listProductTemp;
      console.log(this.listOption );
      console.log(this.product)
      this.openModal(Action.CAPNHAT);
    })
  }

  closeModal() {
    this.listOption = [];
    this.product = new Product();
    this.modalService.close(this.modalRef);
  }

  onFileImageSelect(e) {
  }

  loadOption() {
    this.lstOption = concat(
      this.optionService.ngSelect(new PagesRequest(0, 10), null), // default items
      this.textInput_tenOption$.pipe(
        debounceTime(500),
        distinctUntilChanged(),
        tap(() => (this.loadlstOption = true)),
        switchMap((term) =>
          this.optionService
            .ngSelect(new PagesRequest(0, 10), { optionTen: term })
            .pipe(
              catchError(() => of([])), // empty list on error
              tap(() => (this.loadlstOption = false))
            )
        )
      )
    );
  }

  save() {
    let listProductOptionTemp = [];
    console.log(this.listOption);
    debugger;
    this.listOption.forEach((e) => {
      // debugger;
      // let op = new Option();
      // op.id = Number(e);
      let productOption = new ProductOption();
      productOption.option = e;
      listProductOptionTemp.push(productOption);
    })
    this.product.listProductOption = listProductOptionTemp;
    console.log(this.product);
    this.productService.saveProduct(this.product).subscribe((res) => {
      console.log(res);
      this.closeModal();
      this.getProducts(1, 5);
    })
  }

}
