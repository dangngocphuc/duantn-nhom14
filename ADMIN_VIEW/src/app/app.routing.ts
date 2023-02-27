import { NgModule } from '@angular/core';
import { CommonModule, } from '@angular/common';
import { BrowserModule } from '@angular/platform-browser';
import { Routes, RouterModule } from '@angular/router';

import { AdminLayoutComponent } from './layouts/admin-layout/admin-layout.component';
import { AuthLayoutComponent } from './layouts/auth-layout/auth-layout.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { UserProfileComponent } from './pages/user-profile/user-profile.component';
import { TablesComponent } from './pages/tables/tables.component';
import { IconsComponent } from './pages/icons/icons.component';
import { MapsComponent } from './pages/maps/maps.component';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { AuthGuard } from './guard/auth.guard';
// import { UserDetailResolveService } from './services/authentication/user-detail-resolve.service';
// import { UserSessionResolveService } from './services/authentication/user-session-resolve.service';
import { ProductDetailsComponent } from './pages/product-details/product-details.component';
import { OptionValuesComponent } from './pages/option-values/option-values.component';
import { VoucherComponent } from './pages/voucher/voucher.component';
import { Category, Review } from './models/type';
import { CategoryComponent } from './pages/category/category.component';
import { UserDetailResolveService } from './services/authentication/user-detail-resolve.service';
import { UserSessionResolveService } from './services/authentication/user-session-resolve.service';
import { ImeiComponent } from './pages/imei/imei.component';
import { BrandComponent } from './pages/brand/brand.component';
import { ReviewComponent } from './pages/review/review.component';
import { CpuComponent } from './pages/cpu/cpu.component';
import { RamComponent } from './pages/ram/ram.component';
import { RomComponent } from './pages/rom/rom.component';
import { GpuComponent } from './pages/gpu/gpu.component';


const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full', },
  {
    path: '', component: AdminLayoutComponent
    , canActivate: [AuthGuard], resolve: { userDetail: UserDetailResolveService, userSession: UserSessionResolveService },
    children: [
      { path: 'dashboard', component: DashboardComponent },
      { path: 'brand', component: BrandComponent },
      { path: 'user', component: UserProfileComponent },
      { path: 'products', component: TablesComponent },
      // { path: 'options', component: IconsComponent },
      { path: 'oders', component: MapsComponent },
      { path: 'product-details', component: ProductDetailsComponent },
      { path: 'option-values', component: OptionValuesComponent },
      { path: 'voucher', component: VoucherComponent },
      { path: 'category', component: CategoryComponent },
      { path: 'imei', component: ImeiComponent },
      { path: 'review', component: ReviewComponent },
      { path: 'cpu', component: CpuComponent },
      { path: 'ram', component: RamComponent },
      { path: 'rom', component: RomComponent },
      { path: 'gpu', component: GpuComponent },
      // {
      //   path: '',
      //   loadChildren: () => import('src/app/layouts/admin-layout/admin-layout.module').then(m => m.AdminLayoutModule)
      // }
    ]
  },
  {
    path: '',
    component: AuthLayoutComponent,
    children: [
      { path: 'login', component: LoginComponent },
      { path: 'register', component: RegisterComponent }
      // {
      //   path: '',
      //   loadChildren: () => import('src/app/layouts/auth-layout/auth-layout.module').then(m => m.AuthLayoutModule)
      // }
    ]
  },
  {
    path: '**',
    redirectTo: 'dashboard'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule { }
