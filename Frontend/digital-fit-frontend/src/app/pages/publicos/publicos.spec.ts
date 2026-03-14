import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Publicos } from './publicos';

describe('Publicos', () => {
  let component: Publicos;
  let fixture: ComponentFixture<Publicos>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Publicos],
    }).compileComponents();

    fixture = TestBed.createComponent(Publicos);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
