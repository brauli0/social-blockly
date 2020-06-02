import { Injectable } from '@angular/core';
import { Router, CanActivate } from '@angular/router';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService {

  constructor(
    public router: Router,
    public authService: AuthService) {}
  
  canActivate(): boolean {
    if (!this.authService.isAuthenticated) {
      this.router.navigate(['/login']);
      return false;
    }

    return true;
  }
}