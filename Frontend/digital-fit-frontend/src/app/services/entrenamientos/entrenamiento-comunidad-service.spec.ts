import { TestBed } from '@angular/core/testing';

import { EntrenamientoComunidadService } from './entrenamiento-comunidad-service';

describe('EntrenamientoComunidadService', () => {
  let service: EntrenamientoComunidadService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EntrenamientoComunidadService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
