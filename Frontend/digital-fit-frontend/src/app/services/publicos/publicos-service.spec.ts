import { TestBed } from '@angular/core/testing';

import { PublicosService } from './publicos-service';

describe('PublicosService', () => {
  let service: PublicosService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PublicosService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
