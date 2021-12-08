jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TariffVehicleTypeService } from '../service/tariff-vehicle-type.service';
import { ITariffVehicleType, TariffVehicleType } from '../tariff-vehicle-type.model';
import { IItemCatalogue } from 'app/entities/item-catalogue/item-catalogue.model';
import { ItemCatalogueService } from 'app/entities/item-catalogue/service/item-catalogue.service';
import { ITariff } from 'app/entities/tariff/tariff.model';
import { TariffService } from 'app/entities/tariff/service/tariff.service';

import { TariffVehicleTypeUpdateComponent } from './tariff-vehicle-type-update.component';

describe('Component Tests', () => {
  describe('TariffVehicleType Management Update Component', () => {
    let comp: TariffVehicleTypeUpdateComponent;
    let fixture: ComponentFixture<TariffVehicleTypeUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let tariffVehicleTypeService: TariffVehicleTypeService;
    let itemCatalogueService: ItemCatalogueService;
    let tariffService: TariffService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TariffVehicleTypeUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TariffVehicleTypeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TariffVehicleTypeUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      tariffVehicleTypeService = TestBed.inject(TariffVehicleTypeService);
      itemCatalogueService = TestBed.inject(ItemCatalogueService);
      tariffService = TestBed.inject(TariffService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ItemCatalogue query and add missing value', () => {
        const tariffVehicleType: ITariffVehicleType = { id: 456 };
        const vehicleType: IItemCatalogue = { id: 13467 };
        tariffVehicleType.vehicleType = vehicleType;

        const itemCatalogueCollection: IItemCatalogue[] = [{ id: 53808 }];
        jest.spyOn(itemCatalogueService, 'query').mockReturnValue(of(new HttpResponse({ body: itemCatalogueCollection })));
        const additionalItemCatalogues = [vehicleType];
        const expectedCollection: IItemCatalogue[] = [...additionalItemCatalogues, ...itemCatalogueCollection];
        jest.spyOn(itemCatalogueService, 'addItemCatalogueToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ tariffVehicleType });
        comp.ngOnInit();

        expect(itemCatalogueService.query).toHaveBeenCalled();
        expect(itemCatalogueService.addItemCatalogueToCollectionIfMissing).toHaveBeenCalledWith(
          itemCatalogueCollection,
          ...additionalItemCatalogues
        );
        expect(comp.itemCataloguesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Tariff query and add missing value', () => {
        const tariffVehicleType: ITariffVehicleType = { id: 456 };
        const tariff: ITariff = { id: 88933 };
        tariffVehicleType.tariff = tariff;

        const tariffCollection: ITariff[] = [{ id: 4542 }];
        jest.spyOn(tariffService, 'query').mockReturnValue(of(new HttpResponse({ body: tariffCollection })));
        const additionalTariffs = [tariff];
        const expectedCollection: ITariff[] = [...additionalTariffs, ...tariffCollection];
        jest.spyOn(tariffService, 'addTariffToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ tariffVehicleType });
        comp.ngOnInit();

        expect(tariffService.query).toHaveBeenCalled();
        expect(tariffService.addTariffToCollectionIfMissing).toHaveBeenCalledWith(tariffCollection, ...additionalTariffs);
        expect(comp.tariffsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const tariffVehicleType: ITariffVehicleType = { id: 456 };
        const vehicleType: IItemCatalogue = { id: 82285 };
        tariffVehicleType.vehicleType = vehicleType;
        const tariff: ITariff = { id: 50524 };
        tariffVehicleType.tariff = tariff;

        activatedRoute.data = of({ tariffVehicleType });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(tariffVehicleType));
        expect(comp.itemCataloguesSharedCollection).toContain(vehicleType);
        expect(comp.tariffsSharedCollection).toContain(tariff);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TariffVehicleType>>();
        const tariffVehicleType = { id: 123 };
        jest.spyOn(tariffVehicleTypeService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ tariffVehicleType });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tariffVehicleType }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(tariffVehicleTypeService.update).toHaveBeenCalledWith(tariffVehicleType);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TariffVehicleType>>();
        const tariffVehicleType = new TariffVehicleType();
        jest.spyOn(tariffVehicleTypeService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ tariffVehicleType });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tariffVehicleType }));
        saveSubject.complete();

        // THEN
        expect(tariffVehicleTypeService.create).toHaveBeenCalledWith(tariffVehicleType);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TariffVehicleType>>();
        const tariffVehicleType = { id: 123 };
        jest.spyOn(tariffVehicleTypeService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ tariffVehicleType });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(tariffVehicleTypeService.update).toHaveBeenCalledWith(tariffVehicleType);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackItemCatalogueById', () => {
        it('Should return tracked ItemCatalogue primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackItemCatalogueById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackTariffById', () => {
        it('Should return tracked Tariff primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackTariffById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
