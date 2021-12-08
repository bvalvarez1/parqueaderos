jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { RecordService } from '../service/record.service';
import { IRecord, Record } from '../record.model';

import { RecordUpdateComponent } from './record-update.component';

describe('Component Tests', () => {
  describe('Record Management Update Component', () => {
    let comp: RecordUpdateComponent;
    let fixture: ComponentFixture<RecordUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let recordService: RecordService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [RecordUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(RecordUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RecordUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      recordService = TestBed.inject(RecordService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const record: IRecord = { id: 456 };

        activatedRoute.data = of({ record });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(record));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Record>>();
        const record = { id: 123 };
        jest.spyOn(recordService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ record });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: record }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(recordService.update).toHaveBeenCalledWith(record);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Record>>();
        const record = new Record();
        jest.spyOn(recordService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ record });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: record }));
        saveSubject.complete();

        // THEN
        expect(recordService.create).toHaveBeenCalledWith(record);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Record>>();
        const record = { id: 123 };
        jest.spyOn(recordService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ record });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(recordService.update).toHaveBeenCalledWith(record);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
