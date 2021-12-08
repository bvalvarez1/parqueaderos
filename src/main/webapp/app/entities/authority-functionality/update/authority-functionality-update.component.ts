import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAuthorityFunctionality, AuthorityFunctionality } from '../authority-functionality.model';
import { AuthorityFunctionalityService } from '../service/authority-functionality.service';
import { IFunctionality } from 'app/entities/functionality/functionality.model';
import { FunctionalityService } from 'app/entities/functionality/service/functionality.service';

@Component({
  selector: 'jhi-authority-functionality-update',
  templateUrl: './authority-functionality-update.component.html',
})
export class AuthorityFunctionalityUpdateComponent implements OnInit {
  isSaving = false;

  functionalitiesSharedCollection: IFunctionality[] = [];

  editForm = this.fb.group({
    id: [],
    authority: [null, [Validators.required]],
    priority: [null, [Validators.required]],
    active: [],
    functionality: [null, Validators.required],
  });

  constructor(
    protected authorityFunctionalityService: AuthorityFunctionalityService,
    protected functionalityService: FunctionalityService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ authorityFunctionality }) => {
      this.updateForm(authorityFunctionality);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const authorityFunctionality = this.createFromForm();
    if (authorityFunctionality.id !== undefined) {
      this.subscribeToSaveResponse(this.authorityFunctionalityService.update(authorityFunctionality));
    } else {
      this.subscribeToSaveResponse(this.authorityFunctionalityService.create(authorityFunctionality));
    }
  }

  trackFunctionalityById(index: number, item: IFunctionality): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAuthorityFunctionality>>): void {
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

  protected updateForm(authorityFunctionality: IAuthorityFunctionality): void {
    this.editForm.patchValue({
      id: authorityFunctionality.id,
      authority: authorityFunctionality.authority,
      priority: authorityFunctionality.priority,
      active: authorityFunctionality.active,
      functionality: authorityFunctionality.functionality,
    });

    this.functionalitiesSharedCollection = this.functionalityService.addFunctionalityToCollectionIfMissing(
      this.functionalitiesSharedCollection,
      authorityFunctionality.functionality
    );
  }

  protected loadRelationshipsOptions(): void {
    this.functionalityService
      .query()
      .pipe(map((res: HttpResponse<IFunctionality[]>) => res.body ?? []))
      .pipe(
        map((functionalities: IFunctionality[]) =>
          this.functionalityService.addFunctionalityToCollectionIfMissing(functionalities, this.editForm.get('functionality')!.value)
        )
      )
      .subscribe((functionalities: IFunctionality[]) => (this.functionalitiesSharedCollection = functionalities));
  }

  protected createFromForm(): IAuthorityFunctionality {
    return {
      ...new AuthorityFunctionality(),
      id: this.editForm.get(['id'])!.value,
      authority: this.editForm.get(['authority'])!.value,
      priority: this.editForm.get(['priority'])!.value,
      active: this.editForm.get(['active'])!.value,
      functionality: this.editForm.get(['functionality'])!.value,
    };
  }
}
