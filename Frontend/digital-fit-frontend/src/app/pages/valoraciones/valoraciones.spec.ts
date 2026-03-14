import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ValoracionesComponent } from './valoraciones';

describe('Valoraciones', () => {
  let component: ValoracionesComponent;
  let fixture: ComponentFixture<ValoracionesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ValoracionesComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ValoracionesComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
