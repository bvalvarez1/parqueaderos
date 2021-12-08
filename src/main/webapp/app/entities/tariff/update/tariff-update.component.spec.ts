jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TariffService } from '../service/tariff.service';
import { ITariff, Tariff } from '../tariff.model';
import { IInstitution } from 'app/entities/institution/institution.model';
import { InstitutionService } from 'app/entities/institution/service/institution.service';

import { TariffUpdateComponent } from './tariff-update.component';

describe('Component Tests', () => {
  describe('Tariff Management Update Component', () => {
    let comp: TariffUpdateComponent;
    let fixture: ComponentFixture<TariffUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let tariffService: TariffService;
    let institutionService: InstitutionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TariffUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TariffUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TariffUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      tariffService = TestBed.inject(TariffService);
      institutionService = TestBed.inject(InstitutionService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Institution query and add missing value', () => {
        const tariff: ITariff = { id: 456 };
        const institution: IInstitution = { id: 71371 };
        tariff.institution = institution;

        const institutionCollection: IInstitution[] = [{ id: 73481 }];
        jest.spyOn(institutionService, 'query').mockReturnValue(of(new HttpResponse({ body: institutionCollection })));
        const additionalInstitutions = [institution];
        const expectedCollection: IInstitution[] = [...additionalInstitutions, ...institutionCollection];
        jest.spyOn(institutionService, 'addInstitutionToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ tariff });
        comp.ngOnInit();

        expect(institutionService.query).toHaveBeenCalled();
        expect(institutionService.addInstitutionToCollectionIfMissing).toHaveBeenCalledWith(
          institutionCollection,
          ...additionalInstitutions
        );
        expect(comp.institutionsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const tariff: ITariff = { id: 456 };
        const institution: IInstitution = { id: 80202 };
        tariff.institution = institution;

        activatedRoute.data = of({ tariff });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(tariff));
        expect(comp.institutionsSharedCollection).toContain(institution);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Tariff>>();
        const tariff = { id: 123 };
        jest.spyOn(tariffService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ tariff });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tariff }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(tariffService.update).toHaveBeenCalledWith(tariff);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Tariff>>();
        const tariff = new Tariff();
        jest.spyOn(tariffService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ tariff });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tariff }));
        saveSubject.complete();

        // THEN
        expect(tariffService.create).toHaveBeenCalledWith(tariff);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Tariff>>();
        const tariff = { id: 123 };
        jest.spyOn(tariffService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ tariff });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(tariffService.update).toHaveBeenCalledWith(tariff);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
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
