import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetalleMisEntrenamientos } from './detalle-mis-entrenamientos';

describe('DetalleMisEntrenamientos', () => {
  let component: DetalleMisEntrenamientos;
  let fixture: ComponentFixture<DetalleMisEntrenamientos>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetalleMisEntrenamientos],
    }).compileComponents();

    fixture = TestBed.createComponent(DetalleMisEntrenamientos);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
