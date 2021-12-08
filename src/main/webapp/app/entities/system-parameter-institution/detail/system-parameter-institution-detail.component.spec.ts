import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SystemParameterInstitutionDetailComponent } from './system-parameter-institution-detail.component';

describe('Component Tests', () => {
  describe('SystemParameterInstitution Management Detail Component', () => {
    let comp: SystemParameterInstitutionDetailComponent;
    let fixture: ComponentFixture<SystemParameterInstitutionDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [SystemParameterInstitutionDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ systemParameterInstitution: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(SystemParameterInstitutionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SystemParameterInstitutionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load systemParameterInstitution on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.systemParameterInstitution).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
