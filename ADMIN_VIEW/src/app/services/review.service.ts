import { HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpBaseService } from './http-base.service';

@Injectable({
  providedIn: 'root',
})
export class ReviewService {
  constructor(private http: HttpBaseService) {}
  getReviews(params: any): Observable<any> {
    return this.http.getByParams<any>(`/user/reviews`, params);
  }
  deleteReview(id): Observable<any> {
    return this.http.delete<any>(`/user/review/` + id);
  }
  saveReview(review): Observable<any> {
    return this.http.post<any>(`/user/review`, review);
  }
}
