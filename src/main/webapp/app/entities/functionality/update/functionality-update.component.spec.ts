jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FunctionalityService } from '../service/functionality.service';
import { IFunctionality, Functionality } from '../functionality.model';

import { FunctionalityUpdateComponent } from './functionality-update.component';

describe('Component Tests', () => {
  describe('Functionality Management Update Component', () => {
    let comp: FunctionalityUpdateComponent;
    let fixture: ComponentFixture<FunctionalityUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let functionalityService: FunctionalityService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FunctionalityUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FunctionalityUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FunctionalityUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      functionalityService = TestBed.inject(FunctionalityService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Functionality query and add missing value', () => {
        const functionality: IFunctionality = { id: 456 };
        const parent: IFunctionality = { id: 29344 };
        functionality.parent = parent;

        const functionalityCollection: IFunctionality[] = [{ id: 54992 }];
        jest.spyOn(functionalityService, 'query').mockReturnValue(of(new HttpResponse({ body: functionalityCollection })));
        const additionalFunctionalities = [parent];
        const expectedCollection: IFunctionality[] = [...additionalFunctionalities, ...functionalityCollection];
        jest.spyOn(functionalityService, 'addFunctionalityToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ functionality });
        comp.ngOnInit();

        expect(functionalityService.query).toHaveBeenCalled();
        expect(functionalityService.addFunctionalityToCollectionIfMissing).toHaveBeenCalledWith(
          functionalityCollection,
          ...additionalFunctionalities
        );
        expect(comp.functionalitiesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const functionality: IFunctionality = { id: 456 };
        const parent: IFunctionality = { id: 95602 };
        functionality.parent = parent;

        activatedRoute.data = of({ functionality });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(functionality));
        expect(comp.functionalitiesSharedCollection).toContain(parent);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Functionality>>();
        const functionality = { id: 123 };
        jest.spyOn(functionalityService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ functionality });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: functionality }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(functionalityService.update).toHaveBeenCalledWith(functionality);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Functionality>>();
        const functionality = new Functionality();
        jest.spyOn(functionalityService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ functionality });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: functionality }));
        saveSubject.complete();

        // THEN
        expect(functionalityService.create).toHaveBeenCalledWith(functionality);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Functionality>>();
        const functionality = { id: 123 };
        jest.spyOn(functionalityService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ functionality });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(functionalityService.update).toHaveBeenCalledWith(functionality);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackFunctionalityById', () => {
        it('Should return tracked Functionality primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackFunctionalityById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
