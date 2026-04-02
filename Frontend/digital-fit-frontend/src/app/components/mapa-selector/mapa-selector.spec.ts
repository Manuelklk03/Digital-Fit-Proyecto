import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MapaSelectorComponent } from './mapa-selector';

describe('MapaSelector', () => {
  let component: MapaSelectorComponent;
  let fixture: ComponentFixture<MapaSelectorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MapaSelectorComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(MapaSelectorComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
