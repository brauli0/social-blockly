import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor() { }

  get isAuthenticated(): boolean {
    return (localStorage.getItem('token') != null);
  }

  login(info: any) {
    localStorage.setItem('token', info.serviceToken);
    localStorage.setItem('id', info.user.id);
    localStorage.setItem('firstname', info.user.firstName);
    localStorage.setItem('username', info.user.userName);
  }

  logout() {
    localStorage.clear();
  }

  get id(): string {
    return localStorage.getItem('id');
  }

  get token(): string {
    return localStorage.getItem('token');
  }

  get userName(): string {
    return localStorage.getItem('username');
  }

  get firstName(): string {
    return localStorage.getItem('firstname');
  }
}
