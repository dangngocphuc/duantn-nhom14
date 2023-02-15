import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpBaseService } from './http-base.service';

@Injectable({
  providedIn: 'root',
})
export class ProductVariantService {
  // Url=environment.urlServer
  constructor(private http: HttpBaseService) {}
//   getProducts(params): Observable<any> {
//     return this.http.get<any>(`/product`, params);
//   }
//   getProduct(id): Observable<any> {
//     return this.http.get<any>(`/product/` + id, null);
//   }
//   deleteProduct(id): Observable<any> {
//     return this.http.delete<any>(`/product/delete/` + id);
//   }
  saveProduct(product): Observable<any> {
    return this.http.post<any>(`/productVariant/save`, product);
  }
//   postImage(id: number, file: File): Observable<any> {
//     const data: FormData = new FormData();
//     data.append('file', file);
//     return this.http.postImage<any>(`/product/upload_image/${id}`, data);
//   }
}
