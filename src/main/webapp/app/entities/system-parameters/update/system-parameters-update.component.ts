import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ISystemParameters, SystemParameters } from '../system-parameters.model';
import { SystemParametersService } from '../service/system-parameters.service';

@Component({
  selector: 'jhi-system-parameters-update',
  templateUrl: './system-parameters-update.component.html',
})
export class SystemParametersUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    code: [null, [Validators.required]],
    clase: [null, [Validators.required]],
  });

  constructor(
    protected systemParametersService: SystemParametersService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ systemParameters }) => {
      this.updateForm(systemParameters);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const systemParameters = this.createFromForm();
    if (systemParameters.id !== undefined) {
      this.subscribeToSaveResponse(this.systemParametersService.update(systemParameters));
    } else {
      this.subscribeToSaveResponse(this.systemParametersService.create(systemParameters));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISystemParameters>>): void {
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

  protected updateForm(systemParameters: ISystemParameters): void {
    this.editForm.patchValue({
      id: systemParameters.id,
      name: systemParameters.name,
      code: systemParameters.code,
      clase: systemParameters.clase,
    });
  }

  protected createFromForm(): ISystemParameters {
    return {
      ...new SystemParameters(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      code: this.editForm.get(['code'])!.value,
      clase: this.editForm.get(['clase'])!.value,
    };
  }
}
