import { TestBed } from '@angular/core/testing';

import { MisLugares } from './mis-lugares';

describe('MisLugares', () => {
  let service: MisLugares;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MisLugares);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
