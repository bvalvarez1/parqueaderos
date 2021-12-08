import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IItemCatalogue, ItemCatalogue } from '../item-catalogue.model';
import { ItemCatalogueService } from '../service/item-catalogue.service';
import { ICatalogue } from 'app/entities/catalogue/catalogue.model';
import { CatalogueService } from 'app/entities/catalogue/service/catalogue.service';

@Component({
  selector: 'jhi-item-catalogue-update',
  templateUrl: './item-catalogue-update.component.html',
})
export class ItemCatalogueUpdateComponent implements OnInit {
  isSaving = false;

  cataloguesSharedCollection: ICatalogue[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    code: [null, [Validators.required]],
    description: [],
    catalogCode: [null, [Validators.required]],
    active: [],
    catalogue: [],
  });

  constructor(
    protected itemCatalogueService: ItemCatalogueService,
    protected catalogueService: CatalogueService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ itemCatalogue }) => {
      this.updateForm(itemCatalogue);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const itemCatalogue = this.createFromForm();
    if (itemCatalogue.id !== undefined) {
      this.subscribeToSaveResponse(this.itemCatalogueService.update(itemCatalogue));
    } else {
      this.subscribeToSaveResponse(this.itemCatalogueService.create(itemCatalogue));
    }
  }

  trackCatalogueById(index: number, item: ICatalogue): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IItemCatalogue>>): void {
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

  protected updateForm(itemCatalogue: IItemCatalogue): void {
    this.editForm.patchValue({
      id: itemCatalogue.id,
      name: itemCatalogue.name,
      code: itemCatalogue.code,
      description: itemCatalogue.description,
      catalogCode: itemCatalogue.catalogCode,
      active: itemCatalogue.active,
      catalogue: itemCatalogue.catalogue,
    });

    this.cataloguesSharedCollection = this.catalogueService.addCatalogueToCollectionIfMissing(
      this.cataloguesSharedCollection,
      itemCatalogue.catalogue
    );
  }

  protected loadRelationshipsOptions(): void {
    this.catalogueService
      .query()
      .pipe(map((res: HttpResponse<ICatalogue[]>) => res.body ?? []))
      .pipe(
        map((catalogues: ICatalogue[]) =>
          this.catalogueService.addCatalogueToCollectionIfMissing(catalogues, this.editForm.get('catalogue')!.value)
        )
      )
      .subscribe((catalogues: ICatalogue[]) => (this.cataloguesSharedCollection = catalogues));
  }

  protected createFromForm(): IItemCatalogue {
    return {
      ...new ItemCatalogue(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      code: this.editForm.get(['code'])!.value,
      description: this.editForm.get(['description'])!.value,
      catalogCode: this.editForm.get(['catalogCode'])!.value,
      active: this.editForm.get(['active'])!.value,
      catalogue: this.editForm.get(['catalogue'])!.value,
    };
  }
}
