import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SystemParametersDetailComponent } from './system-parameters-detail.component';

describe('Component Tests', () => {
  describe('SystemParameters Management Detail Component', () => {
    let comp: SystemParametersDetailComponent;
    let fixture: ComponentFixture<SystemParametersDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [SystemParametersDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ systemParameters: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(SystemParametersDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SystemParametersDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load systemParameters on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.systemParameters).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
