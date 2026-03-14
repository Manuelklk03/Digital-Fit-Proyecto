import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login';
import { RegisterComponent } from './pages/register/register';
import { InicioComponent } from './pages/inicio/inicio';
import { PrivadosComponent } from './pages/privados/privados';
import { PublicosComponent } from './pages/publicos/publicos';
import { EntrenamientosComponent } from './pages/entrenamientos/entrenamientos';
import { SoporteComponent } from './pages/soporte/soporte';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'inicio', component: InicioComponent },
  { path: 'privados', component: PrivadosComponent },
  { path: 'publicos', component: PublicosComponent },
  { path: 'entrenamientos', component: EntrenamientosComponent },
  { path: 'soporte', component: SoporteComponent },
  { path: '**', redirectTo: 'login' }
];
