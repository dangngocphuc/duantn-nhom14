import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NZ_I18N } from 'ng-zorro-antd/i18n';
import { en_US } from 'ng-zorro-antd/i18n';
import { registerLocaleData } from '@angular/common';
import en from '@angular/common/locales/en';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { SharedModule } from './shared/shared.module';
import { HomeComponent } from './pages/home/home.component';
import { ProductComponent } from './pages/product/product.component';
import { LoginComponent } from './pages/login/login.component';
import { CartComponent } from './pages/cart/cart.component';
import { HeaderComponent } from './pages/header/header.component';
import { FooterComponent } from './pages/footer/footer.component';
import { ContactComponent } from './pages/contact/contact.component';
import { ProductDetailComponent } from './pages/product-detail/product-detail.component';
import { SignupComponent } from './pages/signup/signup.component';
import { PaymentComponent } from './pages/payment/payment.component';
import { PaySuccessComponent } from './pages/pay-success/pay-success.component';
import { OrderComponent } from './pages/order/order.component';
import { NotFoundComponent } from './pages/not-found/not-found.component';
import { LoaderService } from './services/loader/loader.service';
import { LoaderComponent } from './pages/loader/loader.component';
import { LoaderInterceptor } from './interceptor/loader.interceptor';
import { CaptchaModule } from './captcha/captcha.module';
import { PopupModule } from '@progress/kendo-angular-popup';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgxPayPalModule } from 'ngx-paypal';
import { NgSelectModule } from '@ng-select/ng-select';
import { ModalModule } from 'ngb-modal';

import { SocialLoginModule, SocialAuthServiceConfig } from 'angularx-social-login';
import { GoogleLoginProvider } from 'angularx-social-login';
import { AuthLayoutComponent } from './layouts/auth-layout/auth-layout.component';
import { UserLayoutComponent } from './layouts/user-layout/user-layout.component';
import { NgImageSliderModule } from 'ng-image-slider';
import { NgxTrimDirectiveModule } from 'ngx-trim-directive';



registerLocaleData(en);

const googleLoginOptions = {
  scope: 'profile email',
  plugin_name:'sample_login' //can be any name
}; 

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoaderComponent,
    ProductComponent,
    LoginComponent,
    CartComponent,
    HeaderComponent,
    FooterComponent,
    ContactComponent,
    ProductDetailComponent,
    SignupComponent,
    PaymentComponent,
    PaySuccessComponent,
    OrderComponent,
    NotFoundComponent,
    AuthLayoutComponent,
    UserLayoutComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    SharedModule,
    CaptchaModule,
    PopupModule,
    NgbModule,
    ModalModule,
    NgxPayPalModule,
    NgSelectModule,
    SocialLoginModule,
    NgSelectModule,
    NgImageSliderModule,
    NgxTrimDirectiveModule
  ],
  providers: [LoaderService,{ provide: NZ_I18N, useValue: en_US },{ provide: HTTP_INTERCEPTORS, useClass: LoaderInterceptor, multi: true },{
    provide: 'SocialAuthServiceConfig',
    useValue: {
      autoLogin: false,
      providers: [
        {
          id: GoogleLoginProvider.PROVIDER_ID,
          provider: new GoogleLoginProvider(
            '480175323742-hbeh9d4aadsdvosl4sjjode9ganc2lao.apps.googleusercontent.com',
            googleLoginOptions
          )
        }
      ],
      onError: (err) => {
        console.error(err);
      }
    } as SocialAuthServiceConfig,
  }
    
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
