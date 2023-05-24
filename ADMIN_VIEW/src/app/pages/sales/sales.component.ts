import { Component, OnInit } from '@angular/core';
import { catchError, concat, debounceTime, distinctUntilChanged, Observable, of, Subject, switchMap, tap } from 'rxjs';
import { Bill, BillDetail, PagesRequest, ProductDetail, Tab, User } from 'src/app/models/type';
import { BillService } from 'src/app/services/bill.service';
import { ImeiService } from 'src/app/services/imei.service';
import { ProductDetailService } from 'src/app/services/productDetail.service';
import { UserService } from 'src/app/services/user.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-sales',
  templateUrl: './sales.component.html',
  styleUrls: ['./sales.component.scss']
})
export class SalesComponent implements OnInit {

  listBill: Bill[];
  bill = new Bill();

  lstProductDetail: ProductDetail[];
  // listOfProductDetail: ProductDetail[] = [];
  productDetail = new ProductDetail();

  lstUser: User[];
  user = new User();

  tab = new Tab();
  listTab: Tab[] = [];
  moneyCustomer: number;

  lstImei: Observable<any[]>;
  loadlstImei = false;
  textInput_imei$ = new Subject<string>();


  constructor(private productDetailService: ProductDetailService, private userService: UserService,
    private imeiService: ImeiService, private billService: BillService) {
  }

  ngOnInit(): void {
    // this.listBill.push(new Bill());
    const listTab = localStorage.getItem('listTab') || '';
    if (listTab) {
      this.listTab = JSON.parse(listTab);
      if (this.listTab) {
        this.listTab.forEach((e) => {
          e.bill.listBillDetail.forEach((e) => {
            this.loadImei(e);
          })
        })
      }
      // this.totalProduct = this.cart.length;
    } else {
      this.tab.name = 'Đơn 1'
      this.listTab.push(this.tab);
      console.log(this.listTab);
    }
    this.getListProductDetail();
    this.getListUser();
  }

  getListProductDetail() {
    this.productDetailService.getListProductDetail().subscribe((response) => {
      this.lstProductDetail = response;
    })
  }

  getListUser() {
    this.userService.getListUser().subscribe((res) => {
      // this.lstUser = res;
      this.lstUser = (res).map(item => ({ userID: item.userID, name: item.username, userPhone:item.userPhone }));
      // console.log(this.lstUser);
    })
  }

  // tabs = [{name:'Đơn 1'}];
  selectedIndex = 0;
  closeTab({ index }: { index: number }): void {
    this.listTab.splice(index, 1);
    this.listTab.forEach((e, index) => {
      e.name = 'Đơn ' + (index + 1);
    })
    this.updateCart();
    // window.location.reload();
  }

  newTab(): void {
    let tab = new Tab()
    tab.name = 'Đơn ' + (this.listTab.length + 1);
    this.selectedIndex = this.listTab.length;
    this.listTab.push(tab);
    this.updateCart();
  }

  addProduct(tab) {
    debugger;
    console.log(tab);
    console.log(this.listTab);

    // console.log(object);
    // this.loadImei(tab.)
    let duplicate = false;
    if (tab?.bill?.listBillDetail.length > 0) {
      tab?.bill?.listBillDetail?.forEach((ele) => {
        if (ele.productDetail.id == tab.bill.productDetailValue.id) {
          duplicate = true;
        }
      });
    }
    if (!duplicate) {
      debugger;
      let billDetail = new BillDetail();
      tab.bill.productDetailValue.quanlityBuy = 1;
      billDetail.productDetail = tab.bill.productDetailValue;
      billDetail.quantity = tab.bill.productDetailValue.quanlityBuy;
      billDetail.price = tab.bill.productDetailValue.productPrice;
      tab.bill.listBillDetail.push(billDetail);
      tab.bill.listBillDetail.forEach((e) => {
        this.loadImei(e);
      })
      console.log(tab);
      this.updateCart();
    }

    else {
      debugger;
      tab.bill.listBillDetail.forEach((ele) => {
        console.log(ele);
        if (ele.productDetail.id == tab.bill.productDetailValue.id) {
          if (ele.productDetail.quanlityBuy == ele.productDetail.quantity) {
            Swal.fire('', 'sản phẩm hết hàng', 'warning')
          } else {
            ele.productDetail.quanlityBuy = Number(ele.productDetail.quanlityBuy) + 1;
            ele.quantity = ele.productDetail.quanlityBuy;
            this.updateCart();
          }
        }
      });
    }
  }

  changeQuanlity(e, item) {
    // console.log(index);
    debugger;
    console.log(item);
    console.log(e?.target?.value);
    if (e?.target?.value > item.productDetail.quantity) {
      item.productDetail.quanlityBuy = item.productDetail.quantity;
      item.quantity = item.productDetail.quanlityBuy;
    } else if (e?.target?.value < 0) {
      item.productDetail.quanlityBuy = 1;
      item.quantity = item.productDetail.quanlityBuy;
    }
    else {
      item.productDetail.quanlityBuy = e?.target?.value;
      item.quantity = item.productDetail.quanlityBuy;
    }
    this.updateCart();
  }


  updateCart() {
    this.listTab.forEach((e) => {
      e.bill.total = 0;
      e.bill.totalProduct = 0;
      e.bill.listBillDetail.forEach((element) => {
        e.bill.total += element.productDetail.quanlityBuy * element.productDetail.productPrice;
        e.bill.totalProduct += Number(element.productDetail.quanlityBuy);
      })
    })
    localStorage.setItem('listTab', JSON.stringify(this.listTab));
  }

  delProduct(tab, id) {
    debugger;
    // this.listTab.forEach((e) => {
    //   // e.bill.total = 0;
    //   e.bill.listBillDetail =  e.bill.listBillDetail.filter(
    //     (element) => element.id != id
    //   );
    // })
    tab.bill.listBillDetail = tab.bill.listBillDetail.filter(
      (element) => element.productDetail.id != id
    );
    // this.listOfProductDetail = this.listOfProductDetail.filter(
    //   (element) => element.id != id
    // );
    this.updateCart();
    // window.location.reload();
  }

  // payment() {
  //   // console.log(this.newBill);
  //   debugger;
  //   let listBillDetailTemp = []
  //   this.listOfProductDetail.forEach((e) => {
  //     let billDetail = new BillDetail();
  //     // e.listImei = e.listImei.filter((e) => e.status == 1)
  //     billDetail.productDetail = e;
  //     billDetail.quantity = e.quanlityBuy;
  //     billDetail.price = e.productPrice;
  //     // billDetail.listImei = e.listImei.slice(0, e.quanlityBuy);
  //     listBillDetailTemp.push(billDetail);
  //   })
  //   this.newBill.total = this.totalPrice;
  //   this.newBill.listBillDetail = listBillDetailTemp;
  //   console.log(this.newBill);
  //   this.saveBill(this.newBill);
  // }

  loadImei(e) {
    console.log(e.productDetail.id);
    this.lstImei = concat(
      this.imeiService.getListImeiByProductDetail(new PagesRequest(0, 10), { imei: null, productId: e.productDetail.id }), // default items
      this.textInput_imei$.pipe(
        debounceTime(500),
        distinctUntilChanged(),
        tap(() => (this.loadlstImei = true)),
        switchMap((term) =>
          this.imeiService
            .getListImeiByProductDetail(new PagesRequest(0, 10), { imei: term, productId: e.productDetail.id })
            .pipe(
              catchError(() => of([])), // empty list on error
              tap(() => (this.loadlstImei = false))
            )
        )
      )
    );
    this.lstImei.subscribe(data => {
      e.listImeiValue = data;
    })
  }

  payment(tab, i) {
    console.log(tab.bill);
    if (tab.bill.listBillDetail) {
      let check = true;
      tab.bill.listBillDetail.forEach((e) => {
        if (e.listImei.length != e.quantity) {
          Swal.fire('Lưu thất bại', 'Nhập Imei bằng với số lượng sản phẩm', 'warning')
          return check = false;
        }
      })
      if (check) {
        tab.bill.status = "Delivered";
        tab.bill.payment = "Live";
        this.saveBill(tab, i);
      }

    }
  }

  saveBill(tab, index) {
    debugger;
    // tab.bill.user
    console.log(tab.bill.user);
    this.billService.paymentBill(tab.bill).subscribe(
      (data) => {
        if (data) {
          Swal.fire('Xử lý thành công', '', 'success');
          this.listTab.splice(index, 1);
          this.listTab.forEach((e, index) => {
            e.name = 'Đơn ' + (index + 1);
          })
          this.updateCart();
          // this.handleOk();
          window.location.reload();
        }else{
          Swal.fire('Xử lý thất bại', '', 'error');
        }
      },
      (error) => {
        Swal.fire('', error, 'error');
      }
    );
  }

  changeUser(tab) {
    tab.bill.name = null;
    tab.bill.phone = null;
    if (tab) {
      console.log(tab);
      if (tab?.bill?.user) {
        tab.bill.name = tab.bill.user.name;
        tab.bill.phone = tab.bill.user.userPhone;
      }
    }
  }
}

