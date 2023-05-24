import { Component, OnInit, ViewChild } from '@angular/core';
import { ModalDismissReasons, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ModalManager } from 'ngb-modal';
import { Action, Common } from 'src/app/commons/common';
import { Brand, Image, Imei, Option, PageProduct, PagesRequest, Product, ProductOption } from 'src/app/models/type';
import { ProductService } from 'src/app/services/product.service';
import * as ClassicEditor from '@ckeditor/ckeditor5-build-classic';
import { catchError, concat, debounceTime, distinctUntilChanged, Observable, of, Subject, switchMap, tap } from 'rxjs';
import { OptionService } from 'src/app/services/option.service';
import { clippingParents } from '@popperjs/core';
import { BrandService } from 'src/app/services/brand.service';
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { OwlOptions } from 'ngx-owl-carousel-o';
import Swal from 'sweetalert2';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';

@Component({
  selector: 'app-tables',
  templateUrl: './tables.component.html',
  styleUrls: ['./tables.component.scss']
})
export class TablesComponent implements OnInit {

  controlArray: Map<string, any> = new Map<string, any>();
  // modalRef: any;
  isVisible = false;
  closeResult = '';
  action: Action;
  isEdit = false;
  isInsert = false;
  isView = false;
  Action = Action;
  pageProduct: PageProduct;
  product = new Product();
  listOption: Option[];
  common: Common = new Common();
  lstTrangThai = this.common.lstTrangThai
  listBrand: Brand[];
  imageSrc: string = '';
  linkImage: String;
  formGroup: FormGroup;
  @ViewChild('myModal') myModal;

  pageSizes = [5, 10, 15, 20];

  pageRequest = new PagesRequest();
  imageProduct: any;

  private modalRef;
  constructor(private productService: ProductService, private optionService: OptionService, private brandService: BrandService, private fb: FormBuilder,
    private modalService: ModalManager,private sanitizer: DomSanitizer) { }

  public Editor = ClassicEditor;

  lstOption: Observable<any[]>;
  loadlstOption = false;
  textInput_tenOption$ = new Subject<string>();
  myForm: any;
  imagess:any;
  ngOnInit() {
    this.getProducts(this.pageRequest.page, this.pageRequest.size);
    this.getListBrand();
    this.imagess = [944, 1011, 984].map((n) => `https://picsum.photos/id/${n}/900/500`);
    this.formGroup = this.fb.group({
      product: this.fb.group({
        code: [{ value: this.product.maSanPham, }, Validators.required],
        name: [{ value: this.product.tenSanPham, }, Validators.required],
        brand: [{ value: this.product.brand, }, Validators.required],
        status: [{ value: this.product.status, }, Validators.required],
        // option: [{ value: '', }, Validators.required],
      }),
      // listImage: this.fb.array([], Validators.required),
    });
  }

  getListBrand() {
    this.brandService.getListBrand().subscribe(
      (data) => {
        if (data) {
          this.listBrand = data;
          console.log(this.pageProduct);
        }
      },
      (error) => { }
    );
  }

  save() {
    // Swal.fire({
    //   title: "Đang thực hiện",
    //   html: '',
    //   allowOutsideClick: false,
    //   allowEscapeKey: false,
    //   didOpen: () => {
    //     Swal.showLoading()
    //   },
    // })
    debugger;
    console.log(this.formData);
    this.imgURL = [];
    this.productService.postImage(this.formData).subscribe((res) => {
      this.product.listImage = res;
      this.productService.saveProduct(this.product).subscribe((respone) => {
        if (respone == 1) {
          Swal.fire('success', '', 'success');
          this.closeModal();
          this.getProducts(this.pageRequest.page, this.pageRequest.size);   
        }
      });
      // }
    },
      (err) => {
        // debugger;
        Swal.fire('err:' + err,'','error');
      });

    // debugger;
    // console.log(this.product);
    // this.productService.saveProduct(this.product, this.formData).subscribe((res) => {
    //   this.closeModal();
    //   this.getProducts();
    // })
  }

  getProducts(pageIndex: number, pageSize: number) {
    this.controlArray.set('pageIndex', pageIndex);
    this.controlArray.set('pageSize', pageSize);
    // get product
    console.log(this.controlArray);
    this.productService.getProducts(this.controlArray).subscribe(
      (data) => {
        if (data) {
          this.pageProduct = data;
          this.pageProduct.number = ++this.pageProduct.number;
          console.log(this.pageProduct);
        }
      },
      (error) => { }
    );
  }

  openModal(action) {
    this.loadOption();
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
    this.imgURL = [];
    this.productService.getProductById(id).subscribe((respone) => {
      debugger;
      this.product = respone;
      if(this.product.listImage.length>0){
        this.product.listImage.forEach((e)=>{
          // this.addRow(e);
          this.imgURL.push("../../assets/" + e.image);
        })
      }
      // this.listOption = listProductTemp;
      console.log(this.imgURL);
      console.log(this.product)
      this.openModal(Action.CAPNHAT);
    })
  }

  closeModal() {
    this.listOption = [];
    this.imageObject = [];
    this.imgURL = [];
    this.product = new Product();
    this.modalService.close(this.modalRef);
  }

  onFileImageSelect(e) {
  }

  loadOption() {
    this.lstOption = concat(
      this.optionService.ngSelect(new PagesRequest(0, 10), null), // default items
      this.textInput_tenOption$.pipe(
        debounceTime(500),
        distinctUntilChanged(),
        tap(() => (this.loadlstOption = true)),
        switchMap((term) =>
          this.optionService
            .ngSelect(new PagesRequest(0, 10), { optionTen: term })
            .pipe(
              catchError(() => of([])), // empty list on error
              tap(() => (this.loadlstOption = false))
            )
        )
      )
    );
  }

  // save() {
  //   console.log(this.product.listImage);
  //   // return;
  //   let listProductOptionTemp = [];
  //   console.log(this.listOption);
  //   debugger;
  //   // this.listOption.forEach((e) => {
  //   //   // debugger;
  //   //   // let op = new Option();
  //   //   // op.id = Number(e);
  //   //   let productOption = new ProductOption();
  //   //   productOption.option = e;
  //   //   listProductOptionTemp.push(productOption);
  //   // })
  //   // this.product.listProductOption = listProductOptionTemp;
  //   console.log(this.product);
  //   this.productService.saveProduct(this.product).subscribe((res) => {
  //     console.log(res);
  //     this.closeModal();
  //     this.getProducts(this.pageRequest.page, this.pageRequest.size);
  //   })
  // }

  get f() {
    return this.myForm.controls;
  }
  
  // async addRow(item: Image = new Image()) {
  //   // debugger;
  //   if (!item.id) {
  //     this.product.listImage.push(item);
  //   }
  //   await this.getListImage.push(this.fb.group({
  //     image: [{ value: item?.image}],
  //     thumbImage: [{ value: item?.thumbImage }],
  //   }));
  // }

  // get getListImage() {
  //   return this.formGroup.controls['listImage'] as FormArray;
  // }

  imageObject:Array<Image> = [];
  convertImage(){
    debugger;
    this.imageObject = [];
    this.product.listImage.forEach(e=>{
      this.imageObject.push(e);
    })
    console.log(this.imageObject);
  }

  handlePageSizeChange(event: any) {
    this.pageRequest.size = event.target.value;
    this.getProducts(this.pageRequest.page, this.pageRequest.size);
  }

  handlePageChange(event: any) {
    this.pageRequest.page = event - 1;
    this.getProducts(this.pageRequest.page, this.pageRequest.size);
  }

  public imagePath;
  imgURL = [];
  public message: string;
  formData = new FormData();
  preview(files) {
    this.formData = new FormData();
    debugger;
    this.imgURL = [];
    if (files.length === 0)
      return;
    for (var file of files) {
      var mimeType = file.type;
      if (mimeType.match(/image\/*/) == null) {
        // this.message = "";
        Swal.fire('Only images are supported.', '', 'error');
        return;
      }
    }
    for (var file of files) {
      // var reader = new FileReader();
      // this.imagePath = files;
      // reader.readAsDataURL(file);
      // this.imgURL.push(reader.result);
      const reader = new FileReader();
      reader.onload = (e: any) => {
        console.log(e.target.result);
        this.imgURL.push(e.target.result);
      };
      reader.readAsDataURL(file);
    }
    // this.formData.delete
    Array.from(files).forEach((e: File) => {
      this.formData.append("file", e)
    });
    // console.log(this.formData);
    // console.log(this.imgURL);
  }

  viewImage(id) {
    this.imgURL = [];
    this.productService.viewImage(id).subscribe((response: Blob) => {
      debugger;
      // const bytes = new Uint8Array(response);
      // const imageArray = [];
      // for (let i = 0; i < bytes.length; i++) {
      //   imageArray.push(this.sanitizer.bypassSecurityTrustUrl('data:image/png;base64,' + btoa(String.fromCharCode(bytes[i]))));
      // }
      // this.imgURL = imageArray;
      // this.imageProduct  = response;
      // Swal.close();
      const imageBlob = new Blob([response], { type: 'image/jpeg' });
      const imageUrl = URL.createObjectURL(imageBlob);
      this.imgURL.push(this.sanitizer.bypassSecurityTrustUrl(imageUrl));

      // debugger;
      var mediaType = 'image/jpeg';
      var blob = new Blob([response], { type: mediaType });
      var reader = new FileReader();
      reader.readAsDataURL(blob);
      reader.onloadend = () => {
        var base64data = reader.result;
        this.imageProduct = base64data;
      };
    }, error => {
      debugger;
      console.log(error);
      // Swal.fire({
      //   position: 'center',
      //   icon: 'error',
      //   title: error?.error?.messages
      // });
    });
  }
}
