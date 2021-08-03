import { LicenseBuyOrActivateDialogComponent } from './../buy-or-activate/license-buy-or-activate-dialog.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
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
  constructor(protected systemSettingService: SystemSettingService, protected modalService: NgbModal) {}
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

  open(): void {
    const modalRef = this.modalService.open(LicenseBuyOrActivateDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.closed.subscribe(reason => {
      if (reason === 'bought') {
        setTimeout(() => {
          window.location.reload();
        }, 3500);
      }
    });
  }
}
