import { NG_VALUE_ACCESSOR, ControlValueAccessor } from '@angular/forms';
import { Directive, ElementRef, forwardRef, AfterViewInit, NgZone, Component, Input, SimpleChange, OnChanges, ViewChild, Output, EventEmitter } from '@angular/core';
import { Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { DatePipe } from '@angular/common';
import * as moment from 'moment';
declare var $: any;

export const EDITOR_VALUE_ACCESSOR: any = {
  provide: NG_VALUE_ACCESSOR,
  useExisting: forwardRef(() => NgDatepickerComponent),
  multi: true
};

@Component({
  selector: 'datepicker',
  templateUrl: './ng-datepicker.component.html',
  styleUrls: ['./ng-datepicker.component.css'],
  providers: [EDITOR_VALUE_ACCESSOR,DatePipe]
})
export class NgDatepickerComponent implements AfterViewInit, ControlValueAccessor {
  constructor(private datePipe: DatePipe) {
  }
  inputId= '_' + Math.random().toString(36).substr(2, 9);
  @Input()
  name: string;
  @Input('value')
  val: string;
  @Input()
  styleClass: string;
  @Input()
  label: string;
  @Input("disabled")disabled:boolean=false;

  @Output() blur = new EventEmitter<string>();

  changeValue(value: string) {
    this.blur.emit(value);
  }

  @Input('placeholder')
  placeholder: string;
  $dp1
  onChange: any = () => { };
  onTouched: any = () => { };
  get value() {
    return this.val;
  }
  set value(val) {
    this.val = val;
    let date = moment(this.val,["DD/MM/YYYY", moment.ISO_8601])
    if(date){
      this.onChange(date.utc().toDate());
      this.onTouched();
    }
  }
  registerOnChange(fn) {
    this.onChange = fn;
  }
  registerOnTouched(fn) { 
    this.onTouched = fn;
  }
  writeValue(value) {
    if (value) {
      var date=this.datePipe.transform(value, 'dd/MM/yyyy');
      this.val = date;
      if(this.$dp1)
      this.$dp1.datepicker('set', this.val)
    }
  }
  ngAfterViewInit(){
    let datepicker=this
    datepicker.$dp1=$('#'+this.inputId).datepicker({
        format: 'DD/MM/YYYY',
        autoHide: true,
    });
    if(datepicker.val)
    datepicker.$dp1.datepicker('set', datepicker.val)
    datepicker.$dp1.on('changeDate', function (ev) {
      var date = datepicker.$dp1.datepicker('get')
      if(date && date.isValid()){
        datepicker.onChange(date.utc().toDate());
      }else{
        datepicker.onChange(null);
      }
      datepicker.onTouched();
    });
  }
}
