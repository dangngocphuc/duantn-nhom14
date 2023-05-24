import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ModalManager } from 'ngb-modal';
import { Observable } from 'rxjs';
import { Action, Common } from 'src/app/commons/common';
import { Cpu, PageCpu, PagesRequest, ProductDetail } from 'src/app/models/type';
import { CpuService } from 'src/app/services/cpu.service';
import { ProductDetailService } from 'src/app/services/productDetail.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-cpu',
  templateUrl: './cpu.component.html',
  styleUrls: ['./cpu.component.scss']
})
export class CpuComponent implements OnInit {

  isVisible = false;
  closeResult = '';
  action: Action;
  isEdit = false;
  isInsert = false;
  isView = false;
  Action = Action;
  pageSizes = [5, 10, 15, 20];

  // cpuRequest = new cpuRequest();
  pageRequest = new PagesRequest();
  pageCpu = new PageCpu();
  cpu = new Cpu();
  lstProductDetail: ProductDetail[];
  common: Common = new Common();
  lstTrangThai = this.common.lstTrangThai;

  option = new Option();
  formGroup: FormGroup;
  controlArray: Map<string, any> = new Map<string, any>();
  isFormSubmit = false;
  @ViewChild('myModal') myModal;
  private modalRef;

  constructor(private cpuService: CpuService, private modalService: ModalManager, private fb: FormBuilder,
    private productService: ProductDetailService) { }

  ngOnInit(): void {
    this.getPagecpu();
    this.formGroup = this.fb.group({
      cpu: this.fb.group({
        id: [{ value: '', disabled: true },],
        cpu: [{ value: '', }, Validators.required],
        status: [{ value: '', }, Validators.required],
      }),
    });
  }


  getPagecpu() {
    // get product
    // console.log(this.cpuRequest)
    this.cpuService.getPageCpu(this.pageRequest).subscribe(
      (data) => {
        if (data) {
          this.pageCpu = data;
          console.log(data);
          this.pageCpu.number = ++this.pageCpu.number;
          console.log(this.pageCpu);
        }
      }, (error) => {
        console.log(error);
      }
    );
  }

  handlePageSizeChange(event: any) {
    this.pageRequest.size = event.target.value;
    this.getPagecpu();
  }
  handlePageChange(event: any) {
    this.pageRequest.page = event - 1;
    this.getPagecpu();
  }

  search() {
    this.getPagecpu();
  }

  view(id) {
    console.log(id);
    this.cpuService.getCpuById(id).subscribe((respone) => {
      this.cpu = respone;
      // this.initForm(this.option);
      console.log(this.cpu);
      this.openModal(Action.CAPNHAT);
    })
  }

  setStatus(item) {
    if (item == 1) {
      return "Active";
    }
    else if (item == 0) {
      return "InActive";
    }
    else {
      return "";
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
    this.cpu = new Cpu();
  }

  save() {
    console.log(this.cpu);
    console.log(this.formGroup);
    this.isFormSubmit = true;
    if (this.formGroup.status == "INVALID") {
      return;
    }
    this.cpuService.saveCpu(this.cpu).subscribe((respone) => {
      // this.option = respone;
      if (respone) {
        Swal.fire('', '', 'success');
        this.closeModal();
        this.getPagecpu()
      } else {
        Swal.fire('', 'error', 'error')
      }
    }, (error) => {
      Swal.fire('', error, 'error')
    })
  }

  delete() {
    if (this.cpu.id) {
      this.cpuService.deleteCpu(this.cpu.id).subscribe((respone) => {
        // this.option = respone;
        Swal.fire('', '', 'success');
        this.closeModal();
        this.getPagecpu()

      }, (error) => {
        Swal.fire('', error, 'error')
      })
    }
  }

  checkButtonDelete() {
    if (this.cpu.status == 1) {
      return true;
    }
    return false;
  }

  checkButtonSave() {
    if (!this.cpu) {
      return true;
    }
    if (!this.cpu.status) {
      return true;
    }
    if (this.cpu.status == 0) {
      return false;
    }
    return true;
  }
}
