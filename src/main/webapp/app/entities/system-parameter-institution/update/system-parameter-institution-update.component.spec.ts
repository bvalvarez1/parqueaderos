jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SystemParameterInstitutionService } from '../service/system-parameter-institution.service';
import { ISystemParameterInstitution, SystemParameterInstitution } from '../system-parameter-institution.model';
import { ISystemParameters } from 'app/entities/system-parameters/system-parameters.model';
import { SystemParametersService } from 'app/entities/system-parameters/service/system-parameters.service';
import { IInstitution } from 'app/entities/institution/institution.model';
import { InstitutionService } from 'app/entities/institution/service/institution.service';

import { SystemParameterInstitutionUpdateComponent } from './system-parameter-institution-update.component';

describe('Component Tests', () => {
  describe('SystemParameterInstitution Management Update Component', () => {
    let comp: SystemParameterInstitutionUpdateComponent;
    let fixture: ComponentFixture<SystemParameterInstitutionUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let systemParameterInstitutionService: SystemParameterInstitutionService;
    let systemParametersService: SystemParametersService;
    let institutionService: InstitutionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SystemParameterInstitutionUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(SystemParameterInstitutionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SystemParameterInstitutionUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      systemParameterInstitutionService = TestBed.inject(SystemParameterInstitutionService);
      systemParametersService = TestBed.inject(SystemParametersService);
      institutionService = TestBed.inject(InstitutionService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call SystemParameters query and add missing value', () => {
        const systemParameterInstitution: ISystemParameterInstitution = { id: 456 };
        const parameter: ISystemParameters = { id: 429 };
        systemParameterInstitution.parameter = parameter;

        const systemParametersCollection: ISystemParameters[] = [{ id: 27675 }];
        jest.spyOn(systemParametersService, 'query').mockReturnValue(of(new HttpResponse({ body: systemParametersCollection })));
        const additionalSystemParameters = [parameter];
        const expectedCollection: ISystemParameters[] = [...additionalSystemParameters, ...systemParametersCollection];
        jest.spyOn(systemParametersService, 'addSystemParametersToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ systemParameterInstitution });
        comp.ngOnInit();

        expect(systemParametersService.query).toHaveBeenCalled();
        expect(systemParametersService.addSystemParametersToCollectionIfMissing).toHaveBeenCalledWith(
          systemParametersCollection,
          ...additionalSystemParameters
        );
        expect(comp.systemParametersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Institution query and add missing value', () => {
        const systemParameterInstitution: ISystemParameterInstitution = { id: 456 };
        const institution: IInstitution = { id: 11177 };
        systemParameterInstitution.institution = institution;

        const institutionCollection: IInstitution[] = [{ id: 93412 }];
        jest.spyOn(institutionService, 'query').mockReturnValue(of(new HttpResponse({ body: institutionCollection })));
        const additionalInstitutions = [institution];
        const expectedCollection: IInstitution[] = [...additionalInstitutions, ...institutionCollection];
        jest.spyOn(institutionService, 'addInstitutionToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ systemParameterInstitution });
        comp.ngOnInit();

        expect(institutionService.query).toHaveBeenCalled();
        expect(institutionService.addInstitutionToCollectionIfMissing).toHaveBeenCalledWith(
          institutionCollection,
          ...additionalInstitutions
        );
        expect(comp.institutionsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const systemParameterInstitution: ISystemParameterInstitution = { id: 456 };
        const parameter: ISystemParameters = { id: 4735 };
        systemParameterInstitution.parameter = parameter;
        const institution: IInstitution = { id: 88154 };
        systemParameterInstitution.institution = institution;

        activatedRoute.data = of({ systemParameterInstitution });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(systemParameterInstitution));
        expect(comp.systemParametersSharedCollection).toContain(parameter);
        expect(comp.institutionsSharedCollection).toContain(institution);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<SystemParameterInstitution>>();
        const systemParameterInstitution = { id: 123 };
        jest.spyOn(systemParameterInstitutionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ systemParameterInstitution });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: systemParameterInstitution }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(systemParameterInstitutionService.update).toHaveBeenCalledWith(systemParameterInstitution);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<SystemParameterInstitution>>();
        const systemParameterInstitution = new SystemParameterInstitution();
        jest.spyOn(systemParameterInstitutionService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ systemParameterInstitution });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: systemParameterInstitution }));
        saveSubject.complete();

        // THEN
        expect(systemParameterInstitutionService.create).toHaveBeenCalledWith(systemParameterInstitution);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<SystemParameterInstitution>>();
        const systemParameterInstitution = { id: 123 };
        jest.spyOn(systemParameterInstitutionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ systemParameterInstitution });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(systemParameterInstitutionService.update).toHaveBeenCalledWith(systemParameterInstitution);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackSystemParametersById', () => {
        it('Should return tracked SystemParameters primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackSystemParametersById(0, entity);
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
