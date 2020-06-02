import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { environment } from '../environments/environment';
import { AuthService} from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class GroupService {

  constructor(
    private http: HttpClient,
    private authService: AuthService) {}

  createUrl: string = environment.backend_url + '/groups/create';
  deleteUrl: string = environment.backend_url + '/groups/delete';
  updateUrl: string = environment.backend_url + '/groups/update';
  addMemberUrl: string = environment.backend_url + '/groups/addmember';
  removeMemberUrl: string = environment.backend_url + '/groups/removemember';

  createGroup(data: {userId: number, groupName: string}):
    Observable<{id: number,
      groupName: string,
      creationDate: string,
      usernames: string[]
    }> {
    return this.http.post<{
      id: number,
      groupName: string,
      creationDate: string,
      usernames: string[]
    }>(this.createUrl, data, {
      headers: new HttpHeaders({
        'Authorization': 'Bearer '+ this.authService.token
      })
    });
  }

  deleteGroup(data: {groupId: number}): Observable<String> {
    return this.http.post<String>(this.deleteUrl, data, {
      headers: new HttpHeaders({
        'Authorization': 'Bearer '+ this.authService.token
      })
    });
  }

  getGroup(groupId: number):
    Observable<{id: number,
      groupName: string,
      creationDate: string,
      iAmAdmin: boolean,
      chatEnable: boolean,
      users: {
        id: number,
        userName: string,
        firstName: string,
        lastName: string,
        email: string,
        signUpDate: string,
        role: string
      }[]
    }> {
    return this.http.get<{
      id: number,
      groupName: string,
      creationDate: string,
      iAmAdmin: boolean,
      chatEnable: boolean,
      users: {
        id: number,
        userName: string,
        firstName: string,
        lastName: string,
        email: string,
        signUpDate: string,
        role: string
      }[]
    }>(environment.backend_url + "/groups/" + groupId, {
      headers: new HttpHeaders({
        'Authorization': 'Bearer '+ this.authService.token
      })
    });
  }

  updateGroup(data: {groupId: number, groupName: string, chatEnable: boolean}):
    Observable<{
      id: number,
      groupName: string,
      creationDate: string,
      chatEnable: boolean,
      users: {
        id: number,
        userName: string,
        firstName: string,
        lastName: string,
        email: string,
        signUpDate: string,
        role: string
      }[]
    }> {
    return this.http.put<{
      id: number,
      groupName: string,
      creationDate: string,
      chatEnable: boolean,
      users: {
        id: number,
        userName: string,
        firstName: string,
        lastName: string,
        email: string,
        signUpDate: string,
        role: string
      }[]
    }>(this.updateUrl, data, {
      headers: new HttpHeaders({
        'Authorization': 'Bearer '+ this.authService.token
      })
    });
  }

  addMember(data: {userId: number, groupId: number}): Observable<String> {
    return this.http.put<String>(this.addMemberUrl, data, {
      headers: new HttpHeaders({
        'Authorization': 'Bearer '+ this.authService.token
      })
    });
  }

  removeMember(data: {userId: number, groupId: number}): Observable<String> {
    return this.http.put<String>(this.removeMemberUrl, data, {
      headers: new HttpHeaders({
        'Authorization': 'Bearer '+ this.authService.token
      })
    });
  }

  // Aquí cada grupo non trae lista de "users"
  getAllGroupsByUser(userId: number):
    Observable<{
      id: number,
      groupName: string,
      creationDate: string
    }[]> {
    return this.http.get<{
      id: number,
      groupName: string,
      creationDate: string
    }[]>(environment.backend_url + '/groups/allgroups/' + userId, {
      headers: new HttpHeaders({
        'Authorization': 'Bearer '+ this.authService.token
      })
    });
  }
}