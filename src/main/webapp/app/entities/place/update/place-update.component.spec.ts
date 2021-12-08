jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PlaceService } from '../service/place.service';
import { IPlace, Place } from '../place.model';
import { IItemCatalogue } from 'app/entities/item-catalogue/item-catalogue.model';
import { ItemCatalogueService } from 'app/entities/item-catalogue/service/item-catalogue.service';
import { IInstitution } from 'app/entities/institution/institution.model';
import { InstitutionService } from 'app/entities/institution/service/institution.service';

import { PlaceUpdateComponent } from './place-update.component';

describe('Component Tests', () => {
  describe('Place Management Update Component', () => {
    let comp: PlaceUpdateComponent;
    let fixture: ComponentFixture<PlaceUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let placeService: PlaceService;
    let itemCatalogueService: ItemCatalogueService;
    let institutionService: InstitutionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PlaceUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PlaceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PlaceUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      placeService = TestBed.inject(PlaceService);
      itemCatalogueService = TestBed.inject(ItemCatalogueService);
      institutionService = TestBed.inject(InstitutionService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ItemCatalogue query and add missing value', () => {
        const place: IPlace = { id: 456 };
        const status: IItemCatalogue = { id: 7284 };
        place.status = status;

        const itemCatalogueCollection: IItemCatalogue[] = [{ id: 88918 }];
        jest.spyOn(itemCatalogueService, 'query').mockReturnValue(of(new HttpResponse({ body: itemCatalogueCollection })));
        const additionalItemCatalogues = [status];
        const expectedCollection: IItemCatalogue[] = [...additionalItemCatalogues, ...itemCatalogueCollection];
        jest.spyOn(itemCatalogueService, 'addItemCatalogueToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ place });
        comp.ngOnInit();

        expect(itemCatalogueService.query).toHaveBeenCalled();
        expect(itemCatalogueService.addItemCatalogueToCollectionIfMissing).toHaveBeenCalledWith(
          itemCatalogueCollection,
          ...additionalItemCatalogues
        );
        expect(comp.itemCataloguesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Institution query and add missing value', () => {
        const place: IPlace = { id: 456 };
        const institution: IInstitution = { id: 22112 };
        place.institution = institution;

        const institutionCollection: IInstitution[] = [{ id: 85430 }];
        jest.spyOn(institutionService, 'query').mockReturnValue(of(new HttpResponse({ body: institutionCollection })));
        const additionalInstitutions = [institution];
        const expectedCollection: IInstitution[] = [...additionalInstitutions, ...institutionCollection];
        jest.spyOn(institutionService, 'addInstitutionToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ place });
        comp.ngOnInit();

        expect(institutionService.query).toHaveBeenCalled();
        expect(institutionService.addInstitutionToCollectionIfMissing).toHaveBeenCalledWith(
          institutionCollection,
          ...additionalInstitutions
        );
        expect(comp.institutionsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const place: IPlace = { id: 456 };
        const status: IItemCatalogue = { id: 96269 };
        place.status = status;
        const institution: IInstitution = { id: 83336 };
        place.institution = institution;

        activatedRoute.data = of({ place });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(place));
        expect(comp.itemCataloguesSharedCollection).toContain(status);
        expect(comp.institutionsSharedCollection).toContain(institution);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Place>>();
        const place = { id: 123 };
        jest.spyOn(placeService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ place });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: place }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(placeService.update).toHaveBeenCalledWith(place);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Place>>();
        const place = new Place();
        jest.spyOn(placeService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ place });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: place }));
        saveSubject.complete();

        // THEN
        expect(placeService.create).toHaveBeenCalledWith(place);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Place>>();
        const place = { id: 123 };
        jest.spyOn(placeService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ place });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(placeService.update).toHaveBeenCalledWith(place);
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
