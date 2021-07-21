import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISystemSetting } from '../system-setting.model';
import { SystemSettingService } from '../service/system-setting.service';
import { SystemSettingDeleteDialogComponent } from '../delete/system-setting-delete-dialog.component';

@Component({
  selector: 'jhi-system-setting',
  templateUrl: './system-setting.component.html',
})
export class SystemSettingComponent implements OnInit {
  systemSettings?: ISystemSetting[];
  isLoading = false;

  constructor(protected systemSettingService: SystemSettingService, protected modalService: NgbModal) {}

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

  trackId(index: number, item: ISystemSetting): number {
    return item.id!;
  }

  /*delete(systemSetting: ISystemSetting): void {
    const modalRef = this.modalService.open(SystemSettingDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.systemSetting = systemSetting;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }*/
}
