import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetalleMiLugar } from './detalle-mi-lugar';

describe('DetalleMiLugar', () => {
  let component: DetalleMiLugar;
  let fixture: ComponentFixture<DetalleMiLugar>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetalleMiLugar],
    }).compileComponents();

    fixture = TestBed.createComponent(DetalleMiLugar);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
