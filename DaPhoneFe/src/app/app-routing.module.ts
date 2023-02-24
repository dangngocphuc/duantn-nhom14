import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthLayoutComponent } from './layouts/auth-layout/auth-layout.component';
import { UserLayoutComponent } from './layouts/user-layout/user-layout.component';
import { CartComponent } from './pages/cart/cart.component';
import { ContactComponent } from './pages/contact/contact.component';
import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './pages/login/login.component';
import { NotFoundComponent } from './pages/not-found/not-found.component';
import { OrderComponent } from './pages/order/order.component';
import { PaySuccessComponent } from './pages/pay-success/pay-success.component';
import { PaymentComponent } from './pages/payment/payment.component';
import { ProductDetailComponent } from './pages/product-detail/product-detail.component';
import { ProductComponent } from './pages/product/product.component';
import { SignupComponent } from './pages/signup/signup.component';
import { AuthGuard } from './guard/auth.guard';

const routes: Routes = [
  {
    path: '',
    redirectTo: 'home',
    pathMatch: 'full',
  },
  {
    path: '',
    component: UserLayoutComponent,
    children: [
      // {
      //   path: 'login',
      //   component: LoginComponent,
      // },
      {
        path: 'login/:isPay',
        component: LoginComponent,
      },
      
      {
        path: '',
        component: HomeComponent,
        pathMatch: 'full',
      },
      {
        path: 'home',
        component: HomeComponent,
        pathMatch: 'full',
      },
      {
        path: 'contact',
        component: ContactComponent,
      },
      {
        path: 'products/:category',
        component: ProductComponent,
      },
      {
        path: 'products',
        component: ProductComponent,
      },
      {
        path: 'product-detail/:id',
        component: ProductDetailComponent,
        // pathMatch: 'full',
      },
      {
        path: 'mycart',
        component: CartComponent,
      },
      {
        path: 'payment',
        component: PaymentComponent,
      },
      {
        path: 'pay-success',
        component: PaySuccessComponent,
      },
      {
        path: 'myorder',
        component: OrderComponent,
        canActivate: [AuthGuard],
      },
      
    ]
  },{
    path: '',
    component: AuthLayoutComponent,
    children: [
      { path: 'login', component: LoginComponent },
      { path: 'signup',component: SignupComponent},
    ]
  },

  { path: '**', component: NotFoundComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
