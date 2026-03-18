import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetallePrivado } from './detalle-privado';

describe('DetallePrivado', () => {
  let component: DetallePrivado;
  let fixture: ComponentFixture<DetallePrivado>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetallePrivado],
    }).compileComponents();

    fixture = TestBed.createComponent(DetallePrivado);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
