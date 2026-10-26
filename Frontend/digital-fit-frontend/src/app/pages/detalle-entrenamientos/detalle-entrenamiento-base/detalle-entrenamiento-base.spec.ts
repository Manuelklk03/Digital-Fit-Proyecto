import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetalleEntrenamientoBase } from './detalle-entrenamiento-base';

describe('DetalleEntrenamientoBase', () => {
  let component: DetalleEntrenamientoBase;
  let fixture: ComponentFixture<DetalleEntrenamientoBase>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetalleEntrenamientoBase],
    }).compileComponents();

    fixture = TestBed.createComponent(DetalleEntrenamientoBase);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
