import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ModalManager } from 'ngb-modal';
import { Action, Common } from 'src/app/commons/common';
import { PagePromotion, PagesRequest, Promotion } from 'src/app/models/type';
import { PromotionService } from 'src/app/services/promotion.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-voucher',
  templateUrl: './voucher.component.html',
  styleUrls: ['./voucher.component.scss']
})
export class VoucherComponent implements OnInit {

  isVisible = false;
  closeResult = '';
  action: Action;
  isEdit = false;
  isInsert = false;
  isView = false;
  Action = Action;
  pageSizes = [5, 10, 15, 20];

  // RamRequest = new RamRequest();
  pageRequest = new PagesRequest();
  pageRam = new PagePromotion();
  promotion = new Promotion();
  // lstProductDetail : ProductDetail[];
  common: Common = new Common();
  lstTrangThai = this.common.lstTrangThai;
  formGroup: FormGroup;
  @ViewChild('myModal') myModal;
  private modalRef;
  constructor(private promotionService: PromotionService, private modalService: ModalManager, private fb: FormBuilder) { }


  ngOnInit(): void {
    this.getPagePromotion();
    this.formGroup = this.fb.group({
      promotion: this.fb.group({
        code: [{ value: '' }],
        value: [{ value: '' }],
        dateFrom: [{ value: '', }, Validators.required],
        dateTo: [{ value: '', }, Validators.required],
        type: [{ value: '', }, Validators.required],
        quantity: [{ value: '', }, Validators.required],
      }),
    });
  }

  getPagePromotion() {
    // get product
    // console.log(this.RamRequest)
    this.promotionService.getPagePromotion(this.pageRequest).subscribe(
      (data) => {
        if (data) {
          this.pageRam = data;
          console.log(data);
          this.pageRam.number = ++this.pageRam.number;
          console.log(this.pageRam);
        }
      }, (error) => {
        console.log(error);
      }
    );
  }

  handlePageSizeChange(event: any) {
    this.pageRequest.size = event.target.value;
    this.getPagePromotion();
  }
  handlePageChange(event: any) {
    this.pageRequest.page = event - 1;
    this.getPagePromotion();
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
  }

  save(){
    console.log(this.promotion);
    debugger;
    // console.log(this.ram);
    console.log(this.formGroup);
    // this.isFormSubmit = true;
    if (this.formGroup.status == "INVALID") {
      return;
    }
    this.promotionService.savePromotion(this.promotion).subscribe((respone) => {
      // this.option = respone;
      if (respone) {
        Swal.fire('', '', 'success');
        this.closeModal();
        this.getPagePromotion()
      }
    }, (error) => {
      Swal.fire('', error, 'error')
    })
  }
}
