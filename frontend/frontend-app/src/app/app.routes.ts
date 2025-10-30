import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login';
import { ProfileComponent } from './components/profile/profile';
import { HomeComponent } from './components/home/home';
import { MenComponent } from './components/men/men';
import { WomenComponent } from './components/women/women';
import { KidsComponent } from './components/kids/kids';
import { OffersComponent } from './components/offers/offers';
import { AdminComponent } from './components/admin/admin';
import { AdminDashboardComponent } from './components/admin/dashboard/admin-dashboard';
import { AdminProductosComponent } from './components/admin/sections/admin-productos';
import { AdminComprasComponent } from './components/admin/sections/admin-compras';
import { AdminVentasComponent } from './components/admin/sections/admin-ventas';
import { AdminUsuariosComponent } from './components/admin/sections/admin-usuarios';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'profile', component: ProfileComponent },
  { path: 'hombres', component: MenComponent },
  { path: 'mujeres', component: WomenComponent },
  { path: 'ninos', component: KidsComponent },
  { path: 'ofertas', component: OffersComponent },
  { path: 'admin', component: AdminComponent, children: [
    { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
    { path: 'dashboard', component: AdminDashboardComponent },
    { path: 'productos', component: AdminProductosComponent },
    { path: 'compras', component: AdminComprasComponent },
    { path: 'ventas', component: AdminVentasComponent },
    { path: 'usuarios', component: AdminUsuariosComponent }
  ] },
  { path: '', component: HomeComponent }
];
