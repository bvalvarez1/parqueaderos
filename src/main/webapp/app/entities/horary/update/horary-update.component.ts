import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IHorary, Horary } from '../horary.model';
import { HoraryService } from '../service/horary.service';
import { IItemCatalogue } from 'app/entities/item-catalogue/item-catalogue.model';
import { ItemCatalogueService } from 'app/entities/item-catalogue/service/item-catalogue.service';
import { IContract } from 'app/entities/contract/contract.model';
import { ContractService } from 'app/entities/contract/service/contract.service';

@Component({
  selector: 'jhi-horary-update',
  templateUrl: './horary-update.component.html',
})
export class HoraryUpdateComponent implements OnInit {
  isSaving = false;

  itemCataloguesSharedCollection: IItemCatalogue[] = [];
  contractsSharedCollection: IContract[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    startTime: [null, [Validators.required]],
    finalHour: [null, [Validators.required]],
    status: [null, Validators.required],
    contract: [],
  });

  constructor(
    protected horaryService: HoraryService,
    protected itemCatalogueService: ItemCatalogueService,
    protected contractService: ContractService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ horary }) => {
      if (horary.id === undefined) {
        const today = dayjs().startOf('day');
        horary.startTime = today;
        horary.finalHour = today;
      }

      this.updateForm(horary);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const horary = this.createFromForm();
    if (horary.id !== undefined) {
      this.subscribeToSaveResponse(this.horaryService.update(horary));
    } else {
      this.subscribeToSaveResponse(this.horaryService.create(horary));
    }
  }

  trackItemCatalogueById(index: number, item: IItemCatalogue): number {
    return item.id!;
  }

  trackContractById(index: number, item: IContract): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHorary>>): void {
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

  protected updateForm(horary: IHorary): void {
    this.editForm.patchValue({
      id: horary.id,
      name: horary.name,
      startTime: horary.startTime ? horary.startTime.format(DATE_TIME_FORMAT) : null,
      finalHour: horary.finalHour ? horary.finalHour.format(DATE_TIME_FORMAT) : null,
      status: horary.status,
      contract: horary.contract,
    });

    this.itemCataloguesSharedCollection = this.itemCatalogueService.addItemCatalogueToCollectionIfMissing(
      this.itemCataloguesSharedCollection,
      horary.status
    );
    this.contractsSharedCollection = this.contractService.addContractToCollectionIfMissing(this.contractsSharedCollection, horary.contract);
  }

  protected loadRelationshipsOptions(): void {
    this.itemCatalogueService
      .query()
      .pipe(map((res: HttpResponse<IItemCatalogue[]>) => res.body ?? []))
      .pipe(
        map((itemCatalogues: IItemCatalogue[]) =>
          this.itemCatalogueService.addItemCatalogueToCollectionIfMissing(itemCatalogues, this.editForm.get('status')!.value)
        )
      )
      .subscribe((itemCatalogues: IItemCatalogue[]) => (this.itemCataloguesSharedCollection = itemCatalogues));

    this.contractService
      .query()
      .pipe(map((res: HttpResponse<IContract[]>) => res.body ?? []))
      .pipe(
        map((contracts: IContract[]) =>
          this.contractService.addContractToCollectionIfMissing(contracts, this.editForm.get('contract')!.value)
        )
      )
      .subscribe((contracts: IContract[]) => (this.contractsSharedCollection = contracts));
  }

  protected createFromForm(): IHorary {
    return {
      ...new Horary(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      startTime: this.editForm.get(['startTime'])!.value ? dayjs(this.editForm.get(['startTime'])!.value, DATE_TIME_FORMAT) : undefined,
      finalHour: this.editForm.get(['finalHour'])!.value ? dayjs(this.editForm.get(['finalHour'])!.value, DATE_TIME_FORMAT) : undefined,
      status: this.editForm.get(['status'])!.value,
      contract: this.editForm.get(['contract'])!.value,
    };
  }
}
