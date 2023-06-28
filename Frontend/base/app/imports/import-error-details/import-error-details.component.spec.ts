import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ImportErrorDetailsComponent } from './import-error-details.component';

describe('ImportErrorDetailsComponent', () => {
  let component: ImportErrorDetailsComponent;
  let fixture: ComponentFixture<ImportErrorDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ImportErrorDetailsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ImportErrorDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
