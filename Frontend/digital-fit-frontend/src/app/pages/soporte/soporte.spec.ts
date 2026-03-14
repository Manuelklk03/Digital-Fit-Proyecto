import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SoporteComponent } from './soporte';

describe('Soporte', () => {
  let component: SoporteComponent;
  let fixture: ComponentFixture<SoporteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SoporteComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(SoporteComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
