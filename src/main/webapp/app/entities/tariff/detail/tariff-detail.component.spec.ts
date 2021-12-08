import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TariffDetailComponent } from './tariff-detail.component';

describe('Component Tests', () => {
  describe('Tariff Management Detail Component', () => {
    let comp: TariffDetailComponent;
    let fixture: ComponentFixture<TariffDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [TariffDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ tariff: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(TariffDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TariffDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load tariff on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.tariff).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
