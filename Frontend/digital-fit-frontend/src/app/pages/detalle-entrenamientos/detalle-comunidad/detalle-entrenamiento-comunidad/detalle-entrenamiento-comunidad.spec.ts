import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetalleEntrenamientoComunidadComponent } from './detalle-entrenamiento-comunidad';

describe('DetalleEntrenamientoComunidad', () => {
  let component: DetalleEntrenamientoComunidadComponent;
  let fixture: ComponentFixture<DetalleEntrenamientoComunidadComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetalleEntrenamientoComunidadComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(DetalleEntrenamientoComunidadComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
