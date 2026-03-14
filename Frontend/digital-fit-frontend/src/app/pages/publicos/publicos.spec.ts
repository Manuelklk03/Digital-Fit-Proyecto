import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PublicosComponent } from './publicos';

describe('Publicos', () => {
  let component: PublicosComponent;
  let fixture: ComponentFixture<PublicosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PublicosComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(PublicosComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
