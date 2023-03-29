import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
// import { NgModule } from '@angular/core';
// import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { HTTP_INTERCEPTORS } from '@angular/common/http';

import { AppComponent } from './app.component';
import { AdminLayoutComponent } from './layouts/admin-layout/admin-layout.component';
import { AuthLayoutComponent } from './layouts/auth-layout/auth-layout.component';
// import { CarouselModule } from 'ngx-owl-carousel-o';
import { NgbActiveModal, NgbCarouselModule, NgbDateAdapter, NgbDateNativeAdapter, NgbDatepickerModule, NgbModule } from '@ng-bootstrap/ng-bootstrap';
// import { ProgressbarModule } from 'ngx-bootstrap/progressbar';
import { ButtonsModule } from '@progress/kendo-angular-buttons';
import { DropDownsModule } from '@progress/kendo-angular-dropdowns';
import { GridModule, PDFModule, ExcelModule } from '@progress/kendo-angular-grid';
import { IconModule, IconsModule } from '@progress/kendo-angular-icons';
import { InputsModule } from '@progress/kendo-angular-inputs';
import { NotificationModule } from '@progress/kendo-angular-notification';

import { AppRoutingModule } from './app.routing';
import { ComponentsModule } from './components/components.module';
import { MapsComponent } from './pages/maps/maps.component';
import { IconsComponent } from './pages/icons/icons.component';
import { TablesComponent } from './pages/tables/tables.component';
import { UserProfileComponent } from './pages/user-profile/user-profile.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { ClipboardModule } from 'ngx-clipboard';
import { CommonModule } from '@angular/common';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { CookieService } from 'ngx-cookie-service';
import { SpinnerComponent } from './components/spinner/spinner.component';
import { LoaderInterceptor } from './interceptors/loader-interceptor.service';
import { LoaderService } from './services/loader.service';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';
import { SharedModule } from './shared/shared.module';
import { ModalModule } from 'ngb-modal';
import { ProductDetailsComponent } from './pages/product-details/product-details.component';
import { NgSelectModule } from '@ng-select/ng-select';
import { OptionValuesComponent } from './pages/option-values/option-values.component';
import { NgxPaginationModule } from 'ngx-pagination';
import { CategoryComponent } from './pages/category/category.component';
import { VoucherComponent } from './pages/voucher/voucher.component';
// import { CKEditorModule } from '@ckeditor/ckeditor5-ui';
import { CKEditorModule } from '@ckeditor/ckeditor5-angular';
import { ImeiComponent } from './pages/imei/imei.component';
import { BrandComponent } from './pages/brand/brand.component';
import { CarouselModule } from 'ngx-owl-carousel-o';
import { NzTableModule } from 'ng-zorro-antd/table';
import { en_US, NZ_I18N } from 'ng-zorro-antd/i18n';
import { NzTabsModule } from 'ng-zorro-antd/tabs';
// import { ChartsModule } from 'ng2-charts';
import {CdkStepperModule} from '@angular/cdk/stepper';
import {NgStepperModule} from 'angular-ng-stepper';
import { ReviewComponent } from './pages/review/review.component';
import { NgImageSliderModule } from 'ng-image-slider';
import { CpuComponent } from './pages/cpu/cpu.component';
import { RamComponent } from './pages/ram/ram.component';
import { RomComponent } from './pages/rom/rom.component';
import { GpuComponent } from './pages/gpu/gpu.component';
import { NgxCurrencyModule } from 'ngx-currency';
import { NgDatepickerComponent } from './components/ng-datepicker/ng-datepicker.component';
import { DatePipe } from '@angular/common';
import { SalesComponent } from './pages/sales/sales.component';
@NgModule({
  imports: [
    BrowserAnimationsModule,
    HttpClientModule,
    ComponentsModule,
    NgbModule,
    RouterModule,
    AppRoutingModule,
    CommonModule,
    ClipboardModule,
    FormsModule,
    ReactiveFormsModule,
    GridModule,
    PDFModule,
    ExcelModule,
    SharedModule,
    ModalModule,
    NgSelectModule,
    NgxPaginationModule,
    CKEditorModule,
    NgbCarouselModule,
    CarouselModule ,
    SharedModule,
    NzTableModule,
    CdkStepperModule,
    // ChartsModule
    NgStepperModule,
    NgImageSliderModule,
    NgxCurrencyModule,
    NgbDatepickerModule,
    NzTabsModule,
    IconModule
    // CarouselModule
    // CKEditorModule
    // ProgressbarModule.forRoot()
  ],
  declarations: [
    AppComponent,
    AdminLayoutComponent,
    AuthLayoutComponent,
    DashboardComponent,
    UserProfileComponent,
    TablesComponent,
    IconsComponent,
    MapsComponent,
    LoginComponent,
    RegisterComponent,
    SpinnerComponent,
    ProductDetailsComponent,
    OptionValuesComponent,
    CategoryComponent,
    VoucherComponent,
    ImeiComponent,
    BrandComponent,
    ReviewComponent,
    CpuComponent,
    RamComponent,
    RomComponent,
    GpuComponent,
    NgDatepickerComponent,
    SalesComponent
  ],
  providers: [
    NgbActiveModal,
    CookieService,
    LoaderService,
    DatePipe,
    { provide: NgbDateAdapter, useClass: NgbDateNativeAdapter },
    { provide: NZ_I18N, useValue: en_US },
    { provide: HTTP_INTERCEPTORS, useClass: LoaderInterceptor, multi: true },
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
