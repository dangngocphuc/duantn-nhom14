import { TestBed } from '@angular/core/testing';

import { UserDetailResolveService } from './user-detail-resolve.service';

describe('UserDetailResolveService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: UserDetailResolveService = TestBed.get(UserDetailResolveService);
    expect(service).toBeTruthy();
  });
});
