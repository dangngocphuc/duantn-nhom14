import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { PageProduct, PageProductDetail, Product, ProductDetail } from '../models/type';
import { HttpBaseService } from './http-base.service';

@Injectable({
  providedIn: 'root',
})
export class ProductDetailService {
  // Url=environment.urlServer
  constructor(private http: HttpBaseService) {}

  getPageProductDetail(params): Observable<PageProductDetail> {
    return this.http.get<PageProductDetail>(`/product/detail/search`, params);
  }

  getProductDetailById(id): Observable<any> {
    return this.http.get<any>(`/product/detail/` + id, null);
  }

  saveProductDetail(product): Observable<any> {
    return this.http.post<any>(`/product/detail/save`, product);
  }
  
  getListProductDetail(params?): Observable<ProductDetail[]> {
    return this.http.get<ProductDetail[]>(`/product/detail/list`, params);
  }
}
