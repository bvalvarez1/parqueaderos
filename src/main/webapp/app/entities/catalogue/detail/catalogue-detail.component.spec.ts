import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CatalogueDetailComponent } from './catalogue-detail.component';

describe('Component Tests', () => {
  describe('Catalogue Management Detail Component', () => {
    let comp: CatalogueDetailComponent;
    let fixture: ComponentFixture<CatalogueDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CatalogueDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ catalogue: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CatalogueDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CatalogueDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load catalogue on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.catalogue).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
