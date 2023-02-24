import { Component, OnInit } from '@angular/core';
import { Product } from './entity/Product';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})


export class AppComponent implements OnInit{
  // title = 'helperFrontend';
  // public show = true;
  // compare: Product[] = [];
  // totalProduct;
  // listOfProduct: Product[] = [];

  constructor(private modalService: NgbModal) {

  }
  ngOnInit(): void {
    // const compare = localStorage.getItem('compare') || '';
    //  if (compare) {
    //    this.compare = JSON.parse(compare);
    //    this.totalProduct = this.compare.length;
    //  }
  }

  // delProduct(id) {
  //   this.listOfProduct = this.listOfProduct.filter(
  //     (element) => element.productID != id
  //   );
  //   this.updateCompare();
  //   window.location.reload();
  // }

  // updateCompare() {
  //   localStorage.setItem('compare', JSON.stringify(this.compare));
  // }
}
