import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ModalManager } from 'ngb-modal';
import { Action, Common } from 'src/app/commons/common';
import { Rom, PageRom, PagesRequest } from 'src/app/models/type';
import { RomService } from 'src/app/services/Rom.service';

@Component({
  selector: 'app-rom',
  templateUrl: './rom.component.html',
  styleUrls: ['./rom.component.scss']
})
export class RomComponent implements OnInit {

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
  pageRom = new PageRom();
  rom = new Rom();
  // lstProductDetail : ProductDetail[];
  common: Common = new Common();
  lstTrangThai = this.common.lstTrangThai;

  option = new Option();
  formGroup: FormGroup;
  controlArray: Map<string, any> = new Map<string, any>();
  isFormSubmit = false;
  @ViewChild('myModal') myModal;
  private modalRef;

  constructor(private romService: RomService, private modalService: ModalManager, private fb: FormBuilder) { }

  ngOnInit(): void {
    this.getRom();
    this.formGroup = this.fb.group({
      rom: this.fb.group({
        id: [{ value: '' }, Validators.required],
        rom: [{ value: '', }, Validators.required],
      }),
    });
  }


  getRom() {
    // get product
    // console.log(this.RomRequest)
    this.romService.getPageRom(this.pageRequest).subscribe(
      (data) => {
        if (data) {
          this.pageRom = data;
          console.log(data);
          this.pageRom.number = ++this.pageRom.number;
          console.log(this.pageRom);
        }
      }, (error) => {
        console.log(error);
      }
    );
  }
  handlePageSizeChange(event: any) {
    this.pageRequest.size = event.target.value;
    this.getRom();
  }
  handlePageChange(event: any) {
    this.pageRequest.page = event - 1;
    this.getRom();
  }

  search() {
    this.getRom();
  }

  view(id) {
    console.log(id);
    this.romService.getRomById(id).subscribe((respone) => {
      this.rom = respone;
      // this.initForm(this.option);
      console.log(this.rom);
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
    this.rom = new Rom();
  }

}
