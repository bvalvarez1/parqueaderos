import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AuthorityFunctionalityDetailComponent } from './authority-functionality-detail.component';

describe('Component Tests', () => {
  describe('AuthorityFunctionality Management Detail Component', () => {
    let comp: AuthorityFunctionalityDetailComponent;
    let fixture: ComponentFixture<AuthorityFunctionalityDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [AuthorityFunctionalityDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ authorityFunctionality: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(AuthorityFunctionalityDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AuthorityFunctionalityDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load authorityFunctionality on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.authorityFunctionality).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
