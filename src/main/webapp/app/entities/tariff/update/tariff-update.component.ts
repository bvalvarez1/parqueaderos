import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ITariff, Tariff } from '../tariff.model';
import { TariffService } from '../service/tariff.service';
import { IInstitution } from 'app/entities/institution/institution.model';
import { InstitutionService } from 'app/entities/institution/service/institution.service';

@Component({
  selector: 'jhi-tariff-update',
  templateUrl: './tariff-update.component.html',
})
export class TariffUpdateComponent implements OnInit {
  isSaving = false;

  institutionsSharedCollection: IInstitution[] = [];

  editForm = this.fb.group({
    id: [],
    initialDate: [null, [Validators.required]],
    finalDate: [],
    institution: [null, Validators.required],
  });

  constructor(
    protected tariffService: TariffService,
    protected institutionService: InstitutionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tariff }) => {
      this.updateForm(tariff);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tariff = this.createFromForm();
    if (tariff.id !== undefined) {
      this.subscribeToSaveResponse(this.tariffService.update(tariff));
    } else {
      this.subscribeToSaveResponse(this.tariffService.create(tariff));
    }
  }

  trackInstitutionById(index: number, item: IInstitution): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITariff>>): void {
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

  protected updateForm(tariff: ITariff): void {
    this.editForm.patchValue({
      id: tariff.id,
      initialDate: tariff.initialDate,
      finalDate: tariff.finalDate,
      institution: tariff.institution,
    });

    this.institutionsSharedCollection = this.institutionService.addInstitutionToCollectionIfMissing(
      this.institutionsSharedCollection,
      tariff.institution
    );
  }

  protected loadRelationshipsOptions(): void {
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

  protected createFromForm(): ITariff {
    return {
      ...new Tariff(),
      id: this.editForm.get(['id'])!.value,
      initialDate: this.editForm.get(['initialDate'])!.value,
      finalDate: this.editForm.get(['finalDate'])!.value,
      institution: this.editForm.get(['institution'])!.value,
    };
  }
}
