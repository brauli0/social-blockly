import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { environment } from '../environments/environment';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(
    private http: HttpClient,
    private authService: AuthService) {}

  registerUrl: string = environment.backend_url + '/users/signUp';
  loginUrl: string = environment.backend_url + '/users/login';
  lastLoginUrl: string = environment.backend_url + '/users/lastLogin';

  doLogin(user: {userName: string, password: string }): Observable<String> {
    return this.http.post<String>(this.loginUrl, user);
  }

  saveLastAccessDate() {
    return this.http.post<String>(this.lastLoginUrl, 1, {
      headers: new HttpHeaders({
        'Authorization': 'Bearer '+ this.authService.token
      })
    });
  }

  doSignUp(
    user: {
      userName: string,
      password: string,
      firstName: string,
      lastName: string,
      email: string,
      role: string
    }): Observable<String> {
    return this.http.post<String>(this.registerUrl, user);
  };

  // URL: environment.backend_url + '/users/'+ id + '/changePassword'
  doChangePassword(id: string,
    data: {
      oldPassword: string,
      newPassword: string
    }
  ) {
    return this.http.post<String>(environment.backend_url + '/users/' + id + '/changePassword', data, {
      headers: new HttpHeaders({
        'Authorization': 'Bearer '+ this.authService.token
      })
    });
  }

  doUpdateProfile(user: {id: number, firstName: string, lastName: string, email:string}): Observable<String> {
    return this.http.put<String>(environment.backend_url + '/users/' + user.id, user, {
      headers: new HttpHeaders({
        'Authorization': 'Bearer '+ this.authService.token
      })
    });
  }

  getUserInfo(id: string): Observable<String> {
    return this.http.get<String>(environment.backend_url + '/users/info/' + id);
  }

  getUsersByKeyword(keyword: string): Observable<
  {
    id: number,
    userName: string,
    firstName: string,
    lastName: string,
    email: string,
    signUpDate: string,
    role: string
  }[]> {
    return this.http.get<
    {
      id: number,
      userName: string,
      firstName: string,
      lastName: string,
      email: string,
      signUpDate: string,
      role: string
    }[]>(environment.backend_url + '/users/search/' + keyword);
  }

  getUserPublicInfo(username: string): Observable<
  {
    id: number,
    userName: string,
    firstName: string,
    lastName: string,
    email: string,
    signUpDate: string,
    role: string
  }> {
    return this.http.get<{
      id: number,
      userName: string,
      firstName: string,
      lastName: string,
      email: string,
      signUpDate: string,
      role: string
    }>(environment.backend_url + '/users/publicinfo/' + username);
  }
}

/*
  Answer from 'doLogin' and 'doSignUp':
  {
    serviceToken: string,
    user: {
      id: number;
	    userName: string;
	    password: string;
	    firstName: string;
	    lastName: string;
	    email: string;
	    signUpDate: string;
	    lastLoginDate: string;
	    role: string;
    }
  }
  */
