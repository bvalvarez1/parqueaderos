import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUserAuthority } from '../user-authority.model';

@Component({
  selector: 'jhi-user-authority-detail',
  templateUrl: './user-authority-detail.component.html',
})
export class UserAuthorityDetailComponent implements OnInit {
  userAuthority: IUserAuthority | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userAuthority }) => {
      this.userAuthority = userAuthority;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
