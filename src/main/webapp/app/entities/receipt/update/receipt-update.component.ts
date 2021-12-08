import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IReceipt, Receipt } from '../receipt.model';
import { ReceiptService } from '../service/receipt.service';
import { IRecordTicket } from 'app/entities/record-ticket/record-ticket.model';
import { RecordTicketService } from 'app/entities/record-ticket/service/record-ticket.service';

@Component({
  selector: 'jhi-receipt-update',
  templateUrl: './receipt-update.component.html',
})
export class ReceiptUpdateComponent implements OnInit {
  isSaving = false;

  recordticketidsCollection: IRecordTicket[] = [];

  editForm = this.fb.group({
    id: [],
    authorizationNumber: [],
    sequential: [],
    status: [],
    sriaccesskey: [],
    sriauthorizationdate: [],
    receiptdate: [],
    recordticketid: [],
  });

  constructor(
    protected receiptService: ReceiptService,
    protected recordTicketService: RecordTicketService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ receipt }) => {
      this.updateForm(receipt);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const receipt = this.createFromForm();
    if (receipt.id !== undefined) {
      this.subscribeToSaveResponse(this.receiptService.update(receipt));
    } else {
      this.subscribeToSaveResponse(this.receiptService.create(receipt));
    }
  }

  trackRecordTicketById(index: number, item: IRecordTicket): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReceipt>>): void {
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

  protected updateForm(receipt: IReceipt): void {
    this.editForm.patchValue({
      id: receipt.id,
      authorizationNumber: receipt.authorizationNumber,
      sequential: receipt.sequential,
      status: receipt.status,
      sriaccesskey: receipt.sriaccesskey,
      sriauthorizationdate: receipt.sriauthorizationdate,
      receiptdate: receipt.receiptdate,
      recordticketid: receipt.recordticketid,
    });

    this.recordticketidsCollection = this.recordTicketService.addRecordTicketToCollectionIfMissing(
      this.recordticketidsCollection,
      receipt.recordticketid
    );
  }

  protected loadRelationshipsOptions(): void {
    this.recordTicketService
      .query({ filter: 'receipt-is-null' })
      .pipe(map((res: HttpResponse<IRecordTicket[]>) => res.body ?? []))
      .pipe(
        map((recordTickets: IRecordTicket[]) =>
          this.recordTicketService.addRecordTicketToCollectionIfMissing(recordTickets, this.editForm.get('recordticketid')!.value)
        )
      )
      .subscribe((recordTickets: IRecordTicket[]) => (this.recordticketidsCollection = recordTickets));
  }

  protected createFromForm(): IReceipt {
    return {
      ...new Receipt(),
      id: this.editForm.get(['id'])!.value,
      authorizationNumber: this.editForm.get(['authorizationNumber'])!.value,
      sequential: this.editForm.get(['sequential'])!.value,
      status: this.editForm.get(['status'])!.value,
      sriaccesskey: this.editForm.get(['sriaccesskey'])!.value,
      sriauthorizationdate: this.editForm.get(['sriauthorizationdate'])!.value,
      receiptdate: this.editForm.get(['receiptdate'])!.value,
      recordticketid: this.editForm.get(['recordticketid'])!.value,
    };
  }
}
