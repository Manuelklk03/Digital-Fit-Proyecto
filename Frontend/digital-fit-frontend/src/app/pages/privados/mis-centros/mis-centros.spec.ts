import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MisCentrosComponent } from './mis-centros';

describe('MisCentros', () => {
  let component: MisCentrosComponent;
  let fixture: ComponentFixture<MisCentrosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MisCentrosComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(MisCentrosComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
