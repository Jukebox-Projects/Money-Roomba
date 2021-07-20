import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { iconList } from './icon.list';
import { IICon } from './icon.model';

@Component({
  templateUrl: './icon-picker.component.html',
})
export class IconPickerComponent implements OnInit {
  icon?: IICon;
  iconList?: IICon[] = iconList;

  constructor(protected activeModal: NgbActiveModal) {}

  ngOnInit(): void {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  selectIcon(icon: IICon): void {
    this.activeModal.close(icon.id);
  }
}
