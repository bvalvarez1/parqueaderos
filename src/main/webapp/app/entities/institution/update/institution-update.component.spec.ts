jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { InstitutionService } from '../service/institution.service';
import { IInstitution, Institution } from '../institution.model';
import { IItemCatalogue } from 'app/entities/item-catalogue/item-catalogue.model';
import { ItemCatalogueService } from 'app/entities/item-catalogue/service/item-catalogue.service';

import { InstitutionUpdateComponent } from './institution-update.component';

describe('Component Tests', () => {
  describe('Institution Management Update Component', () => {
    let comp: InstitutionUpdateComponent;
    let fixture: ComponentFixture<InstitutionUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let institutionService: InstitutionService;
    let itemCatalogueService: ItemCatalogueService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [InstitutionUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(InstitutionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(InstitutionUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      institutionService = TestBed.inject(InstitutionService);
      itemCatalogueService = TestBed.inject(ItemCatalogueService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ItemCatalogue query and add missing value', () => {
        const institution: IInstitution = { id: 456 };
        const canton: IItemCatalogue = { id: 87403 };
        institution.canton = canton;

        const itemCatalogueCollection: IItemCatalogue[] = [{ id: 70749 }];
        jest.spyOn(itemCatalogueService, 'query').mockReturnValue(of(new HttpResponse({ body: itemCatalogueCollection })));
        const additionalItemCatalogues = [canton];
        const expectedCollection: IItemCatalogue[] = [...additionalItemCatalogues, ...itemCatalogueCollection];
        jest.spyOn(itemCatalogueService, 'addItemCatalogueToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ institution });
        comp.ngOnInit();

        expect(itemCatalogueService.query).toHaveBeenCalled();
        expect(itemCatalogueService.addItemCatalogueToCollectionIfMissing).toHaveBeenCalledWith(
          itemCatalogueCollection,
          ...additionalItemCatalogues
        );
        expect(comp.itemCataloguesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const institution: IInstitution = { id: 456 };
        const canton: IItemCatalogue = { id: 79523 };
        institution.canton = canton;

        activatedRoute.data = of({ institution });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(institution));
        expect(comp.itemCataloguesSharedCollection).toContain(canton);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Institution>>();
        const institution = { id: 123 };
        jest.spyOn(institutionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ institution });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: institution }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(institutionService.update).toHaveBeenCalledWith(institution);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Institution>>();
        const institution = new Institution();
        jest.spyOn(institutionService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ institution });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: institution }));
        saveSubject.complete();

        // THEN
        expect(institutionService.create).toHaveBeenCalledWith(institution);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Institution>>();
        const institution = { id: 123 };
        jest.spyOn(institutionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ institution });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(institutionService.update).toHaveBeenCalledWith(institution);
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
    });
  });
});
