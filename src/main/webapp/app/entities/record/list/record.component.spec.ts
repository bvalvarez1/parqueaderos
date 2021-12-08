import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { RecordService } from '../service/record.service';

import { RecordComponent } from './record.component';

describe('Component Tests', () => {
  describe('Record Management Component', () => {
    let comp: RecordComponent;
    let fixture: ComponentFixture<RecordComponent>;
    let service: RecordService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [RecordComponent],
      })
        .overrideTemplate(RecordComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RecordComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(RecordService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.records?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
