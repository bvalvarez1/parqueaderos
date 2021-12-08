import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISystemParameterInstitution } from '../system-parameter-institution.model';

@Component({
  selector: 'jhi-system-parameter-institution-detail',
  templateUrl: './system-parameter-institution-detail.component.html',
})
export class SystemParameterInstitutionDetailComponent implements OnInit {
  systemParameterInstitution: ISystemParameterInstitution | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ systemParameterInstitution }) => {
      this.systemParameterInstitution = systemParameterInstitution;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
