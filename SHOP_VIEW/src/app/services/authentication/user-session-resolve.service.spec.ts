import { TestBed } from '@angular/core/testing';

import { UserSessionResolveService } from './user-session-resolve.service';

describe('UserSessionResolveService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: UserSessionResolveService = TestBed.get(UserSessionResolveService);
    expect(service).toBeTruthy();
  });
});
