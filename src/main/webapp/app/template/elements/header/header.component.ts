import { Authority } from './../../../config/authority.constants';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { SessionStorageService } from 'ngx-webstorage';

import { VERSION } from 'app/app.constants';
import { LANGUAGES } from 'app/config/language.constants';
import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { LoginService } from 'app/login/login.service';
import { ProfileService } from 'app/layouts/profiles/profile.service';
import { EventService } from '../../../entities/event/service/event.service';
import { NotificationService } from '../../../entities/notification/service/notification.service';
import { IEvent } from '../../../entities/event/event.model';
import { HttpResponse } from '@angular/common/http';
import { INotification } from '../../../entities/notification/notification.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CategoryStatusDialogComponent } from '../../../entities/category/status/category-status-dialog.component';
import { NotificationsDialogComponent } from '../../../layouts/notifications-dialog/notifications-dialog.component';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
})
export class HeaderComponent implements OnInit {
  toggleSingle: boolean = true;

  inProduction?: boolean;
  languages = LANGUAGES;
  openAPIEnabled?: boolean;
  version = '';
  account: Account | null = null;
  date = new Date();
  eventsNotifications?: IEvent[];

  constructor(
    private loginService: LoginService,
    private translateService: TranslateService,
    private sessionStorageService: SessionStorageService,
    private accountService: AccountService,
    private profileService: ProfileService,
    private router: Router,
    private eventService: EventService,
    private notificationService: NotificationService,
    private modalService: NgbModal
  ) {
    if (VERSION) {
      this.version = VERSION.toLowerCase().startsWith('v') ? VERSION : 'v' + VERSION;
    }
  }

  ngOnInit(): void {
    this.profileService.getProfileInfo().subscribe(profileInfo => {
      this.inProduction = profileInfo.inProduction;
      this.openAPIEnabled = profileInfo.openAPIEnabled;
    });

    this.accountService.getAuthenticationState().subscribe(async account => (this.account = account));

    this.getNotifications();
  }

  isPremium(): boolean {
    return this.accountService.hasAnyAuthority(Authority.PREMIUM_USER) || this.accountService.hasAnyAuthority(Authority.ADMIN);
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  changeLanguage(languageKey: string): void {
    this.sessionStorageService.store('locale', languageKey);
    this.translateService.use(languageKey);
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  logout(): void {
    this.loginService.logout();
    this.router.navigateByUrl('/landing', { skipLocationChange: true }).then(() => {
      this.router.navigate(['landing']);
    });
  }

  getNotifications(): void {
    this.eventService.queryUserNotificationsUnopened().subscribe((res: HttpResponse<IEvent[]>) => {
      this.eventsNotifications = res.body ?? [];
    });
  }

  updateNotification(notification: INotification): void {
    this.notificationService.seenUpdate(notification).subscribe(() => {
      this.getNotifications();
    });
  }

  openAllNotificationsModal(): void {
    this.eventService.queryUserAllNotifications().subscribe((res: HttpResponse<IEvent[]>) => {
      const modalRef = this.modalService.open(NotificationsDialogComponent, { size: 'lg', backdrop: 'static' });
      modalRef.componentInstance.eventsNotifications = res.body ?? [];
    });
  }
}
