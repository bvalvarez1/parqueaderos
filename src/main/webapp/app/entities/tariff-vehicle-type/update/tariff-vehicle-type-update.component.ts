import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ITariffVehicleType, TariffVehicleType } from '../tariff-vehicle-type.model';
import { TariffVehicleTypeService } from '../service/tariff-vehicle-type.service';
import { IItemCatalogue } from 'app/entities/item-catalogue/item-catalogue.model';
import { ItemCatalogueService } from 'app/entities/item-catalogue/service/item-catalogue.service';
import { ITariff } from 'app/entities/tariff/tariff.model';
import { TariffService } from 'app/entities/tariff/service/tariff.service';

@Component({
  selector: 'jhi-tariff-vehicle-type-update',
  templateUrl: './tariff-vehicle-type-update.component.html',
})
export class TariffVehicleTypeUpdateComponent implements OnInit {
  isSaving = false;

  itemCataloguesSharedCollection: IItemCatalogue[] = [];
  tariffsSharedCollection: ITariff[] = [];

  editForm = this.fb.group({
    id: [],
    minValue: [null, [Validators.required]],
    maxValue: [null, [Validators.required]],
    value: [null, [Validators.required]],
    vehicleType: [null, Validators.required],
    tariff: [],
  });

  constructor(
    protected tariffVehicleTypeService: TariffVehicleTypeService,
    protected itemCatalogueService: ItemCatalogueService,
    protected tariffService: TariffService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tariffVehicleType }) => {
      this.updateForm(tariffVehicleType);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tariffVehicleType = this.createFromForm();
    if (tariffVehicleType.id !== undefined) {
      this.subscribeToSaveResponse(this.tariffVehicleTypeService.update(tariffVehicleType));
    } else {
      this.subscribeToSaveResponse(this.tariffVehicleTypeService.create(tariffVehicleType));
    }
  }

  trackItemCatalogueById(index: number, item: IItemCatalogue): number {
    return item.id!;
  }

  trackTariffById(index: number, item: ITariff): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITariffVehicleType>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(tariffVehicleType: ITariffVehicleType): void {
    this.editForm.patchValue({
      id: tariffVehicleType.id,
      minValue: tariffVehicleType.minValue,
      maxValue: tariffVehicleType.maxValue,
      value: tariffVehicleType.value,
      vehicleType: tariffVehicleType.vehicleType,
      tariff: tariffVehicleType.tariff,
    });

    this.itemCataloguesSharedCollection = this.itemCatalogueService.addItemCatalogueToCollectionIfMissing(
      this.itemCataloguesSharedCollection,
      tariffVehicleType.vehicleType
    );
    this.tariffsSharedCollection = this.tariffService.addTariffToCollectionIfMissing(
      this.tariffsSharedCollection,
      tariffVehicleType.tariff
    );
  }

  protected loadRelationshipsOptions(): void {
    this.itemCatalogueService
      .query()
      .pipe(map((res: HttpResponse<IItemCatalogue[]>) => res.body ?? []))
      .pipe(
        map((itemCatalogues: IItemCatalogue[]) =>
          this.itemCatalogueService.addItemCatalogueToCollectionIfMissing(itemCatalogues, this.editForm.get('vehicleType')!.value)
        )
      )
      .subscribe((itemCatalogues: IItemCatalogue[]) => (this.itemCataloguesSharedCollection = itemCatalogues));

    this.tariffService
      .query()
      .pipe(map((res: HttpResponse<ITariff[]>) => res.body ?? []))
      .pipe(map((tariffs: ITariff[]) => this.tariffService.addTariffToCollectionIfMissing(tariffs, this.editForm.get('tariff')!.value)))
      .subscribe((tariffs: ITariff[]) => (this.tariffsSharedCollection = tariffs));
  }

  protected createFromForm(): ITariffVehicleType {
    return {
      ...new TariffVehicleType(),
      id: this.editForm.get(['id'])!.value,
      minValue: this.editForm.get(['minValue'])!.value,
      maxValue: this.editForm.get(['maxValue'])!.value,
      value: this.editForm.get(['value'])!.value,
      vehicleType: this.editForm.get(['vehicleType'])!.value,
      tariff: this.editForm.get(['tariff'])!.value,
    };
  }
}
