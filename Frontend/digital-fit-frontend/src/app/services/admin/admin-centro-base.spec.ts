import { TestBed } from '@angular/core/testing';

import { AdminCentroBase } from './admin-centro-base';

describe('AdminCentroBase', () => {
  let service: AdminCentroBase;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AdminCentroBase);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
