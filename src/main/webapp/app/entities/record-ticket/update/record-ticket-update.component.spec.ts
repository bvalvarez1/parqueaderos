jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { RecordTicketService } from '../service/record-ticket.service';
import { IRecordTicket, RecordTicket } from '../record-ticket.model';
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

import { RecordTicketUpdateComponent } from './record-ticket-update.component';

describe('Component Tests', () => {
  describe('RecordTicket Management Update Component', () => {
    let comp: RecordTicketUpdateComponent;
    let fixture: ComponentFixture<RecordTicketUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let recordTicketService: RecordTicketService;
    let placeService: PlaceService;
    let userService: UserService;
    let itemCatalogueService: ItemCatalogueService;
    let tariffVehicleTypeService: TariffVehicleTypeService;
    let institutionService: InstitutionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [RecordTicketUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(RecordTicketUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RecordTicketUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      recordTicketService = TestBed.inject(RecordTicketService);
      placeService = TestBed.inject(PlaceService);
      userService = TestBed.inject(UserService);
      itemCatalogueService = TestBed.inject(ItemCatalogueService);
      tariffVehicleTypeService = TestBed.inject(TariffVehicleTypeService);
      institutionService = TestBed.inject(InstitutionService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call placeid query and add missing value', () => {
        const recordTicket: IRecordTicket = { id: 456 };
        const placeid: IPlace = { id: 4785 };
        recordTicket.placeid = placeid;

        const placeidCollection: IPlace[] = [{ id: 2046 }];
        jest.spyOn(placeService, 'query').mockReturnValue(of(new HttpResponse({ body: placeidCollection })));
        const expectedCollection: IPlace[] = [placeid, ...placeidCollection];
        jest.spyOn(placeService, 'addPlaceToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ recordTicket });
        comp.ngOnInit();

        expect(placeService.query).toHaveBeenCalled();
        expect(placeService.addPlaceToCollectionIfMissing).toHaveBeenCalledWith(placeidCollection, placeid);
        expect(comp.placeidsCollection).toEqual(expectedCollection);
      });

      it('Should call User query and add missing value', () => {
        const recordTicket: IRecordTicket = { id: 456 };
        const emitter: IUser = { id: 77943 };
        recordTicket.emitter = emitter;
        const collector: IUser = { id: 59910 };
        recordTicket.collector = collector;

        const userCollection: IUser[] = [{ id: 41289 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [emitter, collector];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ recordTicket });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call ItemCatalogue query and add missing value', () => {
        const recordTicket: IRecordTicket = { id: 456 };
        const status: IItemCatalogue = { id: 43681 };
        recordTicket.status = status;

        const itemCatalogueCollection: IItemCatalogue[] = [{ id: 92306 }];
        jest.spyOn(itemCatalogueService, 'query').mockReturnValue(of(new HttpResponse({ body: itemCatalogueCollection })));
        const additionalItemCatalogues = [status];
        const expectedCollection: IItemCatalogue[] = [...additionalItemCatalogues, ...itemCatalogueCollection];
        jest.spyOn(itemCatalogueService, 'addItemCatalogueToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ recordTicket });
        comp.ngOnInit();

        expect(itemCatalogueService.query).toHaveBeenCalled();
        expect(itemCatalogueService.addItemCatalogueToCollectionIfMissing).toHaveBeenCalledWith(
          itemCatalogueCollection,
          ...additionalItemCatalogues
        );
        expect(comp.itemCataloguesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call TariffVehicleType query and add missing value', () => {
        const recordTicket: IRecordTicket = { id: 456 };
        const tariffVehicle: ITariffVehicleType = { id: 37323 };
        recordTicket.tariffVehicle = tariffVehicle;

        const tariffVehicleTypeCollection: ITariffVehicleType[] = [{ id: 52139 }];
        jest.spyOn(tariffVehicleTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: tariffVehicleTypeCollection })));
        const additionalTariffVehicleTypes = [tariffVehicle];
        const expectedCollection: ITariffVehicleType[] = [...additionalTariffVehicleTypes, ...tariffVehicleTypeCollection];
        jest.spyOn(tariffVehicleTypeService, 'addTariffVehicleTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ recordTicket });
        comp.ngOnInit();

        expect(tariffVehicleTypeService.query).toHaveBeenCalled();
        expect(tariffVehicleTypeService.addTariffVehicleTypeToCollectionIfMissing).toHaveBeenCalledWith(
          tariffVehicleTypeCollection,
          ...additionalTariffVehicleTypes
        );
        expect(comp.tariffVehicleTypesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Institution query and add missing value', () => {
        const recordTicket: IRecordTicket = { id: 456 };
        const institution: IInstitution = { id: 35041 };
        recordTicket.institution = institution;

        const institutionCollection: IInstitution[] = [{ id: 28597 }];
        jest.spyOn(institutionService, 'query').mockReturnValue(of(new HttpResponse({ body: institutionCollection })));
        const additionalInstitutions = [institution];
        const expectedCollection: IInstitution[] = [...additionalInstitutions, ...institutionCollection];
        jest.spyOn(institutionService, 'addInstitutionToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ recordTicket });
        comp.ngOnInit();

        expect(institutionService.query).toHaveBeenCalled();
        expect(institutionService.addInstitutionToCollectionIfMissing).toHaveBeenCalledWith(
          institutionCollection,
          ...additionalInstitutions
        );
        expect(comp.institutionsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const recordTicket: IRecordTicket = { id: 456 };
        const placeid: IPlace = { id: 56239 };
        recordTicket.placeid = placeid;
        const emitter: IUser = { id: 12731 };
        recordTicket.emitter = emitter;
        const collector: IUser = { id: 14039 };
        recordTicket.collector = collector;
        const status: IItemCatalogue = { id: 40695 };
        recordTicket.status = status;
        const tariffVehicle: ITariffVehicleType = { id: 15998 };
        recordTicket.tariffVehicle = tariffVehicle;
        const institution: IInstitution = { id: 7124 };
        recordTicket.institution = institution;

        activatedRoute.data = of({ recordTicket });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(recordTicket));
        expect(comp.placeidsCollection).toContain(placeid);
        expect(comp.usersSharedCollection).toContain(emitter);
        expect(comp.usersSharedCollection).toContain(collector);
        expect(comp.itemCataloguesSharedCollection).toContain(status);
        expect(comp.tariffVehicleTypesSharedCollection).toContain(tariffVehicle);
        expect(comp.institutionsSharedCollection).toContain(institution);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<RecordTicket>>();
        const recordTicket = { id: 123 };
        jest.spyOn(recordTicketService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ recordTicket });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: recordTicket }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(recordTicketService.update).toHaveBeenCalledWith(recordTicket);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<RecordTicket>>();
        const recordTicket = new RecordTicket();
        jest.spyOn(recordTicketService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ recordTicket });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: recordTicket }));
        saveSubject.complete();

        // THEN
        expect(recordTicketService.create).toHaveBeenCalledWith(recordTicket);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<RecordTicket>>();
        const recordTicket = { id: 123 };
        jest.spyOn(recordTicketService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ recordTicket });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(recordTicketService.update).toHaveBeenCalledWith(recordTicket);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackPlaceById', () => {
        it('Should return tracked Place primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPlaceById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackItemCatalogueById', () => {
        it('Should return tracked ItemCatalogue primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackItemCatalogueById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackTariffVehicleTypeById', () => {
        it('Should return tracked TariffVehicleType primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackTariffVehicleTypeById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackInstitutionById', () => {
        it('Should return tracked Institution primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackInstitutionById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
