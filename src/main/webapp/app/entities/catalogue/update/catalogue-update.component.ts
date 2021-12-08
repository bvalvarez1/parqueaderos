import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICatalogue, Catalogue } from '../catalogue.model';
import { CatalogueService } from '../service/catalogue.service';

@Component({
  selector: 'jhi-catalogue-update',
  templateUrl: './catalogue-update.component.html',
})
export class CatalogueUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    code: [null, [Validators.required]],
    description: [],
    active: [],
  });

  constructor(protected catalogueService: CatalogueService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ catalogue }) => {
      this.updateForm(catalogue);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const catalogue = this.createFromForm();
    if (catalogue.id !== undefined) {
      this.subscribeToSaveResponse(this.catalogueService.update(catalogue));
    } else {
      this.subscribeToSaveResponse(this.catalogueService.create(catalogue));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICatalogue>>): void {
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

  protected updateForm(catalogue: ICatalogue): void {
    this.editForm.patchValue({
      id: catalogue.id,
      name: catalogue.name,
      code: catalogue.code,
      description: catalogue.description,
      active: catalogue.active,
    });
  }

  protected createFromForm(): ICatalogue {
    return {
      ...new Catalogue(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      code: this.editForm.get(['code'])!.value,
      description: this.editForm.get(['description'])!.value,
      active: this.editForm.get(['active'])!.value,
    };
  }
}
