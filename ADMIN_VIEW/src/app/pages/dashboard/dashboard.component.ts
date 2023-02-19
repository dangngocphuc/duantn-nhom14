import { Component, OnInit } from '@angular/core';
import { Chart } from 'chart.js';
import { NzModalService } from 'ng-zorro-antd/modal';
import { NzNotificationService } from 'ng-zorro-antd/notification';
import { BillService } from 'src/app/services/bill.service';
import { ChartType, ChartOptions, ChartDataSets } from 'chart.js';
// core components
import {
  chartOptions,
  parseOptions,
  chartExample1,
  chartExample2
} from "../../variables/charts";
@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

  public datasets: any;
  public data: any;
  public salesChart;
  public ordersChart;
  public clicked: boolean = true;
  public clicked1: boolean = false;

  monday: Date;
  sunday: Date;
  month: Date = new Date();
  public lineChartData: ChartDataSets[] = [];
  constructor(
    private notification: NzNotificationService,
    private modal: NzModalService,
    private billService: BillService
  ) {
    this.getDate();
    // this.getStaticBill();
  }

  ngOnInit(): void {
    debugger;
    this.datasets = [
      [0, 20, 10, 30, 15, 40, 20, 60, 60],
      [0, 20, 5, 25, 10, 30, 15, 40, 40]
    ];
    this.data = this.datasets[0];
    console.log(this.data);
    this.getStaticBillByMonth();
    this.getStaticBillByWeek();
    // console.log(this.datasets);
    var chartOrders = document.getElementById('chart-orders');
    parseOptions(Chart, chartOptions());
    this.ordersChart = new Chart(chartOrders, {
      type: 'bar',
      options: chartExample2.options,
      data: chartExample2.data
    });


    // this.salesChart.data.datasets[0].data = this.datasets;
    var chartSales = document.getElementById('chart-sales');
    this.salesChart = new Chart(chartSales, {
      type: 'line',
      options: chartExample1.options,
      data: chartExample1.data,
    });
    // console.log(this.lineChartData);
    // this.salesChart.data = this.lineChartData
    // this.salesChart.update();
  }


  // public updateOptions() {
  //   debugger;
  //   this.salesChart.data.datasets[0].data = this.datasets.data;
  //   this.salesChart.update();
  // }


  controlArray: Map<string, any> = new Map<string, any>();

  getStaticBillThisWeek() {
    this.controlArray.set('fromDate', this.convertDate(this.monday));
    this.controlArray.set('toDate', this.convertDate(this.sunday));
    this.billService.exportBills(this.controlArray).subscribe(
      (data) => {
        // this.downLoadFile(data, 'application/ms-excel');
      },
      (err) => {
        this.createNotification(
          'error',
          'Có lỗi xảy ra!',
          'Vui lòng liên hệ quản trị viên.'
        );
      }
    );
  }
  createNotification(type: string, title: string, message: string): void {
    this.notification.create(type, title, message);
  }

  convertDate(str) {
    if (str) {
      var date = new Date(str),
        mnth = ('0' + (date.getMonth() + 1)).slice(-2),
        day = ('0' + date.getDate()).slice(-2);
      return [date.getFullYear(), mnth, day].join('/');
    }
    return null;
  }

  getDate() {
    var curr = new Date; // get current date
    var first = curr.getDate() - curr.getDay() + 1; // First day is the day of the month - the day of the week
    var last = first + 6; // last day is the first day + 6

    this.monday = new Date(curr.setDate(first));
    this.sunday = new Date(curr.setDate(last));
  }

  async getStaticBillByWeek() {
    await this.billService.getStatisticBillByWeek(null).subscribe(
      (response) => {
        console.log(response.result)
        this.salesChart.data.datasets[0].data = response.result;
        this.salesChart.update();
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

  async getStaticBillByMonth() {
    await this.billService.getStatisticBillByMonth(null).subscribe(
      (response) => {
        console.log(response.result)
        this.ordersChart.data.datasets[0].data = response.result;
        this.ordersChart.update();
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
}
