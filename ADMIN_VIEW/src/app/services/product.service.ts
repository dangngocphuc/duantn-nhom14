import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { PageProduct, PagesRequest, Product } from '../models/type';
import { HttpBaseService } from './http-base.service';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  // Url=environment.urlServer
  constructor(private http: HttpBaseService, private httpClient: HttpClient) {}
  getProducts(params): Observable<PageProduct> {
    return this.http.getByParams<PageProduct>(`/product`, params);
  }
  getProductById(id): Observable<any> {
    return this.http.get<any>(`/product/` + id, null);
  }
  deleteProduct(id): Observable<any> {
    return this.http.delete<any>(`/product/delete/` + id);
  }
  saveProduct(product): Observable<any> {
    return this.http.post<any>(`/product/save`, product);
  }

 postImage(file): Observable<any> {
    // const data: FormData = new FormData();
    // data.append('file', file);
    return this.http.postImage<any>(`/product/upload`, file);
  }

  getListProductDetail(params): Observable<PageProduct> {
    return this.http.get<PageProduct>(`/product`, params);
  }

  public ngSelect(page: PagesRequest, data): Observable<any> {
    let query = {};
    if (page.page) query['page'] = page.page;
    if (page.size) query['size'] = page.size;
    query['sort'] = 'id,desc';
    if (data && data.productName && data.productName != '')
      query['productName'] = data.productName.toString();
    let params = new HttpParams({ fromObject: query });
    // this.getOption();
    // this.httpOption['params'] = params;
    return this.http.get<any>(`/product/ngselect`, params);
  }

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

  viewImage(id): Observable<Blob> {
    let httpOption = {
      responseType: 'blob' as 'json',
      headers: this.getHeader()
    };
    return this.httpClient.get<Blob>(`/api/product/view/` + id, httpOption);
  }

  uploadFile(files: FileList) {
    let formData = new FormData();
    Array.from(files).forEach(file => { formData.append("file", file) });
    // return this.http.post<FileResponse[]>(`${this.urlUploadFile}`, formData,true);
  }

}
