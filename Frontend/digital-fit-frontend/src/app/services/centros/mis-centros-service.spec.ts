import { TestBed } from '@angular/core/testing';

import { MisCentrosService } from './mis-centros-service';

describe('MisCentrosService', () => {
  let service: MisCentrosService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MisCentrosService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
