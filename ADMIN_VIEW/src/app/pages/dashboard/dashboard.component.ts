import { Component, OnInit } from '@angular/core';
import { Chart } from 'chart.js';
import { NzModalService } from 'ng-zorro-antd/modal';
import { NzNotificationService } from 'ng-zorro-antd/notification';
import { BillService } from 'src/app/services/bill.service';
import { ChartType, ChartOptions, ChartDataSets } from 'chart.js';
import { Workbook } from 'exceljs';
import * as fs from 'file-saver';
// core components
import {
  chartOptions,
  parseOptions,
  chartExample1,
  chartExample2
} from "../../variables/charts";
import { ThongKeUser } from 'src/app/models/type';
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
  lstThongKeUser: ThongKeUser[];
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
    // debugger;
    this.datasets = [
      [0, 20, 10, 30, 15, 40, 20, 60, 60],
      [0, 20, 5, 25, 10, 30, 15, 40, 40]
    ];
    this.data = this.datasets[0];
    console.log(this.data);
    this.getStaticBillByMonth();
    this.getStaticBillByWeek();
    this.getStaticBillByUser();
    // console.log(this.datasets);
    var chartOrders = document.getElementById('chart-orders');
    parseOptions(Chart, chartOptions());
    this.ordersChart = new Chart(chartOrders, {
      type: 'bar',
      options: chartExample2.options,
      data: chartExample2.data
    });

    this

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

  async getStaticBillByUser() {
    await this.billService.getStatisticBillByUser().subscribe(
      (response) => {
        this.lstThongKeUser = response;
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

  dataExcel = [];
  exportExcel() {
    this.dataExcel = [];
    let workbook = new Workbook();
    let worksheet = workbook.addWorksheet('Danh sách');
    //Thiết lập tiêu đề
    worksheet.mergeCells('A1', 'C1');
    let titleRow = worksheet.getCell('A1');
    titleRow.value = 'Danh sách'
    titleRow.font = {
      name: 'Times New Roman',
      size: 16,
      // underline: 'single',
      bold: true,
      color: { argb: '000000' }
    }
    titleRow.border = { top: { style: 'thin' }, left: { style: 'thin' }, bottom: { style: 'thin' }, right: { style: 'thin' } }
    titleRow.alignment = { vertical: 'middle', horizontal: 'center' }

    //Thiết lập header
    const header = ["STT", "Khách hàng", "Tổng tiền"]
    let headerRow = worksheet.addRow(header);
    worksheet.getCell('A2').alignment = { vertical: 'middle', horizontal: 'center' };
    worksheet.getCell('B2').alignment = { vertical: 'middle', horizontal: 'center' };
    worksheet.getCell('C2').alignment = { vertical: 'middle', horizontal: 'center' };

    headerRow.font = {
      name: 'Times New Roman',
      size: 14,
      color: { argb: '000000' },
      bold: true
    }
    // Cell Style : Fill and Border
    headerRow.eachCell((cell, number) => {
      cell.fill = {
        type: 'pattern',
        pattern: 'solid',
        fgColor: { argb: 'FFFFFF' },
      },
        cell.style
      cell.border = { top: { style: 'thin' }, left: { style: 'thin' }, bottom: { style: 'thin' }, right: { style: 'thin' } }
    });

    var stt = 1;
    if (this.lstThongKeUser) {
      this.lstThongKeUser.forEach(element => {
        this.dataExcel.push([stt, element.name, element.count])
        stt++;
      })
    }
    if (this.dataExcel.length > 0) {
      // Add Data and Conditional Formatting
      this.dataExcel.forEach(d => {
        let row = worksheet.addRow(d);
        row.font = {
          name: 'Times New Roman',
          size: 12,
          // underline: 'single',
          color: { argb: '000000' }
        }
        row.eachCell((cell, number) => {
          cell.fill = {
            type: 'pattern',
            pattern: 'solid',
            fgColor: { argb: 'FFFFFF' },
            // bgColor: { argb: 'FF0000FF' },
          },
            cell.style
          cell.border = { top: { style: 'thin' }, left: { style: 'thin' }, bottom: { style: 'thin' }, right: { style: 'thin' } }
        });
      });

      //Set columns's width (px)
      worksheet.columns = [{ key: 'A', width: 10 }, { key: 'B', width: 30 }, { key: 'C', width: 30 }];
      // worksheet.addRows(this.dataExcel);
      workbook.xlsx.writeBuffer().then((data) => {
        let blob = new Blob([data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
        fs.saveAs(blob, 'BaoCao.xlsx');
      })

      //excel
    }

  }
}
