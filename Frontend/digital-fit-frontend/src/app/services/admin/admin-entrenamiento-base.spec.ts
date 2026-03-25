import { TestBed } from '@angular/core/testing';

import { AdminEntrenamientoBase } from './admin-entrenamiento-base';

describe('AdminEntrenamientoBase', () => {
  let service: AdminEntrenamientoBase;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AdminEntrenamientoBase);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
