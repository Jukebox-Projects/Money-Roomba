import { IICon } from '../icon.model';
import { Injectable } from '@angular/core';

@Injectable()
export class IconService {
  allIcons = [];
  path: string = 'assets/icons/icon-picker/';

  getIcons(): IICon[] {
    return (this.allIcons = this.iconList.map(icon => ({ ...icon, path: `${this.path}/${icon.path}` })));
  }

  getIcon(id: number) {
    return this.iconList.find(Icons => Icons.id == id);
  }

  getPath(): string {
    return this.path;
  }

  iconList: IICon[] = [
    { id: 1, name: 'control', path: 'control.png' },
    { id: 2, name: 'plus18', path: 'plus18.png' },
    { id: 3, name: 'xmas', path: 'xmas.png' },
    { id: 4, name: 'idea', path: 'idea.png' },
    { id: 5, name: 'colorpalette', path: 'colorpalette.png' },
  ];
}
