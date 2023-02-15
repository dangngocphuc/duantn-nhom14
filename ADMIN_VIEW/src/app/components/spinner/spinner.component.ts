import { Component, OnDestroy, OnInit } from '@angular/core';
import { LoaderService } from "../../services/loader.service";

@Component({
  selector: 'app-spinner',
  templateUrl: './spinner.component.html',
  styleUrls: ['./spinner.component.scss']
})
export class SpinnerComponent implements OnInit, OnDestroy {
  readonly interval = 15;
  readonly spriteWidth = 11200;
  readonly frameWidth = 80;
  positionX = 0;
  intervalRef: ReturnType<typeof setInterval>;
  loading: boolean;
  constructor(
    private loaderService: LoaderService
  ) {
    this.intervalRef = setInterval(() => {
      if (this.positionX < this.spriteWidth) {
        this.positionX += this.frameWidth;
        return;
      }
      this.positionX = 0;
    }, this.interval);

    this.loaderService.isLoading.subscribe((v) => {
      // console.log(v);
      this.loading = v;
    });
  }

  ngOnInit(): void {

  }

  ngOnDestroy(): void {
    clearInterval(this.intervalRef);
  }
}
