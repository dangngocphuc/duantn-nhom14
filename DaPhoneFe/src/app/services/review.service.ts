import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpBaseService } from './http-base.service';

@Injectable({
  providedIn: 'root',
})
export class ReviewService {
  constructor(private http: HttpBaseService) {}

  getReviews(params): Observable<any> {
    return this.http.get<any>(`/user/reviews`, params);
  }

 
  saveReview(review): Observable<any> {
    return this.http.post<any>(`/user/review`, review);
  }
}
