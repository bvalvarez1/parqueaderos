import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IInstitution } from '../parking.model';

import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/config/pagination.constants';
import { ParkingService } from '../service/parking.service';
import { IPlaces } from '../places.model';
import { IPlaceStatusTotal } from '../placesStatusTotal.model';
import { InstitutionService } from 'app/entities/institution/service/institution.service';

import { NewTicketComponent } from '../newticket/newticket.component';
import { IInstitutionTarrif } from '../parkingtariff.model';
import { ITariffVehicleType } from 'app/entities/tariff-vehicle-type/tariff-vehicle-type.model';
import { FormBuilder, Validators } from '@angular/forms';
import { RecordTicketService } from 'app/entities/record-ticket/service/record-ticket.service';
import { IRecordTicket, RecordTicket } from 'app/entities/record-ticket/record-ticket.model';
import { TicketDeleteDialogComponent } from '../delete/ticket-delete-dialog.component';
import { CompileNgModuleMetadata } from '@angular/compiler';
import { ConfirmReserveDialogComponent } from './reserve/confirmreserve-dialog.component';
@Component({
  selector: 'jhi-parking',
  templateUrl: './parking.component.html',
})
export class ParkingComponent implements OnInit {
  institutions?: IInstitution[];
  tariffs?: ITariffVehicleType[];
  placesTickets?: IPlaces[];
  placesStatusTotal?: IPlaceStatusTotal[];
  freePlaces?: number;
  busyPlaces?: number | 0;
  institutionName?: string;
  criteria?: string;
  institution?: IInstitution;
  places?: number;
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  ticketForPay?: IRecordTicket;
  enableButtons?: boolean | false;

  outForm = this.fb.group({
    ticket: [null, [Validators.required]],
  });

  constructor(
    protected parkingService: ParkingService,
    protected institutionService: InstitutionService,
    protected recordticketService: RecordTicketService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected fb: FormBuilder,
    protected modalService: NgbModal
  ) {}

  loadPage(): void {
    this.isLoading = true;
  }

  ngOnInit(): void {
    this.handleNavigation();
    this.loadData();
  }

  loadData(): void {
    this.loadPage();
    this.institutionService.getInstitutionName().subscribe(
      (res: HttpResponse<IInstitutionTarrif>) => {
        this.isLoading = false;
        if (res.body != null) {
          this.institution = res.body.institution;
          this.tariffs = res.body.tarrifs;
        }
      },
      () => {
        this.isLoading = false;
        this.onError();
      }
    );

    this.parkingService.getPlacesInstitution().subscribe(
      (res: HttpResponse<IPlaces[]>) => {
        this.isLoading = false;

        if (res.body != null) {
          this.placesTickets = res.body;
        }
      },
      () => {
        this.isLoading = false;
        this.onError();
      }
    );

    this.parkingService.totalByStatus().subscribe(
      (res: HttpResponse<IPlaceStatusTotal[]>) => {
        this.isLoading = false;

        if (res.body != null) {
          this.placesStatusTotal = res.body;
          this.freePlaces = this.placesStatusTotal.find(i => i.status === 'DISPONIBLE')?.total ?? 0;
          this.busyPlaces = this.placesStatusTotal.find(i => i.status === 'OCUPADO')?.total ?? 0;

          // eslint-disable-next-line no-console
          console.log('-0-0-0-0-0-0-0');

          // eslint-disable-next-line no-console
          console.log(this.busyPlaces);
        }
      },
      () => {
        this.isLoading = false;
        this.onError();
      }
    );

    this.enableButtons = false;
    this.ticketForPay = undefined;
  }

  trackId(index: number, item: IInstitution): number {
    return item.id!;
  }

  outTicket(): void {
    // eslint-disable-next-line no-console
    console.log('----1--');
  }

  newTicket(): void {
    const modalRef = this.modalService.open(NewTicketComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.institution = this.institution;
    // eslint-disable-next-line no-console
    console.log(this.institution);

    modalRef.componentInstance.tariffs = this.tariffs;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'new') {
        this.loadData();
      }
    });
  }

  putValue(ticket: any | ''): void {
    this.outForm.controls['ticket'].setValue(ticket);
    this.ticketForPay = new RecordTicket();
    this.enableButtons = false;

    if (ticket !== null) {
      this.calculateTimeParking();
    }
  }

  calculateTimeParking(): void {
    const ticket = this.outForm.get(['ticket'])!.value;
    const institutionid = this.institution!.id!;

    this.recordticketService.findByTicket(ticket, institutionid).subscribe(
      (res: HttpResponse<IRecordTicket>) => {
        this.isLoading = false;
        // eslint-disable-next-line no-console
        console.log(res.body);
        if (res.body != null) {
          const ticketbd = res.body;
          if (ticketbd.status!.name === 'PRE EMITIDO') {
            // eslint-disable-next-line no-console
            console.log('A CONFIRMAR RESERVA');
            const modalRef = this.modalService.open(ConfirmReserveDialogComponent, { size: 'lg', backdrop: 'static' });
            modalRef.componentInstance.ticket = ticketbd;

            // unsubscribe not needed because closed completes on modal close
            modalRef.closed.subscribe(reason => {
              if (reason === 'confirmed') {
                this.loadData();
              }
            });
          } else if (ticketbd.status!.name === 'EMITIDO') {
            this.ticketForPay = ticketbd;
          }
        }
      },
      () => {
        this.isLoading = false;
        this.onError();
      }
    );

    this.enableButtons = true;
  }

  /**
   * Boton de pago de ticket
   */
  payTicket(): void {
    if (this.ticketForPay) {
      // llamar al servicio
      // eslint-disable-next-line no-console
      console.log(this.ticketForPay);
      this.recordticketService.payTicket(this.ticketForPay).subscribe(
        (res: HttpResponse<IRecordTicket>) => {
          this.isLoading = false;
          // eslint-disable-next-line no-console
          console.log(res.body);
          if (res.body != null) {
            //this.ticketForPay = res.body;
            this.loadData();
          }
        },
        () => {
          this.isLoading = false;
          this.onError();
        }
      );
      this.enableButtons = false;
    }
  }

  /**
   * Anular un ticket
   */
  cancelTicket(): void {
    const modalRef = this.modalService.open(TicketDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.ticket = this.ticketForPay;

    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadData();
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected handleNavigation(): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      const page = params.get('page');
      const pageNumber = +(page ?? 1);
      const sort = (params.get(SORT) ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === ASC;
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage();
      }
    });
  }

  protected onSuccess(data: IInstitution[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/institution'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? ASC : DESC),
        },
      });
    }
    this.institutions = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
