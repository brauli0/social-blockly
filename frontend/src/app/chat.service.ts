import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { environment } from '../environments/environment';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class ChatService {

  constructor(
    private http: HttpClient,
    private authService: AuthService) {}

  createUrl: string = environment.backend_url + '/chat/create';

  createMessage(message: {
    groupId: number, userId: number, messageText: string, messageDate: Date
  }): Observable<{
    id: number,
    groupId: number,
    userId: number,
    messageText: string,
    messageDate: string
  }> {
    return this.http.post<{
      id: number,
	    groupId: number,
	    userId: number,
      messageText: string,
      messageDate: string
    }>(this.createUrl, message, {
      headers: new HttpHeaders({
        'Authorization': 'Bearer '+ this.authService.token
      })
    });
  }

  getGroupMessages(groupId: number): Observable<{
    id: number,
    groupId: number,
    userId: number,
    messageText: string,
    messageDate: string
  }[]> {
    return this.http.get<{
      id: number,
	    groupId: number,
	    userId: number,
      messageText: string,
      messageDate: string
    }[]>(environment.backend_url + '/chat/all/' + groupId, {
      headers: new HttpHeaders({
        'Authorization': 'Bearer '+ this.authService.token
      })
    });
  }

  getGroupMessagesByDate(data: {groupId: number, begin: Date, end: Date}): Observable<{
    id: number,
    groupId: number,
    userId: number,
    messageText: string,
    messageDate: string
  }[]> {
    return this.http.post<{
      id: number,
      groupId: number,
      userId: number,
      messageText: string,
      messageDate: string
    }[]>(environment.backend_url + '/chat/allbydate', data, {
      headers: new HttpHeaders({
        'Authorization': 'Bearer '+ this.authService.token
      })
    });
  }
}
