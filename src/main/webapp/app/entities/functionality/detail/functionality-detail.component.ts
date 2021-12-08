import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFunctionality } from '../functionality.model';

@Component({
  selector: 'jhi-functionality-detail',
  templateUrl: './functionality-detail.component.html',
})
export class FunctionalityDetailComponent implements OnInit {
  functionality: IFunctionality | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ functionality }) => {
      this.functionality = functionality;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
