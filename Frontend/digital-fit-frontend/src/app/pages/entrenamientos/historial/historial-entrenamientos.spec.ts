import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HistorialEntrenamientosComponent } from './historial-entrenamientos';

describe('HistorialEntrenamientos', () => {
  let component: HistorialEntrenamientosComponent;
  let fixture: ComponentFixture<HistorialEntrenamientosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HistorialEntrenamientosComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(HistorialEntrenamientosComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
