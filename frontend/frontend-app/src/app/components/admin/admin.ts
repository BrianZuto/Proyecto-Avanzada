import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MainLayoutComponent } from '../../layouts/main-layout/main-layout';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule, RouterModule, MainLayoutComponent],
  templateUrl: './admin.html',
  styleUrl: './admin.css'
})
export class AdminComponent {
}


