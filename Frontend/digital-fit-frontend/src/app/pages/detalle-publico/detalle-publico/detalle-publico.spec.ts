import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetallePublico } from './detalle-publico';

describe('DetallePublico', () => {
  let component: DetallePublico;
  let fixture: ComponentFixture<DetallePublico>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetallePublico],
    }).compileComponents();

    fixture = TestBed.createComponent(DetallePublico);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
