import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PrivadosComponent } from './privados';

describe('Privados', () => {
  let component: PrivadosComponent;
  let fixture: ComponentFixture<PrivadosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PrivadosComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(PrivadosComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
