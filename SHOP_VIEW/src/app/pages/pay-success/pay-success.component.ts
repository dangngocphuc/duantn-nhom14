import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ResponseVnpay } from 'src/app/models/type';
import { BillService } from 'src/app/services/bill.service';

@Component({
  selector: 'app-pay-success',
  templateUrl: './pay-success.component.html',
  styleUrls: ['./pay-success.component.css']
})
export class PaySuccessComponent implements OnInit {

  isLoading: boolean = false;
  response = new ResponseVnpay();

  constructor(
    private billService: BillService,
    private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      if (params['vnp_Amount']) {
        this.billService.paymentBillByVnpayResponse(params).subscribe((data) => {
          if (data) {
            this.response = data;
            if (this.response.errorCode == '00') {
              this.isLoading = true;
              localStorage.removeItem('cart');
              localStorage.removeItem('total');
              localStorage.removeItem('promotion');
            } else {
              this.isLoading == false;
              console.log(this.response);
            }
          }
        })
      } else {
        this.isLoading = true;
      }
    });

  }

}
