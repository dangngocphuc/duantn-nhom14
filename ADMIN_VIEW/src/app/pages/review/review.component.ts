import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { NzMessageService } from 'ng-zorro-antd/message';
import { NzModalService } from 'ng-zorro-antd/modal';
import { NzNotificationService } from 'ng-zorro-antd/notification';
import { NzTableQueryParams } from 'ng-zorro-antd/table';
import { Review } from 'src/app/models/type';
import { ReviewService } from 'src/app/services/review.service';

@Component({
  selector: 'app-review',
  templateUrl: './review.component.html',
  styleUrls: ['./review.component.scss']
})
export class ReviewComponent implements OnInit {

  searchForm!: FormGroup;
  isVisible = false;
  total = 1;
  loading = true;
  pageSize = 5;
  pageIndex = 1;
  listOfData: Review[] = [];
  review = new Review();
  controlArray: Map<string, any> = new Map<string, any>();
  
  constructor(
    private fb: FormBuilder,
    private reviewService: ReviewService,
    private notification: NzNotificationService,
    private modal: NzModalService,
    private msg: NzMessageService
  ) {}

  ngOnInit(): void {
    //form search
    this.searchForm = this.fb.group({
      reviewName: [null],
      reviewStar: [0],
      productName: [null],
      status:[0]
    });
  }

  

  search() {
    const name = this.searchForm.controls.reviewName.value;
    const star = this.searchForm.controls.reviewStar.value;
    const productName = this.searchForm.controls.productName.value;
    const status = this.searchForm.controls.status.value;
    this.controlArray.set('reviewName', name);
    this.controlArray.set('reviewStar', star);
    this.controlArray.set('productName', productName);
    this.controlArray.set('status', status);
    this.getReviews(this.pageIndex, this.pageSize, null, null);
  }

  onQueryParamsChange(params: NzTableQueryParams): void {
    const { pageSize, pageIndex, sort } = params;
    const currentSort = sort.find((item) => item.value !== null);
    const sortField = (currentSort && currentSort.key) || null;
    const sortOrder = (currentSort && currentSort.value) || null;
    this.getReviews(pageIndex, pageSize, sortField, sortOrder);
  }

  resetForm(): void {
    this.searchForm.reset();
  }

  createNotification(type: string, title: string, message: string): void {
    this.notification.create(type, title, message);
  }

  getReviews(
    pageIndex: number,
    pageSize: number,
    sortField: string | null,
    sortOrder: string | null
  ) {
    this.controlArray.set('pageIndex', pageIndex);
    this.controlArray.set('pageSize', pageSize);
    this.controlArray.set('sortField', sortField);
    this.controlArray.set('sortOrder', sortOrder);
    // get product
    this.reviewService.getReviews(this.controlArray).subscribe(
      (data) => {
        if (data && data.results) {
          this.loading = false;
          this.listOfData = data.results;
          this.total = data.rowCount;
        }
      },
      (error) => {
        this.createNotification(
          'error',
          'Có lỗi xảy ra!',
          'Vui lòng liên hệ quản trị viên.'
        );
      }
    );
  }

  changeStatus(e, review) {
    this.review = review;
    this.review.status = e;
    this.reviewService.saveReview(this.review).subscribe(
      (data) => {
        this.createNotification('success', 'Thay đổi thành công!', '');
      },
      (error) => {
        this.createNotification(
          'error',
          'Có lỗi xảy ra!',
          'Vui lòng liên hệ quản trị viên.'
        );
      },
      () => {
        this.getReviews(this.pageIndex, this.pageSize, null, null);
      }
    );
  }

}
