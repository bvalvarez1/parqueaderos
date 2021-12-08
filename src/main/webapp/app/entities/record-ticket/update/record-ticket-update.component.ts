import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IRecordTicket, RecordTicket } from '../record-ticket.model';
import { RecordTicketService } from '../service/record-ticket.service';
import { IPlace } from 'app/entities/place/place.model';
import { PlaceService } from 'app/entities/place/service/place.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IItemCatalogue } from 'app/entities/item-catalogue/item-catalogue.model';
import { ItemCatalogueService } from 'app/entities/item-catalogue/service/item-catalogue.service';
import { ITariffVehicleType } from 'app/entities/tariff-vehicle-type/tariff-vehicle-type.model';
import { TariffVehicleTypeService } from 'app/entities/tariff-vehicle-type/service/tariff-vehicle-type.service';
import { IInstitution } from 'app/entities/institution/institution.model';
import { InstitutionService } from 'app/entities/institution/service/institution.service';

@Component({
  selector: 'jhi-record-ticket-update',
  templateUrl: './record-ticket-update.component.html',
})
export class RecordTicketUpdateComponent implements OnInit {
  isSaving = false;

  placeidsCollection: IPlace[] = [];
  usersSharedCollection: IUser[] = [];
  itemCataloguesSharedCollection: IItemCatalogue[] = [];
  tariffVehicleTypesSharedCollection: ITariffVehicleType[] = [];
  institutionsSharedCollection: IInstitution[] = [];

  editForm = this.fb.group({
    id: [],
    initDate: [null, [Validators.required]],
    endDate: [],
    plate: [],
    parkingTime: [],
    taxableTotal: [],
    taxes: [],
    total: [],
    observation: [],
    sequential: [],
    placeid: [],
    emitter: [],
    collector: [],
    status: [null, Validators.required],
    tariffVehicle: [null, Validators.required],
    institution: [null, Validators.required],
  });

  constructor(
    protected recordTicketService: RecordTicketService,
    protected placeService: PlaceService,
    protected userService: UserService,
    protected itemCatalogueService: ItemCatalogueService,
    protected tariffVehicleTypeService: TariffVehicleTypeService,
    protected institutionService: InstitutionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ recordTicket }) => {
      if (recordTicket.id === undefined) {
        const today = dayjs().startOf('day');
        recordTicket.initDate = today;
        recordTicket.endDate = today;
        recordTicket.parkingTime = today;
      }

      this.updateForm(recordTicket);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const recordTicket = this.createFromForm();
    if (recordTicket.id !== undefined) {
      this.subscribeToSaveResponse(this.recordTicketService.update(recordTicket));
    } else {
      this.subscribeToSaveResponse(this.recordTicketService.create(recordTicket));
    }
  }

  trackPlaceById(index: number, item: IPlace): number {
    return item.id!;
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackItemCatalogueById(index: number, item: IItemCatalogue): number {
    return item.id!;
  }

  trackTariffVehicleTypeById(index: number, item: ITariffVehicleType): number {
    return item.id!;
  }

  trackInstitutionById(index: number, item: IInstitution): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRecordTicket>>): void {
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

  protected updateForm(recordTicket: IRecordTicket): void {
    this.editForm.patchValue({
      id: recordTicket.id,
      initDate: recordTicket.initDate ? recordTicket.initDate.format(DATE_TIME_FORMAT) : null,
      endDate: recordTicket.endDate ? recordTicket.endDate.format(DATE_TIME_FORMAT) : null,
      plate: recordTicket.plate,
      parkingTime: recordTicket.parkingTime ? recordTicket.parkingTime.format(DATE_TIME_FORMAT) : null,
      taxableTotal: recordTicket.taxableTotal,
      taxes: recordTicket.taxes,
      total: recordTicket.total,
      observation: recordTicket.observation,
      sequential: recordTicket.sequential,
      placeid: recordTicket.placeid,
      emitter: recordTicket.emitter,
      collector: recordTicket.collector,
      status: recordTicket.status,
      tariffVehicle: recordTicket.tariffVehicle,
      institution: recordTicket.institution,
    });

    this.placeidsCollection = this.placeService.addPlaceToCollectionIfMissing(this.placeidsCollection, recordTicket.placeid);
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      recordTicket.emitter,
      recordTicket.collector
    );
    this.itemCataloguesSharedCollection = this.itemCatalogueService.addItemCatalogueToCollectionIfMissing(
      this.itemCataloguesSharedCollection,
      recordTicket.status
    );
    this.tariffVehicleTypesSharedCollection = this.tariffVehicleTypeService.addTariffVehicleTypeToCollectionIfMissing(
      this.tariffVehicleTypesSharedCollection,
      recordTicket.tariffVehicle
    );
    this.institutionsSharedCollection = this.institutionService.addInstitutionToCollectionIfMissing(
      this.institutionsSharedCollection,
      recordTicket.institution
    );
  }

  protected loadRelationshipsOptions(): void {
    this.placeService
      .query({ filter: 'recordticket-is-null' })
      .pipe(map((res: HttpResponse<IPlace[]>) => res.body ?? []))
      .pipe(map((places: IPlace[]) => this.placeService.addPlaceToCollectionIfMissing(places, this.editForm.get('placeid')!.value)))
      .subscribe((places: IPlace[]) => (this.placeidsCollection = places));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(
        map((users: IUser[]) =>
          this.userService.addUserToCollectionIfMissing(users, this.editForm.get('emitter')!.value, this.editForm.get('collector')!.value)
        )
      )
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.itemCatalogueService
      .query()
      .pipe(map((res: HttpResponse<IItemCatalogue[]>) => res.body ?? []))
      .pipe(
        map((itemCatalogues: IItemCatalogue[]) =>
          this.itemCatalogueService.addItemCatalogueToCollectionIfMissing(itemCatalogues, this.editForm.get('status')!.value)
        )
      )
      .subscribe((itemCatalogues: IItemCatalogue[]) => (this.itemCataloguesSharedCollection = itemCatalogues));

    this.tariffVehicleTypeService
      .query()
      .pipe(map((res: HttpResponse<ITariffVehicleType[]>) => res.body ?? []))
      .pipe(
        map((tariffVehicleTypes: ITariffVehicleType[]) =>
          this.tariffVehicleTypeService.addTariffVehicleTypeToCollectionIfMissing(
            tariffVehicleTypes,
            this.editForm.get('tariffVehicle')!.value
          )
        )
      )
      .subscribe((tariffVehicleTypes: ITariffVehicleType[]) => (this.tariffVehicleTypesSharedCollection = tariffVehicleTypes));

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

  protected createFromForm(): IRecordTicket {
    return {
      ...new RecordTicket(),
      id: this.editForm.get(['id'])!.value,
      initDate: this.editForm.get(['initDate'])!.value ? dayjs(this.editForm.get(['initDate'])!.value, DATE_TIME_FORMAT) : undefined,
      endDate: this.editForm.get(['endDate'])!.value ? dayjs(this.editForm.get(['endDate'])!.value, DATE_TIME_FORMAT) : undefined,
      plate: this.editForm.get(['plate'])!.value,
      parkingTime: this.editForm.get(['parkingTime'])!.value
        ? dayjs(this.editForm.get(['parkingTime'])!.value, DATE_TIME_FORMAT)
        : undefined,
      taxableTotal: this.editForm.get(['taxableTotal'])!.value,
      taxes: this.editForm.get(['taxes'])!.value,
      total: this.editForm.get(['total'])!.value,
      observation: this.editForm.get(['observation'])!.value,
      sequential: this.editForm.get(['sequential'])!.value,
      placeid: this.editForm.get(['placeid'])!.value,
      emitter: this.editForm.get(['emitter'])!.value,
      collector: this.editForm.get(['collector'])!.value,
      status: this.editForm.get(['status'])!.value,
      tariffVehicle: this.editForm.get(['tariffVehicle'])!.value,
      institution: this.editForm.get(['institution'])!.value,
    };
  }
}
