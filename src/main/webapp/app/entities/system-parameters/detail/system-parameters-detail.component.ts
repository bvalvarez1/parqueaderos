import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISystemParameters } from '../system-parameters.model';

@Component({
  selector: 'jhi-system-parameters-detail',
  templateUrl: './system-parameters-detail.component.html',
})
export class SystemParametersDetailComponent implements OnInit {
  systemParameters: ISystemParameters | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ systemParameters }) => {
      this.systemParameters = systemParameters;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
