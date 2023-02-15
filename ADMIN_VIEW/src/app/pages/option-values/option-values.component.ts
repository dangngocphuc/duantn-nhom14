import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ModalManager } from 'ngb-modal';
import { Action } from 'src/app/commons/common';
import { OptionValue, OptionValueRequest, PageOptionValue, PagesRequest } from 'src/app/models/type';
import { OptionValueService } from 'src/app/services/optionvalue.service';

@Component({
  selector: 'app-option-values',
  templateUrl: './option-values.component.html',
  styleUrls: ['./option-values.component.scss']
})
export class OptionValuesComponent implements OnInit {

  isVisible = false;
  closeResult = '';
  action: Action;
  isEdit = false;
  isInsert = false;
  isView = false;
  Action = Action;
  pageSizes = [5, 10, 15, 20];

  pageRequest = new PagesRequest();
  optionValueRequest = new OptionValueRequest();

  pageOptionValue = new PageOptionValue();
  optionValue = new OptionValue();
  formGroup: FormGroup;

  isFormSubmit = false;
  @ViewChild('myModal') myModal;
  private modalRef;

  constructor(private optionService: OptionValueService, private modalService: ModalManager, private fb: FormBuilder) { }

  ngOnInit() {
    // this.gridView = this.gridData;
    this.getOptions();

    this.formGroup = this.fb.group({
      code: new FormControl({ value: '', }, [Validators.required]),
      name: new FormControl({ value: '', }, [Validators.required]),
      status: new FormControl({ value: '', }, [Validators.required]),
    });
  }

  getOptions() {
    // get product
    console.log(this.pageRequest)
    this.optionService.getOptions(this.pageRequest,this.optionValueRequest).subscribe(
      (data) => {
        if (data) {
          this.pageOptionValue = data;
          this.pageOptionValue.number = ++this.pageOptionValue.number;
          console.log(this.pageOptionValue);
        }
      }, (error) => {

      }
    );
  }
  handlePageSizeChange(event: any){
    this.pageRequest.size = event.target.value;
    this.getOptions();
  }
  handlePageChange(event: any){
    this.pageRequest.page = event - 1;
    this.getOptions();
  }

  search(){
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

  view(id){
    this.optionService.getOptionById(id).subscribe((respone)=>{
      this.optionValue = respone;
      console.log(this.optionValue)
      this.openModal(Action.CAPNHAT);
    })
  }

  save() {
    console.log(this.formGroup);
    this.isFormSubmit = true;
    if(this.formGroup.status == "INVALID"){
      return;
    }
    this.optionService.saveOption(this.optionValue).subscribe((respone) => {
      this.optionValue = respone;
      this.getOptions();
      this.closeModal();
    }, (error) => {

    })
  }

  closeModal() {
    this.modalService.close(this.modalRef);
  }

  setStatus(item){
    if(item == 1){
      return "Active";
    }
    else{
      return "Inactive";
    }
  }

}
