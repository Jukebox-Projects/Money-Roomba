import { ProfileService } from 'app/layouts/profiles/profile.service';
import { AccountService } from 'app/core/auth/account.service';
import { SessionStorageService } from 'ngx-webstorage';
import { TranslateService } from '@ngx-translate/core';
import { LoginService } from 'app/login/login.service';
import { Account } from 'app/core/auth/account.model';
import { LANGUAGES } from 'app/config/language.constants';
import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { Router } from '@angular/router';

import { VERSION } from 'app/app.constants';
@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.css'],
})
export class NavigationComponent implements OnInit {
  public currentHref: string = '';
  inProduction?: boolean;
  isNavbarCollapsed = true;
  languages = LANGUAGES;
  openAPIEnabled?: boolean;
  version = '';
  account: Account | null = null;

  constructor(
    location: Location,
    router: Router,
    private loginService: LoginService,
    private translateService: TranslateService,
    private sessionStorageService: SessionStorageService,
    private accountService: AccountService,
    private profileService: ProfileService
  ) {
    if (VERSION) {
      this.version = VERSION.toLowerCase().startsWith('v') ? VERSION : 'v' + VERSION;
    }
    router.events.subscribe(val => {
      if (location.path() != '') {
        this.currentHref = location.path();
      } else {
        this.currentHref = 'Home';
      }
    });
  }

  ngOnInit(): void {
    this.profileService.getProfileInfo().subscribe(profileInfo => {
      this.inProduction = profileInfo.inProduction;
      this.openAPIEnabled = profileInfo.openAPIEnabled;
    });
    this.accountService.getAuthenticationState().subscribe(account => (this.account = account));
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  toggleIcon: boolean = true;

  toggleLoveIcon() {
    this.toggleIcon = !this.toggleIcon;
  }

  dashboardArray = ['/', '/wallet', '/scheduled-transaction', '/category', '/transaction'];

  adminArray = [
    '/admin/user-management',
    '/admin/metrics',
    '/admin/health',
    '/admin/logs',
    '/admin/docs',
    '/admin/configuration',
    '/system-setting',
    '/license',
  ];
}
