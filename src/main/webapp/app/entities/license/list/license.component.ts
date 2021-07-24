import { LicenseCreateDialogComponent } from './../create/license-create-dialog.component';
import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILicense } from '../license.model';
import { LicenseService } from '../service/license.service';
import { LicenseDeleteDialogComponent } from '../delete/license-delete-dialog.component';
import { LicenseCreateMethod } from 'app/entities/enumerations/license-create-method.model';

@Component({
  selector: 'jhi-license',
  templateUrl: './license.component.html',
})
export class LicenseComponent implements OnInit {
  licenses?: ILicense[];
  allLicenses?: ILicense[];
  inputText = '';
  isLoading = false;

  constructor(protected licenseService: LicenseService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.licenseService.query().subscribe(
      (res: HttpResponse<ILicense[]>) => {
        this.isLoading = false;
        this.licenses = res.body ?? [];
        this.allLicenses = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ILicense): number {
    return item.id!;
  }

  filter(): void {
    if (this.allLicenses !== undefined) {
      this.licenses = this.allLicenses;
      this.licenses = this.licenses.filter(license => {
        if (
          license.createMethod !== undefined &&
          license.isAssigned !== undefined &&
          license.isActive !== undefined &&
          license.code !== undefined
        ) {
          if ('en bloque'.includes(this.inputText.toLowerCase())) {
            return license.createMethod.toString() == 'BULK';
          }
          if ('manual'.includes(this.inputText.toLowerCase())) {
            return license.createMethod.toString() == 'MANUAL';
          }
          if ('asignada'.includes(this.inputText.toLowerCase())) {
            return license.isAssigned;
          }
          if ('libre'.includes(this.inputText.toLowerCase())) {
            return !license.isAssigned;
          }
          if ('activada'.includes(this.inputText.toLowerCase())) {
            return license.isActive;
          }
          if ('desactivada'.includes(this.inputText.toLowerCase())) {
            return !license.isActive;
          }
          return license.code.toLowerCase().includes(this.inputText.toLowerCase());
        } else {
          return false;
        }
      });
      if (this.inputText === '') {
        this.licenses = this.allLicenses;
      }
    } else {
      this.loadAll();
    }
  }

  delete(license: ILicense): void {
    const modalRef = this.modalService.open(LicenseDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.license = license;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }

  create(): void {
    const modalRef = this.modalService.open(LicenseCreateDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.closed.subscribe(reason => {
      if (reason === 'created') {
        this.loadAll();
      }
    });
  }

  setActive(license: ILicense, isActivated: boolean): void {
    this.licenseService.update({ ...license, isActive: isActivated }).subscribe(() => this.loadAll());
  }

  //Disabled cambia completamente el estilo del checkbox. Por eso esta funcion está encargada de deshabilitar el click a un checkbox.
  checkbox(event) {
    event.preventDefault();
  }
}
