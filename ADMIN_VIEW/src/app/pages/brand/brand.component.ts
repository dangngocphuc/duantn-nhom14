import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ModalManager } from 'ngb-modal';
import { Action, Common } from 'src/app/commons/common';
import { Brand, BrandRequest, PageBrand, PagesRequest } from 'src/app/models/type';
import { BrandService } from 'src/app/services/brand.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-brand',
  templateUrl: './brand.component.html',
  styleUrls: ['./brand.component.scss']
})
export class BrandComponent implements OnInit {

  isVisible = false;
  closeResult = '';
  action: Action;
  isEdit = false;
  isInsert = false;
  isView = false;
  Action = Action;
  pageSizes = [5, 10, 15, 20];

  pageRequest = new PagesRequest();
  brandRequest = new BrandRequest();
  pageBrand = new PageBrand();

  brand = new Brand();

  common: Common = new Common();
  lstTrangThai = this.common.lstTrangThai;

  formGroup: FormGroup;
  controlArray: Map<string, any> = new Map<string, any>();
  isFormSubmit = false;

  @ViewChild('myModal') myModal;
  private modalRef;


  constructor(private brandService: BrandService, private modalService: ModalManager, private fb: FormBuilder) { }

  ngOnInit(): void {
    this.getListBrand();
    this.formGroup = this.fb.group({
      brand: this.fb.group({
        maHang: [{ value: '' }, Validators.required],
        tenHang: [{ value: '', }, Validators.required]
      }),
    });
  }


  getListBrand() {
    // get product
    this.brandService.getPageBrand(this.pageRequest, this.brandRequest).subscribe(
      (data) => {
        if (data) {
          this.pageBrand = data;
          this.pageBrand.number = ++this.pageBrand.number;
          console.log(this.pageRequest);
        }
      }, (error) => {
        console.log(error);
      }
    );
  }
  handlePageSizeChange(event: any) {
    this.pageRequest.size = event.target.value;
    this.getListBrand();
  }
  handlePageChange(event: any) {
    this.pageRequest.page = event - 1;
    this.getListBrand();
  }

  search() {
    this.getListBrand();
  }

  view(id) {
    this.brandService.getBrandById(id).subscribe((respone) => {
      this.brand = respone;
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
    this.brand = new Brand();
  }

  save(){
    console.log(this.brand);
    console.log(this.formGroup);
    this.isFormSubmit = true;
    if (this.formGroup.status == "INVALID") {
      return;
    }
    this.brandService.saveBrand(this.brand).subscribe((respone) => {
      // this.option = respone;
      this.closeModal();
      this.getListBrand();
    }, (error) => {
        Swal.fire('',error,'error')
    })
  }
}
