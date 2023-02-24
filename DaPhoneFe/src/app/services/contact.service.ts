import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Contact } from '../entity/Conntact';
import { HttpBaseService } from './http-base.service';

@Injectable({
  providedIn: 'root'
})
export class ContactService {
  Url=environment.urlServer
  constructor(private http: HttpBaseService) {}
  saveContact(contact:Contact): Observable<any> {
    return this.http.post<any>(`/contact/save`, contact);
  }
}
