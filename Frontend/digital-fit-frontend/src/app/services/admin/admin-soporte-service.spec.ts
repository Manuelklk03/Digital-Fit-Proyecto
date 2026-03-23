import { TestBed } from '@angular/core/testing';

import { AdminSoporteService } from './admin-soporte-service';

describe('AdminSoporteService', () => {
  let service: AdminSoporteService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AdminSoporteService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
