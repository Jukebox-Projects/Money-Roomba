import { IICon } from '../icon.model';
import { Injectable } from '@angular/core';

@Injectable()
export class IconService {
  allIcons = [];
  path: string = 'assets/icons/icon-picker';
  DEFAULT: IICon = { id: 0, name: 'default', path: `${this.path}/default.png` };

  iconList: IICon[] = [
    { id: 1, name: ' ambulance', path: 'ambulance.png' },
    { id: 2, name: ' apple', path: 'apple.png' },
    { id: 3, name: ' avocado', path: 'avocado.png' },
    { id: 4, name: ' band aid', path: 'band aid.png' },
    { id: 5, name: ' barber', path: 'barber.png' },
    { id: 6, name: ' basic-car', path: 'basic-car.png' },
    { id: 7, name: ' basketball', path: 'basketball.png' },
    { id: 8, name: ' beer', path: 'beer.png' },
    { id: 9, name: ' bill', path: 'bill.png' },
    { id: 10, name: ' blood drop', path: 'blood drop.png' },
    { id: 11, name: ' bonsai', path: 'bonsai.png' },
    { id: 12, name: ' broken bone', path: 'broken bone.png' },
    { id: 13, name: ' burger', path: 'burger.png' },
    { id: 14, name: ' calendar', path: 'calendar.png' },
    { id: 15, name: ' camera', path: 'camera.png' },
    { id: 16, name: ' capsules', path: 'capsules.png' },
    { id: 17, name: ' cards', path: 'cards.png' },
    { id: 18, name: ' certificate', path: 'certificate.png' },
    { id: 19, name: ' chef', path: 'chef.png' },
    { id: 20, name: ' christmas-hat', path: 'christmas-hat.png' },
    { id: 21, name: ' clown', path: 'clown.png' },
    { id: 22, name: ' coin', path: 'coin.png' },
    { id: 23, name: ' coins', path: 'coins.png' },
    { id: 24, name: ' colorpalette', path: 'colorpalette.png' },
    { id: 25, name: ' compass', path: 'compass.png' },
    { id: 26, name: ' computer', path: 'computer.png' },
    { id: 27, name: ' control', path: 'control.png' },
    { id: 28, name: ' credit-card', path: 'credit-card.png' },
    { id: 29, name: ' danger', path: 'danger.png' },
    { id: 30, name: ' delivery', path: 'delivery.png' },
    { id: 31, name: ' doctor', path: 'doctor.png' },
    { id: 32, name: ' dog1', path: 'dog1.png' },
    { id: 33, name: ' dog2', path: 'dog2.png' },
    { id: 34, name: ' dog3', path: 'dog3.png' },
    { id: 35, name: ' dog4', path: 'dog4.png' },
    { id: 36, name: ' dog5', path: 'dog5.png' },
    { id: 37, name: ' dumbbell', path: 'dumbbell.png' },
    { id: 38, name: ' emergency-phone', path: 'emergency-phone.png' },
    { id: 39, name: ' facial', path: 'facial.png' },
    { id: 40, name: ' fish', path: 'fish.png' },
    { id: 41, name: ' food-tray', path: 'food-tray.png' },
    { id: 42, name: ' french-fries', path: 'french-fries.png' },
    { id: 43, name: ' fried-eggs', path: 'fried-eggs.png' },
    { id: 44, name: ' frog', path: 'frog.png' },
    { id: 45, name: ' ghost', path: 'ghost.png' },
    { id: 46, name: ' gift', path: 'gift.png' },
    { id: 47, name: ' globe', path: 'globe.png' },
    { id: 48, name: ' graduation', path: 'graduation.png' },
    { id: 49, name: ' guitar', path: 'guitar.png' },
    { id: 50, name: ' haircur', path: 'haircur.png' },
    { id: 51, name: ' hairdryer', path: 'hairdryer.png' },
    { id: 52, name: ' hamster', path: 'hamster.png' },
    { id: 53, name: ' hospital', path: 'hospital.png' },
    { id: 54, name: ' house', path: 'house.png' },
    { id: 55, name: ' idea', path: 'idea.png' },
    { id: 56, name: ' income', path: 'income.png' },
    { id: 57, name: ' knight', path: 'knight.png' },
    { id: 58, name: ' leaf', path: 'leaf.png' },
    { id: 59, name: ' lips', path: 'lips.png' },
    { id: 60, name: ' meat', path: 'meat.png' },
    { id: 61, name: ' medical report', path: 'medical report.png' },
    { id: 62, name: ' medical-mask', path: 'medical-mask.png' },
    { id: 63, name: ' money', path: 'money.png' },
    { id: 64, name: ' mustache', path: 'mustache.png' },
    { id: 65, name: ' nailfile', path: 'nailfile.png' },
    { id: 66, name: ' nigiri', path: 'nigiri.png' },
    { id: 67, name: ' noodles', path: 'noodles.png' },
    { id: 68, name: ' outcome', path: 'outcome.png' },
    { id: 69, name: ' pizza', path: 'pizza.png' },
    { id: 70, name: ' plate', path: 'plate.png' },
    { id: 71, name: ' plus18', path: 'plus18.png' },
    { id: 72, name: ' poop', path: 'poop.png' },
    { id: 73, name: ' pumpkin', path: 'pumpkin.png' },
    { id: 74, name: ' race-car', path: 'race-car.png' },
    { id: 75, name: ' rainbow', path: 'rainbow.png' },
    { id: 76, name: ' red cross', path: 'red cross.png' },
    { id: 77, name: ' restaurant', path: 'restaurant.png' },
    { id: 78, name: ' ribbon', path: 'ribbon.png' },
    { id: 79, name: ' ring', path: 'ring.png' },
    { id: 80, name: ' safe', path: 'safe.png' },
    { id: 81, name: ' salad', path: 'salad.png' },
    { id: 82, name: ' sale', path: 'sale.png' },
    { id: 83, name: ' sandwich', path: 'sandwich.png' },
    { id: 84, name: ' scooter', path: 'scooter.png' },
    { id: 85, name: ' shrimp', path: 'shrimp.png' },
    { id: 86, name: ' skull', path: 'skull.png' },
    { id: 87, name: ' soccer', path: 'soccer.png' },
    { id: 88, name: ' soup', path: 'soup.png' },
    { id: 89, name: ' spa', path: 'spa.png' },
    { id: 90, name: ' stethoscope', path: 'stethoscope.png' },
    { id: 91, name: ' surgeon', path: 'surgeon.png' },
    { id: 92, name: ' swiss-knife', path: 'swiss-knife.png' },
    { id: 93, name: ' sword', path: 'sword.png' },
    { id: 94, name: ' taco', path: 'taco.png' },
    { id: 95, name: ' test-tubes', path: 'test-tubes.png' },
    { id: 96, name: ' thumbs-up', path: 'thumbs-up.png' },
    { id: 97, name: ' tie', path: 'tie.png' },
    { id: 98, name: ' tomato', path: 'tomato.png' },
    { id: 99, name: ' tooth', path: 'tooth.png' },
    { id: 100, name: ' trophy', path: 'trophy.png' },
    { id: 101, name: ' virus', path: 'virus.png' },
    { id: 102, name: ' voucher', path: 'voucher.png' },
    { id: 103, name: ' wallet', path: 'wallet.png' },
    { id: 104, name: ' weight-scale', path: 'weight-scale.png' },
    { id: 105, name: ' wheelchair', path: 'wheelchair.png' },
    { id: 106, name: ' wine-glass', path: 'wine-glass.png' },
    { id: 107, name: ' xmas', path: 'xmas.png' },
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
