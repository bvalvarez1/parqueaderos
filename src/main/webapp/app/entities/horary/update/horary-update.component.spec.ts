jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { HoraryService } from '../service/horary.service';
import { IHorary, Horary } from '../horary.model';
import { IItemCatalogue } from 'app/entities/item-catalogue/item-catalogue.model';
import { ItemCatalogueService } from 'app/entities/item-catalogue/service/item-catalogue.service';
import { IContract } from 'app/entities/contract/contract.model';
import { ContractService } from 'app/entities/contract/service/contract.service';

import { HoraryUpdateComponent } from './horary-update.component';

describe('Component Tests', () => {
  describe('Horary Management Update Component', () => {
    let comp: HoraryUpdateComponent;
    let fixture: ComponentFixture<HoraryUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let horaryService: HoraryService;
    let itemCatalogueService: ItemCatalogueService;
    let contractService: ContractService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [HoraryUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(HoraryUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(HoraryUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      horaryService = TestBed.inject(HoraryService);
      itemCatalogueService = TestBed.inject(ItemCatalogueService);
      contractService = TestBed.inject(ContractService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ItemCatalogue query and add missing value', () => {
        const horary: IHorary = { id: 456 };
        const status: IItemCatalogue = { id: 35616 };
        horary.status = status;

        const itemCatalogueCollection: IItemCatalogue[] = [{ id: 326 }];
        jest.spyOn(itemCatalogueService, 'query').mockReturnValue(of(new HttpResponse({ body: itemCatalogueCollection })));
        const additionalItemCatalogues = [status];
        const expectedCollection: IItemCatalogue[] = [...additionalItemCatalogues, ...itemCatalogueCollection];
        jest.spyOn(itemCatalogueService, 'addItemCatalogueToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ horary });
        comp.ngOnInit();

        expect(itemCatalogueService.query).toHaveBeenCalled();
        expect(itemCatalogueService.addItemCatalogueToCollectionIfMissing).toHaveBeenCalledWith(
          itemCatalogueCollection,
          ...additionalItemCatalogues
        );
        expect(comp.itemCataloguesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Contract query and add missing value', () => {
        const horary: IHorary = { id: 456 };
        const contract: IContract = { id: 47885 };
        horary.contract = contract;

        const contractCollection: IContract[] = [{ id: 37074 }];
        jest.spyOn(contractService, 'query').mockReturnValue(of(new HttpResponse({ body: contractCollection })));
        const additionalContracts = [contract];
        const expectedCollection: IContract[] = [...additionalContracts, ...contractCollection];
        jest.spyOn(contractService, 'addContractToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ horary });
        comp.ngOnInit();

        expect(contractService.query).toHaveBeenCalled();
        expect(contractService.addContractToCollectionIfMissing).toHaveBeenCalledWith(contractCollection, ...additionalContracts);
        expect(comp.contractsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const horary: IHorary = { id: 456 };
        const status: IItemCatalogue = { id: 74395 };
        horary.status = status;
        const contract: IContract = { id: 12562 };
        horary.contract = contract;

        activatedRoute.data = of({ horary });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(horary));
        expect(comp.itemCataloguesSharedCollection).toContain(status);
        expect(comp.contractsSharedCollection).toContain(contract);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Horary>>();
        const horary = { id: 123 };
        jest.spyOn(horaryService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ horary });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: horary }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(horaryService.update).toHaveBeenCalledWith(horary);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Horary>>();
        const horary = new Horary();
        jest.spyOn(horaryService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ horary });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: horary }));
        saveSubject.complete();

        // THEN
        expect(horaryService.create).toHaveBeenCalledWith(horary);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Horary>>();
        const horary = { id: 123 };
        jest.spyOn(horaryService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ horary });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(horaryService.update).toHaveBeenCalledWith(horary);
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

      describe('trackContractById', () => {
        it('Should return tracked Contract primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackContractById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
