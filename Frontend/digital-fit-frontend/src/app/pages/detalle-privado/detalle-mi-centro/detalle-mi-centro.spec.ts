import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetalleMiCentro } from './detalle-mi-centro';

describe('DetalleMiCentro', () => {
  let component: DetalleMiCentro;
  let fixture: ComponentFixture<DetalleMiCentro>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetalleMiCentro],
    }).compileComponents();

    fixture = TestBed.createComponent(DetalleMiCentro);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
