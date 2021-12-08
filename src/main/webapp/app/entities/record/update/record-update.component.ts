import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IRecord, Record } from '../record.model';
import { RecordService } from '../service/record.service';

@Component({
  selector: 'jhi-record-update',
  templateUrl: './record-update.component.html',
})
export class RecordUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
  });

  constructor(protected recordService: RecordService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ record }) => {
      this.updateForm(record);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const record = this.createFromForm();
    if (record.id !== undefined) {
      this.subscribeToSaveResponse(this.recordService.update(record));
    } else {
      this.subscribeToSaveResponse(this.recordService.create(record));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRecord>>): void {
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

  protected updateForm(record: IRecord): void {
    this.editForm.patchValue({
      id: record.id,
    });
  }

  protected createFromForm(): IRecord {
    return {
      ...new Record(),
      id: this.editForm.get(['id'])!.value,
    };
  }
}
