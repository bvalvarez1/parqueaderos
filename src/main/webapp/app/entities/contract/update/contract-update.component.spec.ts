jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ContractService } from '../service/contract.service';
import { IContract, Contract } from '../contract.model';
import { IItemCatalogue } from 'app/entities/item-catalogue/item-catalogue.model';
import { ItemCatalogueService } from 'app/entities/item-catalogue/service/item-catalogue.service';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';

import { ContractUpdateComponent } from './contract-update.component';

describe('Component Tests', () => {
  describe('Contract Management Update Component', () => {
    let comp: ContractUpdateComponent;
    let fixture: ComponentFixture<ContractUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let contractService: ContractService;
    let itemCatalogueService: ItemCatalogueService;
    let personService: PersonService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ContractUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ContractUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ContractUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      contractService = TestBed.inject(ContractService);
      itemCatalogueService = TestBed.inject(ItemCatalogueService);
      personService = TestBed.inject(PersonService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ItemCatalogue query and add missing value', () => {
        const contract: IContract = { id: 456 };
        const status: IItemCatalogue = { id: 23255 };
        contract.status = status;

        const itemCatalogueCollection: IItemCatalogue[] = [{ id: 4212 }];
        jest.spyOn(itemCatalogueService, 'query').mockReturnValue(of(new HttpResponse({ body: itemCatalogueCollection })));
        const additionalItemCatalogues = [status];
        const expectedCollection: IItemCatalogue[] = [...additionalItemCatalogues, ...itemCatalogueCollection];
        jest.spyOn(itemCatalogueService, 'addItemCatalogueToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ contract });
        comp.ngOnInit();

        expect(itemCatalogueService.query).toHaveBeenCalled();
        expect(itemCatalogueService.addItemCatalogueToCollectionIfMissing).toHaveBeenCalledWith(
          itemCatalogueCollection,
          ...additionalItemCatalogues
        );
        expect(comp.itemCataloguesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Person query and add missing value', () => {
        const contract: IContract = { id: 456 };
        const contractor: IPerson = { id: 39538 };
        contract.contractor = contractor;

        const personCollection: IPerson[] = [{ id: 30429 }];
        jest.spyOn(personService, 'query').mockReturnValue(of(new HttpResponse({ body: personCollection })));
        const additionalPeople = [contractor];
        const expectedCollection: IPerson[] = [...additionalPeople, ...personCollection];
        jest.spyOn(personService, 'addPersonToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ contract });
        comp.ngOnInit();

        expect(personService.query).toHaveBeenCalled();
        expect(personService.addPersonToCollectionIfMissing).toHaveBeenCalledWith(personCollection, ...additionalPeople);
        expect(comp.peopleSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const contract: IContract = { id: 456 };
        const status: IItemCatalogue = { id: 6843 };
        contract.status = status;
        const contractor: IPerson = { id: 52471 };
        contract.contractor = contractor;

        activatedRoute.data = of({ contract });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(contract));
        expect(comp.itemCataloguesSharedCollection).toContain(status);
        expect(comp.peopleSharedCollection).toContain(contractor);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Contract>>();
        const contract = { id: 123 };
        jest.spyOn(contractService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ contract });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: contract }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(contractService.update).toHaveBeenCalledWith(contract);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Contract>>();
        const contract = new Contract();
        jest.spyOn(contractService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ contract });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: contract }));
        saveSubject.complete();

        // THEN
        expect(contractService.create).toHaveBeenCalledWith(contract);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Contract>>();
        const contract = { id: 123 };
        jest.spyOn(contractService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ contract });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(contractService.update).toHaveBeenCalledWith(contract);
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

      describe('trackPersonById', () => {
        it('Should return tracked Person primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPersonById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
