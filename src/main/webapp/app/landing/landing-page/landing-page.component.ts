import { HttpResponse } from '@angular/common/http';
import { ISystemSetting } from './../../entities/system-setting/system-setting.model';
import { SystemSettingService } from './../../entities/system-setting/service/system-setting.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-landing-page',
  templateUrl: './landing-page.component.html',
  styleUrls: ['./landing-page.component.css'],
})
export class LandingPageComponent implements OnInit {
  constructor(protected systemSettingService: SystemSettingService) {}
  systemSettings?: ISystemSetting[];
  isLoading = false;
  price = 0;
  tax = 0.0;
  loadAll(): void {
    this.isLoading = true;
    this.systemSettingService.query().subscribe(
      (res: HttpResponse<ISystemSetting[]>) => {
        this.isLoading = false;
        this.systemSettings = res.body ?? [];
        this.price = this.systemSettings?.find(kv => kv?.key === 'price')?.value || 0;
        this.tax = this.systemSettings?.find(kv => kv?.key === 'tax')?.value || 0;
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }
}
