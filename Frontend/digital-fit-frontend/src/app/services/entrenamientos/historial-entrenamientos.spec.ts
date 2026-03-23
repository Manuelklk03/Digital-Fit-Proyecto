import { TestBed } from '@angular/core/testing';

import { HistorialEntrenamientos } from './historial-entrenamientos';

describe('HistorialEntrenamientos', () => {
  let service: HistorialEntrenamientos;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HistorialEntrenamientos);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
