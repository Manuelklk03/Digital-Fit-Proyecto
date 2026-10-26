import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EntrenamientosComunidad } from './entrenamientos-comunidad/entrenamientos-comunidad';

describe('EntrenamientosComunidad', () => {
  let component: EntrenamientosComunidad;
  let fixture: ComponentFixture<EntrenamientosComunidad>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EntrenamientosComunidad],
    }).compileComponents();

    fixture = TestBed.createComponent(EntrenamientosComunidad);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
