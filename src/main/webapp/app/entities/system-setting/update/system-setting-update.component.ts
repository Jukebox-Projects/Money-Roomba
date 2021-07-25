import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ISystemSetting, SystemSetting } from '../system-setting.model';
import { SystemSettingService } from '../service/system-setting.service';

@Component({
  selector: 'jhi-system-setting-update',
  templateUrl: './system-setting-update.component.html',
})
export class SystemSettingUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    key: [
      {
        value: '',
        disabled: true,
      },
    ],
    value: [null, [Validators.required]],
  });

  constructor(protected systemSettingService: SystemSettingService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ systemSetting }) => {
      this.updateForm(systemSetting);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const systemSetting = this.createFromForm();
    if (systemSetting.id !== undefined) {
      this.subscribeToSaveResponse(this.systemSettingService.update(systemSetting));
    } else {
      this.subscribeToSaveResponse(this.systemSettingService.create(systemSetting));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISystemSetting>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(systemSetting: ISystemSetting): void {
    this.editForm.patchValue({
      id: systemSetting.id,
      key: systemSetting.key,
      value: systemSetting.value,
    });
  }

  protected createFromForm(): ISystemSetting {
    return {
      ...new SystemSetting(),
      id: this.editForm.get(['id'])!.value,
      key: this.editForm.get(['key'])!.value,
      value: this.editForm.get(['value'])!.value,
    };
  }
}
