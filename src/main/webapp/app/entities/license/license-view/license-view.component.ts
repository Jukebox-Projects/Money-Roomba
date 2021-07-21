import { HttpResponse } from '@angular/common/http';
import { ISystemSetting } from './../../system-setting/system-setting.model';
import { SystemSettingService } from './../../system-setting/service/system-setting.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-license-view',
  templateUrl: './license-view.component.html',
  styleUrls: ['./license-view.component.scss'],
})
export class LicenseViewComponent implements OnInit {
  constructor(protected systemSettingService: SystemSettingService) {}
  systemSettings?: ISystemSetting[];
  isLoading = false;
  loadAll(): void {
    this.isLoading = true;

    this.systemSettingService.query().subscribe(
      (res: HttpResponse<ISystemSetting[]>) => {
        this.isLoading = false;
        this.systemSettings = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  getPrice(): number {
    return this.systemSettings?.find(kv => kv?.key === 'price')?.value || 0;
  }
}