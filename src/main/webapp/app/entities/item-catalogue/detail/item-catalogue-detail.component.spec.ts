import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ItemCatalogueDetailComponent } from './item-catalogue-detail.component';

describe('Component Tests', () => {
  describe('ItemCatalogue Management Detail Component', () => {
    let comp: ItemCatalogueDetailComponent;
    let fixture: ComponentFixture<ItemCatalogueDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ItemCatalogueDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ itemCatalogue: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ItemCatalogueDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ItemCatalogueDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load itemCatalogue on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.itemCatalogue).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
