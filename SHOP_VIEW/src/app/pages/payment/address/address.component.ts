import { Component, OnInit } from '@angular/core';
import { Address, District, Province, Ward } from 'src/app/models/type';
import { GhnService } from 'src/app/services/ghn.service';

@Component({
  selector: 'app-address',
  templateUrl: './address.component.html',
  styleUrls: ['./address.component.css']
})
export class AddressComponent implements OnInit {

  listDistrict: District[];
  listWard: Ward[];
  listProvince: Province[];
  addresss = new Address();

  provinceId: number;
  districtId: number;
  wardCode: String;
  
  constructor(private ghnService: GhnService) { }

  ngOnInit(): void {

  }

  getListDistrict() {
    // console.log(this.provinceId);
    this.districtId = null;
    this.wardCode = null;
    // this.serviceId = null;
    // this.responseFeeSevice = null;
    this.ghnService.getListDistrict(this.provinceId).subscribe((data) => {
      // console.log(data);
      if (data.code == 200) {
        this.listDistrict = data.data;
        // console.log(data);
      }
    })
  }

  getListWard() {
    // console.log(this.districtId);
    // this.wardCode = null;
    // this.serviceId = null;
    // this.responseFeeSevice = null;
    if (this.districtId) {
      this.ghnService.getListWard(this.districtId).subscribe((data) => {
        // console.log(data);
        if (data.code == 200) {
          this.listWard = data.data;
          // console.log(data);
        }
      })
      // this.getService();
    } else {
      this.wardCode = null;
    }
  }

}
