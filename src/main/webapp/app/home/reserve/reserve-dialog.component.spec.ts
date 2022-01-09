jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ReserveService } from '../service/reserve.service';

import { TariffVehicleTypeDeleteDialogComponent } from './reserve-dialog.component';

describe('Component Tests', () => {
  describe('TariffVehicleType Management Delete Component', () => {
    let comp: TariffVehicleTypeDeleteDialogComponent;
    let fixture: ComponentFixture<TariffVehicleTypeDeleteDialogComponent>;
    let service: ReserveService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TariffVehicleTypeDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(TariffVehicleTypeDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TariffVehicleTypeDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ReserveService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({})));

          // WHEN
          comp.preReserve();
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        jest.spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
