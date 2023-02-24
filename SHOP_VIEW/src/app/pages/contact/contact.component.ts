import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NzNotificationService } from 'ng-zorro-antd/notification';
import { Contact } from 'src/app/entity/Conntact';
import { ContactService } from 'src/app/services/contact.service';

@Component({
  selector: 'app-contact',
  templateUrl: './contact.component.html',
  styleUrls: ['./contact.component.css'],
})
export class ContactComponent implements OnInit {
  validateForm!: FormGroup;
  contact = new Contact();
  constructor(
    private notification: NzNotificationService,
    private fb: FormBuilder,
    private contactService: ContactService
  ) {}
  createNotification(type: string, title: string, message: string): void {
    this.notification.create(type, title, message);
  }
  ngOnInit(): void {
    this.validateForm = this.fb.group({
      userName: [null, [Validators.required]],
      email: [null, [Validators.required]],
      subject: [null, [Validators.required]],
      content: [null, [Validators.required]],
    });
  }

  resetForm() {
    this.validateForm.reset();
  }
  submitForm(): void {
    for (const i in this.validateForm.controls) {
      if (this.validateForm.controls.hasOwnProperty(i)) {
        this.validateForm.controls[i].markAsDirty();
        this.validateForm.controls[i].updateValueAndValidity();
      }
    }
    this.contact.contactName = this.validateForm.controls.userName.value;
    this.contact.contactEmail = this.validateForm.controls.email.value;
    this.contact.contactTitle = this.validateForm.controls.subject.value;
    this.contact.contactMessage = this.validateForm.controls.content.value;
    if (this.contact) {
      this.contactService.saveContact(this.contact).subscribe(
        (data) => {
          if (data) {
            this.createNotification(
              'success',
              'Đã liên hệ thành công!',
              'Thông tin của bạn đã được gửi cho quản trị viên.'
            );
            this.resetForm();
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
  }
}
