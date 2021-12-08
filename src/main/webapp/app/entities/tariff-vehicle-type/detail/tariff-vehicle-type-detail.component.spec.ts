import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TariffVehicleTypeDetailComponent } from './tariff-vehicle-type-detail.component';

describe('Component Tests', () => {
  describe('TariffVehicleType Management Detail Component', () => {
    let comp: TariffVehicleTypeDetailComponent;
    let fixture: ComponentFixture<TariffVehicleTypeDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [TariffVehicleTypeDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ tariffVehicleType: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(TariffVehicleTypeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TariffVehicleTypeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load tariffVehicleType on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.tariffVehicleType).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
