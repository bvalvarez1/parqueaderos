jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SystemParametersService } from '../service/system-parameters.service';
import { ISystemParameters, SystemParameters } from '../system-parameters.model';

import { SystemParametersUpdateComponent } from './system-parameters-update.component';

describe('Component Tests', () => {
  describe('SystemParameters Management Update Component', () => {
    let comp: SystemParametersUpdateComponent;
    let fixture: ComponentFixture<SystemParametersUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let systemParametersService: SystemParametersService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SystemParametersUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(SystemParametersUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SystemParametersUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      systemParametersService = TestBed.inject(SystemParametersService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const systemParameters: ISystemParameters = { id: 456 };

        activatedRoute.data = of({ systemParameters });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(systemParameters));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<SystemParameters>>();
        const systemParameters = { id: 123 };
        jest.spyOn(systemParametersService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ systemParameters });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: systemParameters }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(systemParametersService.update).toHaveBeenCalledWith(systemParameters);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<SystemParameters>>();
        const systemParameters = new SystemParameters();
        jest.spyOn(systemParametersService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ systemParameters });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: systemParameters }));
        saveSubject.complete();

        // THEN
        expect(systemParametersService.create).toHaveBeenCalledWith(systemParameters);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<SystemParameters>>();
        const systemParameters = { id: 123 };
        jest.spyOn(systemParametersService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ systemParameters });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(systemParametersService.update).toHaveBeenCalledWith(systemParameters);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
