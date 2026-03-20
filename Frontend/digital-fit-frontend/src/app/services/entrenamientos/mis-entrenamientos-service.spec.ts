import { TestBed } from '@angular/core/testing';

import { MisEntrenamientosService } from './mis-entrenamientos-service';

describe('MisEntrenamientosService', () => {
  let service: MisEntrenamientosService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MisEntrenamientosService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
