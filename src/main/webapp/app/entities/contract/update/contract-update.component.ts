import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IContract, Contract } from '../contract.model';
import { ContractService } from '../service/contract.service';
import { IItemCatalogue } from 'app/entities/item-catalogue/item-catalogue.model';
import { ItemCatalogueService } from 'app/entities/item-catalogue/service/item-catalogue.service';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';

@Component({
  selector: 'jhi-contract-update',
  templateUrl: './contract-update.component.html',
})
export class ContractUpdateComponent implements OnInit {
  isSaving = false;

  itemCataloguesSharedCollection: IItemCatalogue[] = [];
  peopleSharedCollection: IPerson[] = [];

  editForm = this.fb.group({
    id: [],
    number: [null, [Validators.required]],
    initDate: [null, [Validators.required]],
    endDate: [null, [Validators.required]],
    value: [null, [Validators.required]],
    hours: [],
    status: [null, Validators.required],
    contractor: [null, Validators.required],
  });

  constructor(
    protected contractService: ContractService,
    protected itemCatalogueService: ItemCatalogueService,
    protected personService: PersonService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contract }) => {
      this.updateForm(contract);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contract = this.createFromForm();
    if (contract.id !== undefined) {
      this.subscribeToSaveResponse(this.contractService.update(contract));
    } else {
      this.subscribeToSaveResponse(this.contractService.create(contract));
    }
  }

  trackItemCatalogueById(index: number, item: IItemCatalogue): number {
    return item.id!;
  }

  trackPersonById(index: number, item: IPerson): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContract>>): void {
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

  protected updateForm(contract: IContract): void {
    this.editForm.patchValue({
      id: contract.id,
      number: contract.number,
      initDate: contract.initDate,
      endDate: contract.endDate,
      value: contract.value,
      hours: contract.hours,
      status: contract.status,
      contractor: contract.contractor,
    });

    this.itemCataloguesSharedCollection = this.itemCatalogueService.addItemCatalogueToCollectionIfMissing(
      this.itemCataloguesSharedCollection,
      contract.status
    );
    this.peopleSharedCollection = this.personService.addPersonToCollectionIfMissing(this.peopleSharedCollection, contract.contractor);
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

    this.personService
      .query()
      .pipe(map((res: HttpResponse<IPerson[]>) => res.body ?? []))
      .pipe(map((people: IPerson[]) => this.personService.addPersonToCollectionIfMissing(people, this.editForm.get('contractor')!.value)))
      .subscribe((people: IPerson[]) => (this.peopleSharedCollection = people));
  }

  protected createFromForm(): IContract {
    return {
      ...new Contract(),
      id: this.editForm.get(['id'])!.value,
      number: this.editForm.get(['number'])!.value,
      initDate: this.editForm.get(['initDate'])!.value,
      endDate: this.editForm.get(['endDate'])!.value,
      value: this.editForm.get(['value'])!.value,
      hours: this.editForm.get(['hours'])!.value,
      status: this.editForm.get(['status'])!.value,
      contractor: this.editForm.get(['contractor'])!.value,
    };
  }
}
