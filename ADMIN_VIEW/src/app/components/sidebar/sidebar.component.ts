import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

declare interface RouteInfo {
  path: string;
  title: string;
  icon: string;
  class: string;
}
export const ROUTES: RouteInfo[] = [
  { path: '/dashboard', title: 'Dashboard', icon: 'ni-tv-2 text-primary', class: '' },
  { path: '/brand', title: 'Quản lý thương hiệu', icon: 'ni ni-air-baloon text-gray', class: '' },
  // { path: '/options', title: 'Quản lý Option',  icon:'ni-planet text-blue', class: '' },
  { path: '/products', title: 'Quản lý sản phẩm', icon: 'ni-laptop text-red', class: '' },
  { path: '/product-details', title: 'Quản lý sản phẩm chi tiết', icon: 'ni-key-25 text-info', class: '' },
  { path: '/imei', title: 'Quản lý imei', icon: 'ni ni-bullet-list-67', class: '' },
  { path: '/oders', title: 'Quản lý đơn hàng', icon: 'ni-cart text-orange', class: '' },
  { path: '/user', title: 'Quản lý người dùng', icon: 'ni-single-02 text-yellow', class: '' },
  { path: '/review', title: 'Quản lý đánh giá', icon: 'ni-diamond text-purple ', class: '' },
  { path: '/cpu', title: 'Quản lý CPU', icon: 'ni-planet text-blue', class: '' },
  { path: '/ram', title: 'Quản lý RAM', icon: 'ni-tag text-green', class: '' },
  { path: '/rom', title: 'Quản lý ROM', icon: 'ni ni-spaceship text-pink ', class: '' },
  { path: '/gpu', title: 'Quản lý GPU', icon: 'ni ni-controller text-brown', class: '' },
  // { path: '/option-values', title: 'Quản lý Option-values',  icon:'ni-circle-08 text-pink', class: '' },
  // { path: '/category', title: 'Quản lý category',  icon:'ni-circle-08 text-pink', class: '' },
  // { path: '/voucher', title: 'Quản lý voucher',  icon:'ni-circle-08 text-pink', class: '' }
];

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {

  public menuItems: any[];
  public isCollapsed = true;

  constructor(private router: Router) { }

  ngOnInit() {
    this.menuItems = ROUTES.filter(menuItem => menuItem);
    this.router.events.subscribe((event) => {
      this.isCollapsed = true;
    });
  }
}
