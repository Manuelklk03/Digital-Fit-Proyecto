import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth-service';
import { catchError, map, of } from 'rxjs';

export const publicGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  return authService.yo().pipe(
    map(() => {
      router.navigate(['/inicio']);
      return false;
    }),
    catchError(() => of(true))
  );
};
