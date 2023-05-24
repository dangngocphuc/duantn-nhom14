
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ModalManager } from 'ngb-modal';
import { Action, Common } from 'src/app/commons/common';
import { Option, OptionRequest, OptionValue, PageOption, PagesRequest } from 'src/app/models/type';
import { OptionService } from 'src/app/services/option.service';
// import { employees } from 'src/app/models/type';
import { ProductService } from 'src/app/services/product.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-icons',
  templateUrl: './icons.component.html',
  styleUrls: ['./icons.component.scss']
})
export class IconsComponent implements OnInit {

  isVisible = false;
  closeResult = '';
  action: Action;
  isEdit = false;
  isInsert = false;
  isView = false;
  Action = Action;
  pageSizes = [5, 10, 15, 20];

  pageRequest = new PagesRequest();
  optionRequest = new OptionRequest();

  pageOption = new PageOption();
  option = new Option();
  formGroup: FormGroup;
  controlArray: Map<string, any> = new Map<string, any>();
  isFormSubmit = false;
  @ViewChild('myModal') myModal;
  private modalRef;

  common: Common = new Common();
  lstTrangThai = this.common.lstTrangThai;

  constructor(private optionService: OptionService, private modalService: ModalManager, private fb: FormBuilder) { }

  ngOnInit() {
    // this.gridView = this.gridData;
    this.getOptions();
    this.formGroup = this.fb.group({
      option: this.fb.group({
        code: [{ value: '' }, Validators.required],
        name: [{ value: '', }, Validators.required],
        status: [{ value: '', }, Validators.required]
      }),
      listOptinValue: this.fb.array([ {disabled: true} ], Validators.required),
    });
    this.getListOptinValue
  }

  getOptions() {
    // get product
    console.log(this.pageRequest)
    this.optionService.getOptions(this.pageRequest, this.optionRequest).subscribe(
      (data) => {
        if (data) {
          this.pageOption = data;
          this.pageOption.number = ++this.pageOption.number;
          console.log(this.pageOption);
        }
      }, (error) => {

      }
    );
  }
  handlePageSizeChange(event: any) {
    this.pageRequest.size = event.target.value;
    this.getOptions();
  }
  handlePageChange(event: any) {
    this.pageRequest.page = event - 1;
    this.getOptions();
  }

  search() {
    this.getOptions();
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

  view(id) {
    this.optionService.getOptionById(id).subscribe((respone) => {
      this.option = respone;
      this.initForm(this.option);
      console.log(this.option)
      this.openModal(Action.CAPNHAT);
    })
  }

  save() {
    console.log(this.formGroup);
    this.isFormSubmit = true;
    if (this.formGroup.status == "INVALID") {
      return;
    }
    console.log(this.option)
    this.optionService.saveOption(this.option).subscribe((respone) => {
      // this.option = respone;
      this.closeModal();
      this.getOptions();
    }, (error) => {

    })
  }

  closeModal() {
    this.modalService.close(this.modalRef);
    this.isFormSubmit = false;
    this.getListOptinValue.clearValidators();
    this.getListOptinValue.clear();
    // this.formDangKy.get('listDnvtHosoUyquyenGt').clear();
    this.option.listOptionValue = [];
    this.option = new Option();
  }

  setStatus(item) {
    if (item == 1) {
      return "Active";
    }
    else {
      return "Inactive";
    }
  }

  get getListOptinValue() {
    return this.formGroup.controls['listOptinValue'] as FormArray;
  }

  initForm(option?: Option) {
    this.formGroup = this.fb.group({
      option: this.fb.group({
        code: [{ value: '' }, Validators.required],
        name: [{ value: '', }, Validators.required],
        status: [{ value: '', }, Validators.required]
      }),
      listOptinValue: this.fb.array([], Validators.required),
    });
    if (option?.listOptionValue?.length && option?.listOptionValue?.length > 0) {
      option?.listOptionValue?.forEach((item, index) => {
        this.addRow(item);
      });
    }
    // if (action === 1) {
    //   this.addRow();
    // }
  }

  async addRow(item: OptionValue = new OptionValue()) {
    if (!item.id) {
      this.option.listOptionValue.push(item);
    }
    await this.getListOptinValue.push(this.fb.group({
      optionValue: [{ value: item?.optionValue }, Validators.required],
    }));
  }


  removeRow(index) {
    // this.listLoaiDichVu = [];
    // this.listDnvtDmNccNhtmNccdvValue = [];
    this.getListOptinValue.removeAt(index);
    this.option.listOptionValue = this.option.listOptionValue.filter((item, i) => i !== index);
  }


  delete() {
    Swal.fire({
      title: 'Do you want to Delete?',
      showDenyButton: true,
      showCancelButton: true,
      confirmButtonText: 'yes',
      // denyButtonText: `Nos`,
    }).then((result) => {
      /* Read more about isConfirmed, isDenied below */
      if (result.isConfirmed) {
        this.optionService.deleteOption(this.option.id).subscribe((res) => {
          if(res){
            this.closeModal();
            this.getOptions();
          }
        })
      }
    })

  }
}
