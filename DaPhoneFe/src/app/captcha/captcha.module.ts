import { NgModule, ModuleWithProviders  } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import {CaptchaService} from './captcha.service';
import { CaptchaComponent } from './captcha/captcha.component';
@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule
  ],
  declarations: [CaptchaComponent],
  exports:[CaptchaComponent],
})
export class CaptchaModule {
  static forRoot(): ModuleWithProviders<CaptchaModule> {
    return {
      ngModule: CaptchaModule,
      providers: [ CaptchaService ]
    };
  }
}