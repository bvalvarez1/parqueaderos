import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UserAuthorityInstitutionDetailComponent } from './user-authority-institution-detail.component';

describe('Component Tests', () => {
  describe('UserAuthorityInstitution Management Detail Component', () => {
    let comp: UserAuthorityInstitutionDetailComponent;
    let fixture: ComponentFixture<UserAuthorityInstitutionDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [UserAuthorityInstitutionDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ userAuthorityInstitution: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(UserAuthorityInstitutionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(UserAuthorityInstitutionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load userAuthorityInstitution on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.userAuthorityInstitution).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
