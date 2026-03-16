import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login';
import { RegisterComponent } from './pages/register/register';
import { InicioComponent } from './pages/inicio/inicio';
import { PrivadosComponent } from './pages/privados/privados';
import { PublicosComponent } from './pages/publicos/publicos';
import { EntrenamientosComponent } from './pages/entrenamientos/entrenamientos';
import { SoporteComponent } from './pages/soporte/soporte';
import { ValoracionesComponent } from './pages/valoraciones/valoraciones';
import { authGuard } from './guards/auth-guard';
import { publicGuard } from './guards/public-guard';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },

  { path: 'login', component: LoginComponent, canActivate: [publicGuard] },
  { path: 'register', component: RegisterComponent, canActivate: [publicGuard] },

  { path: 'inicio', component: InicioComponent, canActivate: [authGuard] },
  { path: 'privados', component: PrivadosComponent, canActivate: [authGuard] },
  { path: 'publicos', component: PublicosComponent, canActivate: [authGuard] },
  { path: 'entrenamientos', component: EntrenamientosComponent, canActivate: [authGuard] },
  { path: 'soporte', component: SoporteComponent, canActivate: [authGuard] },
  { path: 'valoraciones', component: ValoracionesComponent, canActivate: [authGuard] },

  { path: '**', redirectTo: 'login' }
];
