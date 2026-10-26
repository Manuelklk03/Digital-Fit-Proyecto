import { TestBed } from '@angular/core/testing';

import { AdminLugarespublicosBase } from './admin-lugarespublicos-base';

describe('AdminLugarespublicosBase', () => {
  let service: AdminLugarespublicosBase;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AdminLugarespublicosBase);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
