import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MisLugares } from './mis-lugares';

describe('MisLugares', () => {
  let component: MisLugares;
  let fixture: ComponentFixture<MisLugares>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MisLugares],
    }).compileComponents();

    fixture = TestBed.createComponent(MisLugares);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
