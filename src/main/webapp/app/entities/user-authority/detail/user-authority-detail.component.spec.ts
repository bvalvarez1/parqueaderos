import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UserAuthorityDetailComponent } from './user-authority-detail.component';

describe('Component Tests', () => {
  describe('UserAuthority Management Detail Component', () => {
    let comp: UserAuthorityDetailComponent;
    let fixture: ComponentFixture<UserAuthorityDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [UserAuthorityDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ userAuthority: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(UserAuthorityDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(UserAuthorityDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load userAuthority on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.userAuthority).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
