import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NzNotificationService } from 'ng-zorro-antd/notification';
import { Product } from 'src/app/entity/Product';
import { ProductDetail } from 'src/app/models/type';
import { ProductDetailService } from 'src/app/services/productDetail.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-user-layout',
  templateUrl: './user-layout.component.html',
  styleUrls: ['./user-layout.component.css']
})
export class UserLayoutComponent implements OnInit {

  title = 'frontend';
  public show = true;
  compare: ProductDetail[] = [];
  totalProduct;
  listOfProduct: ProductDetail[] = [];

  constructor(private modalService: NgbModal, private productDetailService: ProductDetailService, private notification: NzNotificationService) {

  }
  ngOnInit(): void {
    const compare = localStorage.getItem('compare') || '';
    if (compare) {
      this.compare = JSON.parse(compare);
      console.log(this.compare);
      this.totalProduct = this.compare.length;
    }
    console.log(this.listOfProduct);
  }

  createNotification(type: string, title: string, message: string): void {
    this.notification.create(type, title, message);
  }

  delProduct(id) {
    debugger;
    this.compare = this.compare.filter(
      (element) => element.id != id
    );
    this.updateCompare();
    window.location.reload();
    // console.log( this.listOfProduct);
  }

  updateCompare() {
    localStorage.setItem('compare', JSON.stringify(this.compare));
  }

  compareProuct() {
    if (this.compare.length > 1) {
      this.productDetailService.compareProductDetail(this.compare).subscribe((data) => {
        console.log(data);
        // this.createNotification('sucsses',,'')
        Swal.fire('',data.message,'success')
      })
    }
  }
}
