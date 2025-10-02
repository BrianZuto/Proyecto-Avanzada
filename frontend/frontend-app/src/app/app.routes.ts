import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login';
import { ProfileComponent } from './components/profile/profile';
import { HomeComponent } from './components/home/home';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'profile', component: ProfileComponent },
  { path: '', component: HomeComponent }
];
