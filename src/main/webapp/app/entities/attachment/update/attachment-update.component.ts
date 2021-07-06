import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IAttachment, Attachment } from '../attachment.model';
import { AttachmentService } from '../service/attachment.service';

@Component({
  selector: 'jhi-attachment-update',
  templateUrl: './attachment-update.component.html',
})
export class AttachmentUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    url: [null, [Validators.required]],
    name: [null, [Validators.required]],
  });

  constructor(protected attachmentService: AttachmentService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ attachment }) => {
      this.updateForm(attachment);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const attachment = this.createFromForm();
    if (attachment.id !== undefined) {
      this.subscribeToSaveResponse(this.attachmentService.update(attachment));
    } else {
      this.subscribeToSaveResponse(this.attachmentService.create(attachment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAttachment>>): void {
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

  protected updateForm(attachment: IAttachment): void {
    this.editForm.patchValue({
      id: attachment.id,
      url: attachment.url,
      name: attachment.name,
    });
  }

  protected createFromForm(): IAttachment {
    return {
      ...new Attachment(),
      id: this.editForm.get(['id'])!.value,
      url: this.editForm.get(['url'])!.value,
      name: this.editForm.get(['name'])!.value,
    };
  }
}
