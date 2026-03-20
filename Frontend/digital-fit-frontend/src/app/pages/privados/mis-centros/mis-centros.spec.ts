import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MisCentros } from './mis-centros';

describe('MisCentros', () => {
  let component: MisCentros;
  let fixture: ComponentFixture<MisCentros>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MisCentros],
    }).compileComponents();

    fixture = TestBed.createComponent(MisCentros);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
