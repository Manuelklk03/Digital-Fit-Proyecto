import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MisEntrenamientos } from './mis-entrenamientos';

describe('MisEntrenamientos', () => {
  let component: MisEntrenamientos;
  let fixture: ComponentFixture<MisEntrenamientos>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MisEntrenamientos],
    }).compileComponents();

    fixture = TestBed.createComponent(MisEntrenamientos);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
