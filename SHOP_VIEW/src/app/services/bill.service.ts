import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { HttpBaseService } from './http-base.service';

@Injectable({
  providedIn: 'root',
})
export class BillService {

  Url = environment.urlServer
  constructor(private http: HttpBaseService, private httpClient: HttpClient) { }
  // Url=environment.urlServer
  getHeader(): HttpHeaders {
    var headers = { 'Content-Type': 'application/json' };
    if (localStorage) {
      var authorization = localStorage.getItem('Authorization');
      if (authorization) {
        headers['Authorization'] = authorization;
      }
    }
    return new HttpHeaders(headers);
  }

  getProducts(params): Observable<any> {
    return this.http.get<any>(`/product`, params);
  }
  getBills(params): Observable<any> {
    return this.http.get<any>(`/bill`, params);
  }

  getBillDetail(id): Observable<any> {
    return this.http.get<any>(`/bill-detail/` + id, null);
  }

  saveBill(bill): Observable<any> {
    return this.http.post<any>(`/bill/payment`, bill);
  }

  paymentBillByVnpay(bill): Observable<any> {
    return this.http.post<any>(`/bill/payment/vnpay`, bill);
  }

  paymentBillByVnpayResponse(param): Observable<any> {
    let query = {}
    if (param.vnp_Amount) query['vnp_Amount'] = param.vnp_Amount
    if (param.vnp_BankCode) query['vnp_BankCode'] = param.vnp_BankCode
    if (param.vnp_CardType) query['vnp_CardType'] = param.vnp_CardType
    if (param.vnp_OrderInfo) query['vnp_OrderInfo'] = param.vnp_OrderInfo
    if (param.vnp_PayDate) query['vnp_PayDate'] = param.vnp_PayDate
    if (param.vnp_SecureHash) query['vnp_SecureHash'] = param.vnp_SecureHash
    if (param.vnp_TmnCode) query['vnp_TmnCode'] = param.vnp_TmnCode
    if (param.vnp_TransactionNo) query['vnp_TransactionNo'] = param.vnp_TransactionNo
    if (param.vnp_TransactionStatus) query['vnp_TransactionStatus'] = param.vnp_TransactionStatus
    if (param.vnp_TxnRef) query['vnp_TxnRef'] = param.vnp_TxnRef
    if (param.vnp_ResponseCode) query['vnp_ResponseCode'] = param.vnp_ResponseCode
    let params = new HttpParams({ fromObject: query });
    var httpOptions: Object = {
    };
    httpOptions["params"] = params;
    // return this.httpClient.get<any>(this.Url + `/product/search`, httpOptions);
    return this.httpClient.get<any>(this.Url + `/bill/payment/results`, httpOptions);
  }
}
