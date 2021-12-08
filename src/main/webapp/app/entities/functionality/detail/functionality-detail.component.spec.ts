import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FunctionalityDetailComponent } from './functionality-detail.component';

describe('Component Tests', () => {
  describe('Functionality Management Detail Component', () => {
    let comp: FunctionalityDetailComponent;
    let fixture: ComponentFixture<FunctionalityDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [FunctionalityDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ functionality: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(FunctionalityDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FunctionalityDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load functionality on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.functionality).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
