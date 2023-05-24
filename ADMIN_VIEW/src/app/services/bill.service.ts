import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpBaseService } from './http-base.service';

@Injectable({
  providedIn: 'root',
})
export class BillService {
  constructor(private http: HttpBaseService) {}
  getBills(params): Observable<any> {
    return this.http.getByParams<any>(`/bill`, params);
  }
  exportBills(params): Observable<any> {
    return this.http.exportExcel<any>(`/bill`, params);
  }
  getBillDetail(id): Observable<any> {
    return this.http.getByParams<any>(`/bill-detail/` + id, null);
  }
  deleteBill(id): Observable<any> {
    return this.http.delete<any>(`/bill/` + id);
  }
  cancelBill(id): Observable<any> {
    return this.http.get<any>(`/bill/cancel/` + id, null);
  }
  saveBill(bill): Observable<any> {
    return this.http.post<any>(`/bill/save`, bill);
  }
  getStatisticBillByWeek(params): Observable<any> {
    return this.http.get<any>(`/bill/statistic/week`, params);
  }
  getStatisticBillByMonth(params): Observable<any> {
    return this.http.get<any>(`/bill/statistic/month`, params);
  }

  paymentBill(bill): Observable<any> {
    return this.http.post<any>(`/bill/payment`, bill);
  }

  getBillById(id): Observable<any> {
    return this.http.get<any>(`/bill/` + id,null);
  }

  getStatisticBillByUser(): Observable<any> {
    return this.http.get<any>(`/bill/statistic/user`, null);
  }
}
