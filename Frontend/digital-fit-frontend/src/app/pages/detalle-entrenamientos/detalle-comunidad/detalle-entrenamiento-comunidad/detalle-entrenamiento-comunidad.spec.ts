import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetalleEntrenamientoComunidad } from './detalle-entrenamiento-comunidad';

describe('DetalleEntrenamientoComunidad', () => {
  let component: DetalleEntrenamientoComunidad;
  let fixture: ComponentFixture<DetalleEntrenamientoComunidad>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetalleEntrenamientoComunidad],
    }).compileComponents();

    fixture = TestBed.createComponent(DetalleEntrenamientoComunidad);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
