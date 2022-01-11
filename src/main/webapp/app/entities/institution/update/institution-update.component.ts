import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IInstitution, Institution } from '../institution.model';
import { InstitutionService } from '../service/institution.service';
import { IItemCatalogue } from 'app/entities/item-catalogue/item-catalogue.model';
import { ItemCatalogueService } from 'app/entities/item-catalogue/service/item-catalogue.service';
import { MapsAPILoader, MouseEvent } from '@agm/core';
@Component({
  selector: 'jhi-institution-update',
  styles: [
    `
      agm-map {
        height: 300px;
      }
    `,
  ],
  templateUrl: './institution-update.component.html',
})
export class InstitutionUpdateComponent implements OnInit {
  isSaving = false;

  itemCataloguesSharedCollection: IItemCatalogue[] = [];
  latitude = -3.9980144;
  longitude = -79.2053783;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    address: [],
    placesNumber: [null, [Validators.required]],
    ruc: [null, [Validators.required]],
    latitude: [],
    longitude: [],
    acronym: [null, [Validators.required]],
    sequencename: 'por defecto',
    canton: [],
  });

  constructor(
    protected institutionService: InstitutionService,
    protected itemCatalogueService: ItemCatalogueService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ institution }) => {
      this.updateForm(institution);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const institution = this.createFromForm();

    // eslint-disable-next-line no-console
    console.log(institution);

    if (institution.id !== undefined) {
      this.subscribeToSaveResponse(this.institutionService.update(institution));
    } else {
      this.subscribeToSaveResponse(this.institutionService.create(institution));
    }
  }

  trackItemCatalogueById(index: number, item: IItemCatalogue): number {
    return item.id!;
  }

  markerDragEnd($event: MouseEvent): void {
    //poner los valores en el formulario
    this.editForm.controls['latitude'].setValue($event.coords.lat);
    this.editForm.controls['longitude'].setValue($event.coords.lng);
    //this.getAddress(this.latitude, this.longitude);
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInstitution>>): void {
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

  protected updateForm(institution: IInstitution): void {
    this.editForm.patchValue({
      id: institution.id,
      name: institution.name,
      address: institution.address,
      placesNumber: institution.placesNumber,
      ruc: institution.ruc,
      latitude: institution.latitude,
      longitude: institution.longitude,
      acronym: institution.acronym,
      sequencename: institution.sequencename,
      canton: institution.canton,
    });

    this.itemCataloguesSharedCollection = this.itemCatalogueService.addItemCatalogueToCollectionIfMissing(
      this.itemCataloguesSharedCollection,
      institution.canton
    );
  }

  protected loadRelationshipsOptions(): void {
    this.itemCatalogueService
      .query()
      .pipe(map((res: HttpResponse<IItemCatalogue[]>) => res.body ?? []))
      .pipe(
        map((itemCatalogues: IItemCatalogue[]) =>
          this.itemCatalogueService.addItemCatalogueToCollectionIfMissing(itemCatalogues, this.editForm.get('canton')!.value)
        )
      )
      .subscribe((itemCatalogues: IItemCatalogue[]) => (this.itemCataloguesSharedCollection = itemCatalogues));
  }

  protected createFromForm(): IInstitution {
    return {
      ...new Institution(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      address: this.editForm.get(['address'])!.value,
      placesNumber: this.editForm.get(['placesNumber'])!.value,
      ruc: this.editForm.get(['ruc'])!.value,
      latitude: this.editForm.get(['latitude'])!.value,
      longitude: this.editForm.get(['longitude'])!.value,
      acronym: this.editForm.get(['acronym'])!.value,
      sequencename: 'por defecto',
      canton: this.editForm.get(['canton'])!.value,
    };
  }
}
