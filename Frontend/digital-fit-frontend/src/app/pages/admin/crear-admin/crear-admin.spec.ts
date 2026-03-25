import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CrearAdmin } from './crear-admin';

describe('CrearAdmin', () => {
  let component: CrearAdmin;
  let fixture: ComponentFixture<CrearAdmin>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CrearAdmin],
    }).compileComponents();

    fixture = TestBed.createComponent(CrearAdmin);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
