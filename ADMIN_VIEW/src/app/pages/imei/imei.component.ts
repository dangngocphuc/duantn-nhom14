import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ModalManager } from 'ngb-modal';
import { Action, Common } from 'src/app/commons/common';
import { Imei, ImeiRequest, PageImei, PagesRequest, ProductDetail } from 'src/app/models/type';
import { ImeiService } from 'src/app/services/imei.service';
import { ProductDetailService } from 'src/app/services/productDetail.service';

@Component({
  selector: 'app-imei',
  templateUrl: './imei.component.html',
  styleUrls: ['./imei.component.scss']
})
export class ImeiComponent implements OnInit {
  isVisible = false;
  closeResult = '';
  action: Action;
  isEdit = false;
  isInsert = false;
  isView = false;
  Action = Action;
  pageSizes = [5, 10, 15, 20];
  
  imeiRequest = new ImeiRequest();
  pageRequest = new PagesRequest();
  pageImei = new PageImei();
  imei = new Imei();
  lstProductDetail : ProductDetail[];
  common: Common = new Common();
  lstTrangThai = this.common.lstTrangThai;
  
  option = new Option();
  formGroup: FormGroup;
  controlArray: Map<string, any> = new Map<string, any>();
  isFormSubmit = false;
  @ViewChild('myModal') myModal;
  private modalRef;

  constructor(private imeiService: ImeiService, private modalService: ModalManager, private fb: FormBuilder,
    private productService : ProductDetailService) { }

  ngOnInit(): void {
    this.getImei();
    this.formGroup = this.fb.group({
      imei: this.fb.group({
        imei: [{ value: '' }, Validators.required],
        productDetail: [{ value: '', }, Validators.required],
        status: [{ value: '', }, Validators.required]
      }),
    });
  }


  getImei() {
    // get product
    console.log(this.imeiRequest)
    this.imeiService.getImei(this.pageRequest, this.imeiRequest).subscribe(
      (data) => {
        if (data) {
          this.pageImei = data;
          this.pageImei.number = ++this.pageImei.number;
          console.log(this.pageImei);
        }
      }, (error) => {
         console.log(error);
      }
    );
  }
  handlePageSizeChange(event: any) {
    this.pageRequest.size = event.target.value;
    this.getImei();
  }
  handlePageChange(event: any) {
    this.pageRequest.page = event - 1;
    this.getImei();
  }

  search() {
    this.getImei();
  }

  view(id) {
    this.imeiService.getImeiById(id).subscribe((respone) => {
      this.imei = respone;
      // this.initForm(this.option);
      console.log(this.imei);
      this.openModal(Action.CAPNHAT);
    })
  }

  setStatus(item) {
    if (item == 1) {
      return "Còn hàng";
    }
    else {
      return "hết hàng";
    }
  }

  openModal(action) {
    this.productService.getListProductDetail().subscribe((res)=>{
      this.lstProductDetail = res;
    })
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

  closeModal() {
    this.modalService.close(this.modalRef);
    this.isFormSubmit = false;
    this.imei = new Imei();
  }
}
