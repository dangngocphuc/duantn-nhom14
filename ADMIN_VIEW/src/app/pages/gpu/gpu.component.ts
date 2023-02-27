import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ModalManager } from 'ngb-modal';
import { Action, Common } from 'src/app/commons/common';
import { Gpu, PageGpu, PageRom, PagesRequest } from 'src/app/models/type';
import { GpuService } from 'src/app/services/Gpu.service';

@Component({
  selector: 'app-gpu',
  templateUrl: './gpu.component.html',
  styleUrls: ['./gpu.component.scss']
})
export class GpuComponent implements OnInit {

  isVisible = false;
  closeResult = '';
  action: Action;
  isEdit = false;
  isInsert = false;
  isView = false;
  Action = Action;
  pageSizes = [5, 10, 15, 20];
  // RomRequest = new RomRequest();
  pageRequest = new PagesRequest();
  pageGpu = new PageGpu();
  gpu = new Gpu();
  // lstProductDetail : ProductDetail[];
  common: Common = new Common();
  lstTrangThai = this.common.lstTrangThai;
  option = new Option();
  formGroup: FormGroup;
  controlArray: Map<string, any> = new Map<string, any>();
  isFormSubmit = false;

  @ViewChild('myModal') myModal;
  private modalRef;

  constructor(private gpuService: GpuService, private modalService: ModalManager, private fb: FormBuilder) { }

  ngOnInit(): void {
    this.getGpu();
    this.formGroup = this.fb.group({
      gpu: this.fb.group({
        id: [{ value: '' }, Validators.required],
        gpu: [{ value: '', }, Validators.required],
      }),
    });
  }

  getGpu() {
    // get product
    // console.log(this.RomRequest)
    this.gpuService.getPageGpu(this.pageRequest).subscribe(
      (data) => {
        if (data) {
          this.pageGpu = data;
          console.log(data);
          this.pageGpu.number = ++this.pageGpu.number;
          console.log(this.pageGpu);
        }
      }, (error) => {
        console.log(error);
      }
    );
  }

  handlePageSizeChange(event: any) {
    this.pageRequest.size = event.target.value;
    this.getGpu();
  }

  handlePageChange(event: any) {
    this.pageRequest.page = event - 1;
    this.getGpu();
  }

  search() {
    this.getGpu();
  }

  view(id) {
    console.log(id);
    this.gpuService.getGpuById(id).subscribe((respone) => {
      this.gpu = respone;
      // this.initForm(this.option);
      console.log(this.gpu);
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
    // this.productService.getListProductDetail().subscribe((res)=>{
    //   this.lstProductDetail = res;
    // })
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
    this.gpu = new Gpu();
  }

}
