import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITariffVehicleType } from '../tariff-vehicle-type.model';

@Component({
  selector: 'jhi-tariff-vehicle-type-detail',
  templateUrl: './tariff-vehicle-type-detail.component.html',
})
export class TariffVehicleTypeDetailComponent implements OnInit {
  tariffVehicleType: ITariffVehicleType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tariffVehicleType }) => {
      this.tariffVehicleType = tariffVehicleType;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
