import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminDetalleTicket } from './admin-detalle-ticket';

describe('AdminDetalleTicket', () => {
  let component: AdminDetalleTicket;
  let fixture: ComponentFixture<AdminDetalleTicket>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminDetalleTicket],
    }).compileComponents();

    fixture = TestBed.createComponent(AdminDetalleTicket);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
