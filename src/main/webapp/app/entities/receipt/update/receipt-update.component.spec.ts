jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ReceiptService } from '../service/receipt.service';
import { IReceipt, Receipt } from '../receipt.model';
import { IRecordTicket } from 'app/entities/record-ticket/record-ticket.model';
import { RecordTicketService } from 'app/entities/record-ticket/service/record-ticket.service';

import { ReceiptUpdateComponent } from './receipt-update.component';

describe('Component Tests', () => {
  describe('Receipt Management Update Component', () => {
    let comp: ReceiptUpdateComponent;
    let fixture: ComponentFixture<ReceiptUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let receiptService: ReceiptService;
    let recordTicketService: RecordTicketService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ReceiptUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ReceiptUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ReceiptUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      receiptService = TestBed.inject(ReceiptService);
      recordTicketService = TestBed.inject(RecordTicketService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call recordticketid query and add missing value', () => {
        const receipt: IReceipt = { id: 456 };
        const recordticketid: IRecordTicket = { id: 58781 };
        receipt.recordticketid = recordticketid;

        const recordticketidCollection: IRecordTicket[] = [{ id: 77598 }];
        jest.spyOn(recordTicketService, 'query').mockReturnValue(of(new HttpResponse({ body: recordticketidCollection })));
        const expectedCollection: IRecordTicket[] = [recordticketid, ...recordticketidCollection];
        jest.spyOn(recordTicketService, 'addRecordTicketToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ receipt });
        comp.ngOnInit();

        expect(recordTicketService.query).toHaveBeenCalled();
        expect(recordTicketService.addRecordTicketToCollectionIfMissing).toHaveBeenCalledWith(recordticketidCollection, recordticketid);
        expect(comp.recordticketidsCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const receipt: IReceipt = { id: 456 };
        const recordticketid: IRecordTicket = { id: 83912 };
        receipt.recordticketid = recordticketid;

        activatedRoute.data = of({ receipt });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(receipt));
        expect(comp.recordticketidsCollection).toContain(recordticketid);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Receipt>>();
        const receipt = { id: 123 };
        jest.spyOn(receiptService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ receipt });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: receipt }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(receiptService.update).toHaveBeenCalledWith(receipt);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Receipt>>();
        const receipt = new Receipt();
        jest.spyOn(receiptService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ receipt });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: receipt }));
        saveSubject.complete();

        // THEN
        expect(receiptService.create).toHaveBeenCalledWith(receipt);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Receipt>>();
        const receipt = { id: 123 };
        jest.spyOn(receiptService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ receipt });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(receiptService.update).toHaveBeenCalledWith(receipt);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackRecordTicketById', () => {
        it('Should return tracked RecordTicket primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackRecordTicketById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
