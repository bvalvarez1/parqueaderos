import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPlace, Place } from '../place.model';
import { PlaceService } from '../service/place.service';
import { IItemCatalogue } from 'app/entities/item-catalogue/item-catalogue.model';
import { ItemCatalogueService } from 'app/entities/item-catalogue/service/item-catalogue.service';
import { IInstitution } from 'app/entities/institution/institution.model';
import { InstitutionService } from 'app/entities/institution/service/institution.service';

@Component({
  selector: 'jhi-place-update',
  templateUrl: './place-update.component.html',
})
export class PlaceUpdateComponent implements OnInit {
  isSaving = false;

  itemCataloguesSharedCollection: IItemCatalogue[] = [];
  institutionsSharedCollection: IInstitution[] = [];

  editForm = this.fb.group({
    id: [],
    number: [null, [Validators.required]],
    status: [],
    institution: [],
  });

  constructor(
    protected placeService: PlaceService,
    protected itemCatalogueService: ItemCatalogueService,
    protected institutionService: InstitutionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ place }) => {
      this.updateForm(place);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const place = this.createFromForm();
    if (place.id !== undefined) {
      this.subscribeToSaveResponse(this.placeService.update(place));
    } else {
      this.subscribeToSaveResponse(this.placeService.create(place));
    }
  }

  trackItemCatalogueById(index: number, item: IItemCatalogue): number {
    return item.id!;
  }

  trackInstitutionById(index: number, item: IInstitution): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlace>>): void {
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

  protected updateForm(place: IPlace): void {
    this.editForm.patchValue({
      id: place.id,
      number: place.number,
      status: place.status,
      institution: place.institution,
    });

    this.itemCataloguesSharedCollection = this.itemCatalogueService.addItemCatalogueToCollectionIfMissing(
      this.itemCataloguesSharedCollection,
      place.status
    );
    this.institutionsSharedCollection = this.institutionService.addInstitutionToCollectionIfMissing(
      this.institutionsSharedCollection,
      place.institution
    );
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

    this.institutionService
      .query()
      .pipe(map((res: HttpResponse<IInstitution[]>) => res.body ?? []))
      .pipe(
        map((institutions: IInstitution[]) =>
          this.institutionService.addInstitutionToCollectionIfMissing(institutions, this.editForm.get('institution')!.value)
        )
      )
      .subscribe((institutions: IInstitution[]) => (this.institutionsSharedCollection = institutions));
  }

  protected createFromForm(): IPlace {
    return {
      ...new Place(),
      id: this.editForm.get(['id'])!.value,
      number: this.editForm.get(['number'])!.value,
      status: this.editForm.get(['status'])!.value,
      institution: this.editForm.get(['institution'])!.value,
    };
  }
}
