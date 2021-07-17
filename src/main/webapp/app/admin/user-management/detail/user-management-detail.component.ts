import { UserManagementService } from './../service/user-management.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { User } from '../user-management.model';

@Component({
  selector: 'jhi-user-mgmt-detail',
  templateUrl: './user-management-detail.component.html',
})
export class UserManagementDetailComponent implements OnInit {
  user: User | null = null;

  constructor(private route: ActivatedRoute, private userService: UserManagementService) {}

  ngOnInit(): void {
    this.route.data.subscribe(({ user }) => {
      this.user = user;
      this.userService.findUserDetails(user.email).subscribe(userDetails => {
        user.country = userDetails.country;
        user.phone = userDetails.phone;
        user.notifications = userDetails.notifications;
        this.user = user;
      });
    });
  }
}
