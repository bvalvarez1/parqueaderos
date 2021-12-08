import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISystemParameterInstitution, SystemParameterInstitution } from '../system-parameter-institution.model';
import { SystemParameterInstitutionService } from '../service/system-parameter-institution.service';
import { ISystemParameters } from 'app/entities/system-parameters/system-parameters.model';
import { SystemParametersService } from 'app/entities/system-parameters/service/system-parameters.service';
import { IInstitution } from 'app/entities/institution/institution.model';
import { InstitutionService } from 'app/entities/institution/service/institution.service';

@Component({
  selector: 'jhi-system-parameter-institution-update',
  templateUrl: './system-parameter-institution-update.component.html',
})
export class SystemParameterInstitutionUpdateComponent implements OnInit {
  isSaving = false;

  systemParametersSharedCollection: ISystemParameters[] = [];
  institutionsSharedCollection: IInstitution[] = [];

  editForm = this.fb.group({
    id: [],
    value: [null, [Validators.required]],
    active: [],
    parameter: [null, Validators.required],
    institution: [null, Validators.required],
  });

  constructor(
    protected systemParameterInstitutionService: SystemParameterInstitutionService,
    protected systemParametersService: SystemParametersService,
    protected institutionService: InstitutionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ systemParameterInstitution }) => {
      this.updateForm(systemParameterInstitution);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const systemParameterInstitution = this.createFromForm();
    if (systemParameterInstitution.id !== undefined) {
      this.subscribeToSaveResponse(this.systemParameterInstitutionService.update(systemParameterInstitution));
    } else {
      this.subscribeToSaveResponse(this.systemParameterInstitutionService.create(systemParameterInstitution));
    }
  }

  trackSystemParametersById(index: number, item: ISystemParameters): number {
    return item.id!;
  }

  trackInstitutionById(index: number, item: IInstitution): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISystemParameterInstitution>>): void {
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

  protected updateForm(systemParameterInstitution: ISystemParameterInstitution): void {
    this.editForm.patchValue({
      id: systemParameterInstitution.id,
      value: systemParameterInstitution.value,
      active: systemParameterInstitution.active,
      parameter: systemParameterInstitution.parameter,
      institution: systemParameterInstitution.institution,
    });

    this.systemParametersSharedCollection = this.systemParametersService.addSystemParametersToCollectionIfMissing(
      this.systemParametersSharedCollection,
      systemParameterInstitution.parameter
    );
    this.institutionsSharedCollection = this.institutionService.addInstitutionToCollectionIfMissing(
      this.institutionsSharedCollection,
      systemParameterInstitution.institution
    );
  }

  protected loadRelationshipsOptions(): void {
    this.systemParametersService
      .query()
      .pipe(map((res: HttpResponse<ISystemParameters[]>) => res.body ?? []))
      .pipe(
        map((systemParameters: ISystemParameters[]) =>
          this.systemParametersService.addSystemParametersToCollectionIfMissing(systemParameters, this.editForm.get('parameter')!.value)
        )
      )
      .subscribe((systemParameters: ISystemParameters[]) => (this.systemParametersSharedCollection = systemParameters));

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

  protected createFromForm(): ISystemParameterInstitution {
    return {
      ...new SystemParameterInstitution(),
      id: this.editForm.get(['id'])!.value,
      value: this.editForm.get(['value'])!.value,
      active: this.editForm.get(['active'])!.value,
      parameter: this.editForm.get(['parameter'])!.value,
      institution: this.editForm.get(['institution'])!.value,
    };
  }
}
