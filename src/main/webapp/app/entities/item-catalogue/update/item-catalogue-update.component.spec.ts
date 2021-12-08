jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ItemCatalogueService } from '../service/item-catalogue.service';
import { IItemCatalogue, ItemCatalogue } from '../item-catalogue.model';
import { ICatalogue } from 'app/entities/catalogue/catalogue.model';
import { CatalogueService } from 'app/entities/catalogue/service/catalogue.service';

import { ItemCatalogueUpdateComponent } from './item-catalogue-update.component';

describe('Component Tests', () => {
  describe('ItemCatalogue Management Update Component', () => {
    let comp: ItemCatalogueUpdateComponent;
    let fixture: ComponentFixture<ItemCatalogueUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let itemCatalogueService: ItemCatalogueService;
    let catalogueService: CatalogueService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ItemCatalogueUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ItemCatalogueUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ItemCatalogueUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      itemCatalogueService = TestBed.inject(ItemCatalogueService);
      catalogueService = TestBed.inject(CatalogueService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Catalogue query and add missing value', () => {
        const itemCatalogue: IItemCatalogue = { id: 456 };
        const catalogue: ICatalogue = { id: 20238 };
        itemCatalogue.catalogue = catalogue;

        const catalogueCollection: ICatalogue[] = [{ id: 9808 }];
        jest.spyOn(catalogueService, 'query').mockReturnValue(of(new HttpResponse({ body: catalogueCollection })));
        const additionalCatalogues = [catalogue];
        const expectedCollection: ICatalogue[] = [...additionalCatalogues, ...catalogueCollection];
        jest.spyOn(catalogueService, 'addCatalogueToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ itemCatalogue });
        comp.ngOnInit();

        expect(catalogueService.query).toHaveBeenCalled();
        expect(catalogueService.addCatalogueToCollectionIfMissing).toHaveBeenCalledWith(catalogueCollection, ...additionalCatalogues);
        expect(comp.cataloguesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const itemCatalogue: IItemCatalogue = { id: 456 };
        const catalogue: ICatalogue = { id: 99452 };
        itemCatalogue.catalogue = catalogue;

        activatedRoute.data = of({ itemCatalogue });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(itemCatalogue));
        expect(comp.cataloguesSharedCollection).toContain(catalogue);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ItemCatalogue>>();
        const itemCatalogue = { id: 123 };
        jest.spyOn(itemCatalogueService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ itemCatalogue });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: itemCatalogue }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(itemCatalogueService.update).toHaveBeenCalledWith(itemCatalogue);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ItemCatalogue>>();
        const itemCatalogue = new ItemCatalogue();
        jest.spyOn(itemCatalogueService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ itemCatalogue });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: itemCatalogue }));
        saveSubject.complete();

        // THEN
        expect(itemCatalogueService.create).toHaveBeenCalledWith(itemCatalogue);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ItemCatalogue>>();
        const itemCatalogue = { id: 123 };
        jest.spyOn(itemCatalogueService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ itemCatalogue });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(itemCatalogueService.update).toHaveBeenCalledWith(itemCatalogue);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCatalogueById', () => {
        it('Should return tracked Catalogue primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCatalogueById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
