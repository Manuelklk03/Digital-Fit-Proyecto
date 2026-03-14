import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Privados } from './privados';

describe('Privados', () => {
  let component: Privados;
  let fixture: ComponentFixture<Privados>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Privados],
    }).compileComponents();

    fixture = TestBed.createComponent(Privados);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
