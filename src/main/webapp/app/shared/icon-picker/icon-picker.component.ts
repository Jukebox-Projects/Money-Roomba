import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IconService } from './service/icon.service';
import { IICon } from './icon.model';

@Component({
  templateUrl: './icon-picker.component.html',
  styleUrls: ['./icon-picker.component.css'],
})
export class IconPickerComponent implements OnInit {
  icon?: IICon;
  iconList?: IICon[];
  iconPath?: string;

  constructor(protected activeModal: NgbActiveModal, protected iconService: IconService) {}

  ngOnInit(): void {
    this.loadIcons();
  }

  loadIcons(): void {
    this.iconList = this.iconService.getIcons();
  }

  cancel(): void {
    this.activeModal.dismiss();
  }

  selectIcon(icon: IICon): void {
    this.activeModal.close(icon.id);
  }
}
