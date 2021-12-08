import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IFunctionality, Functionality } from '../functionality.model';
import { FunctionalityService } from '../service/functionality.service';

@Component({
  selector: 'jhi-functionality-update',
  templateUrl: './functionality-update.component.html',
})
export class FunctionalityUpdateComponent implements OnInit {
  isSaving = false;

  functionalitiesSharedCollection: IFunctionality[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    description: [],
    icon: [],
    url: [null, [Validators.maxLength(80)]],
    active: [null, [Validators.required]],
    parent: [],
  });

  constructor(protected functionalityService: FunctionalityService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ functionality }) => {
      this.updateForm(functionality);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const functionality = this.createFromForm();
    if (functionality.id !== undefined) {
      this.subscribeToSaveResponse(this.functionalityService.update(functionality));
    } else {
      this.subscribeToSaveResponse(this.functionalityService.create(functionality));
    }
  }

  trackFunctionalityById(index: number, item: IFunctionality): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFunctionality>>): void {
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

  protected updateForm(functionality: IFunctionality): void {
    this.editForm.patchValue({
      id: functionality.id,
      name: functionality.name,
      description: functionality.description,
      icon: functionality.icon,
      url: functionality.url,
      active: functionality.active,
      parent: functionality.parent,
    });

    this.functionalitiesSharedCollection = this.functionalityService.addFunctionalityToCollectionIfMissing(
      this.functionalitiesSharedCollection,
      functionality.parent
    );
  }

  protected loadRelationshipsOptions(): void {
    this.functionalityService
      .query()
      .pipe(map((res: HttpResponse<IFunctionality[]>) => res.body ?? []))
      .pipe(
        map((functionalities: IFunctionality[]) =>
          this.functionalityService.addFunctionalityToCollectionIfMissing(functionalities, this.editForm.get('parent')!.value)
        )
      )
      .subscribe((functionalities: IFunctionality[]) => (this.functionalitiesSharedCollection = functionalities));
  }

  protected createFromForm(): IFunctionality {
    return {
      ...new Functionality(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      icon: this.editForm.get(['icon'])!.value,
      url: this.editForm.get(['url'])!.value,
      active: this.editForm.get(['active'])!.value,
      parent: this.editForm.get(['parent'])!.value,
    };
  }
}
