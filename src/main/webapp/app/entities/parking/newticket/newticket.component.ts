import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder, Validators } from '@angular/forms';
import * as dayjs from 'dayjs';

import { IInstitution } from '../parking.model';
import { ParkingService } from '../service/parking.service';
import { IRecordTicket, RecordTicket } from 'app/entities/record-ticket/record-ticket.model';
import { RecordTicketService } from 'app/entities/record-ticket/service/record-ticket.service';
import { Observable } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { finalize } from 'rxjs/operators';
import { ITariffVehicleType } from 'app/entities/tariff-vehicle-type/tariff-vehicle-type.model';

@Component({
  templateUrl: './newticket.component.html',
})
export class NewTicketComponent {
  institution?: IInstitution;
  tariffs?: ITariffVehicleType[];
  isSaving = false;

  newForm = this.fb.group({
    plate: [null, [Validators.required]],
    tariffType: [null, [Validators.required]],
  });

  constructor(
    protected institutionService: ParkingService,
    protected activeModal: NgbActiveModal,
    protected fb: FormBuilder,
    protected recordTicketService: RecordTicketService
  ) {}

  previousState(): void {
    window.history.back();
  }

  cancel(): void {
    this.activeModal.dismiss();
  }

  trackTariffVehicleTypeById(index: number, item: ITariffVehicleType): number {
    return item.id!;
  }

  newTicket(): void {
    /*
    this.institutionService.delete(id).subscribe(() => {
      this.activeModal.close('new');
    });*/
    const recordTicket = this.createFromForm();

    // eslint-disable-next-line no-console
    console.log('----------------------12312312');

    // eslint-disable-next-line no-console
    console.log(recordTicket);

    // eslint-disable-next-line no-console
    console.log(recordTicket);
    this.subscribeToSaveResponse(this.recordTicketService.createFromParking(recordTicket));
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRecordTicket>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.activeModal.close('new');
  }

  protected onSaveError(): void {
    // TODO
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected createFromForm(): IRecordTicket {
    return {
      ...new RecordTicket(),
      initDate: dayjs(new Date()),
      plate: this.newForm.get(['plate'])!.value,
      institution: this.institution,
      tariffVehicle: this.newForm.get(['tariffType'])!.value,
    };
  }
}
