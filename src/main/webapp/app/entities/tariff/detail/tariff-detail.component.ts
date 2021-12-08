import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITariff } from '../tariff.model';

@Component({
  selector: 'jhi-tariff-detail',
  templateUrl: './tariff-detail.component.html',
})
export class TariffDetailComponent implements OnInit {
  tariff: ITariff | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tariff }) => {
      this.tariff = tariff;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
