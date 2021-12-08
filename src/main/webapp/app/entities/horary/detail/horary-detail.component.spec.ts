import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HoraryDetailComponent } from './horary-detail.component';

describe('Component Tests', () => {
  describe('Horary Management Detail Component', () => {
    let comp: HoraryDetailComponent;
    let fixture: ComponentFixture<HoraryDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [HoraryDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ horary: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(HoraryDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(HoraryDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load horary on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.horary).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
