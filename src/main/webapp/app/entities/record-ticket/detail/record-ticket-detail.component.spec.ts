import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RecordTicketDetailComponent } from './record-ticket-detail.component';

describe('Component Tests', () => {
  describe('RecordTicket Management Detail Component', () => {
    let comp: RecordTicketDetailComponent;
    let fixture: ComponentFixture<RecordTicketDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [RecordTicketDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ recordTicket: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(RecordTicketDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RecordTicketDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load recordTicket on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.recordTicket).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
