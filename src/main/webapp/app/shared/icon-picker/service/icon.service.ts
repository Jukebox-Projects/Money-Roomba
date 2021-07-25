import { IICon } from '../icon.model';
import { Injectable } from '@angular/core';

@Injectable()
export class IconService {
  allIcons = [];
  path: string = 'assets/icons/icon-picker';
  DEFAULT: IICon = { id: 0, name: 'default', path: `${this.path}/default.png` };

  iconList: IICon[] = [
    { id: 1, name: 'gaming', path: 'control.png' },
    { id: 2, name: 'plus18', path: 'plus18.png' },
    { id: 3, name: 'christmas', path: 'xmas.png' },
    { id: 4, name: 'idea', path: 'idea.png' },
    { id: 5, name: 'colorpalette', path: 'colorpalette.png' },
  ];

  getIcons(): IICon[] {
    return (this.allIcons = this.iconList.map(icon => ({ ...icon, path: this.formatPath(icon) })));
  }

  getIcon(id: number): IICon {
    let icon;
    if (id) {
      icon = Object.assign(
        {},
        this.iconList.find(Icons => Icons.id == id)
      );
      icon.path = this.formatPath(icon);
    } else {
      icon = this.DEFAULT;
    }
    return icon;
  }

  formatPath(icon: IICon) {
    return `${this.path}/${icon.path}`;
  }
}
