import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUserAuthorityInstitution } from '../user-authority-institution.model';

@Component({
  selector: 'jhi-user-authority-institution-detail',
  templateUrl: './user-authority-institution-detail.component.html',
})
export class UserAuthorityInstitutionDetailComponent implements OnInit {
  userAuthorityInstitution: IUserAuthorityInstitution | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userAuthorityInstitution }) => {
      this.userAuthorityInstitution = userAuthorityInstitution;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
