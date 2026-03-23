import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetalleHistorial } from './detalle-historial';

describe('DetalleHistorial', () => {
  let component: DetalleHistorial;
  let fixture: ComponentFixture<DetalleHistorial>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetalleHistorial],
    }).compileComponents();

    fixture = TestBed.createComponent(DetalleHistorial);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
