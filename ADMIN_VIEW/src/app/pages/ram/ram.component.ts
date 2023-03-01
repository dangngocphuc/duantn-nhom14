import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ModalManager } from 'ngb-modal';
import { Action, Common } from 'src/app/commons/common';
import { Ram, PageRam, PagesRequest } from 'src/app/models/type';
import { RamService } from 'src/app/services/Ram.service';
import Swal from 'sweetalert2';


@Component({
  selector: 'app-ram',
  templateUrl: './ram.component.html',
  styleUrls: ['./ram.component.scss']
})
export class RamComponent implements OnInit {

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
  pageRam = new PageRam();
  ram = new Ram();
  // lstProductDetail : ProductDetail[];
  common: Common = new Common();
  lstTrangThai = this.common.lstTrangThai;

  option = new Option();
  formGroup: FormGroup;
  controlArray: Map<string, any> = new Map<string, any>();
  isFormSubmit = false;
  @ViewChild('myModal') myModal;
  private modalRef;

  constructor(private ramService: RamService, private modalService: ModalManager, private fb: FormBuilder) { }

  ngOnInit(): void {
    this.getPageRam();
    this.formGroup = this.fb.group({
      ram: this.fb.group({
        id: [{ value: '' }],
        ram: [{ value: '', }, Validators.required],
        status: [{ value: '', }, Validators.required],
      }),
    });
  }


  getPageRam() {
    // get product
    // console.log(this.RamRequest)
    this.ramService.getPageRam(this.pageRequest).subscribe(
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
    this.getPageRam();
  }
  handlePageChange(event: any) {
    this.pageRequest.page = event - 1;
    this.getPageRam();
  }

  search() {
    this.getPageRam();
  }

  view(id) {
    console.log(id);
    this.ramService.getRamById(id).subscribe((respone) => {
      this.ram = respone;
      // this.initForm(this.option);
      console.log(this.ram);
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
    this.ram = new Ram();
  }

  save() {
    debugger;
    console.log(this.ram);
    console.log(this.formGroup);
    this.isFormSubmit = true;
    if (this.formGroup.status == "INVALID") {
      return;
    }
    this.ramService.saveRam(this.ram).subscribe((respone) => {
      // this.option = respone;
      if (respone) {
        Swal.fire('', '', 'success');
        this.closeModal();
        this.getPageRam()
      }
    }, (error) => {
      Swal.fire('', error, 'error')
    })
  }
}
