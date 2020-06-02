import { Injectable } from '@angular/core';

import { HttpClient, HttpHeaders } from '@angular/common/http';

import { environment } from '../environments/environment';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class ExerciseService {

  constructor(
    private http: HttpClient,
    private authService: AuthService) {}

  createExerciseUrl: string = environment.backend_url + '/exercise/create';
  deleteExerciseUrl: string = environment.backend_url + '/exercise/delete';
  updateExerciseUrl: string = environment.backend_url + '/exercise/update';

  createExercise(exercise: {
    statementText: string, groupId: number, userId: number, expirationDate: Date, blocks: string
  }): Observable<{
    id: number,
	  statementText: string,
	  groupId: number,
	  userId: number,
	  creationDate: string,
	  expirationDate: string
  }> {
    return this.http.post<{
      id: number,
      statementText: string,
      groupId: number,
      userId: number,
      creationDate: string,
      expirationDate: string
    }>(this.createExerciseUrl, exercise, {
      headers: new HttpHeaders({
        'Authorization': 'Bearer '+ this.authService.token
      })
    });
  }

  deleteExercise(id: number): Observable<String> {
    return this.http.post<String>(this.deleteExerciseUrl, id, {
      headers: new HttpHeaders({
        'Authorization': 'Bearer '+ this.authService.token
      })
    });
  }

  getExercise(exerciseId: number): Observable<{
    id: number,
	  statementText: string,
	  groupId: number,
	  userId: number,
	  creationDate: string,
    expirationDate: string,
    blocks: string
  }> {
    return this.http.get<{
      id: number,
      statementText: string,
      groupId: number,
      userId: number,
      creationDate: string,
      expirationDate: string,
      blocks: string
    }>(environment.backend_url + '/exercise/' + exerciseId, {
      headers: new HttpHeaders({
        'Authorization': 'Bearer '+ this.authService.token
      })
    });
  }

  updateExercise(data: {
    exerciseId: number, statementText: string, expirationDate: Date
  }): Observable<{
    id: number,
	  statementText: string,
	  groupId: number,
	  userId: number,
	  creationDate: string,
	  expirationDate: string
  }> {
    return this.http.put<{
      id: number,
      statementText: string,
      groupId: number,
      userId: number,
      creationDate: string,
      expirationDate: string
    }>(this.updateExerciseUrl, data, {
      headers: new HttpHeaders({
        'Authorization': 'Bearer '+ this.authService.token
      })
    });
  }

  getExercisesByGroup(groupId: number): Observable<{
    id: number,
	  statementText: string,
	  groupId: number,
	  userId: number,
	  creationDate: string,
	  expirationDate: string
  }[]> {
    return this.http.get<{
      id: number,
      statementText: string,
      groupId: number,
      userId: number,
      creationDate: string,
      expirationDate: string
    }[]>(environment.backend_url + '/exercise/group/' + groupId, {
      headers: new HttpHeaders({
        'Authorization': 'Bearer '+ this.authService.token
      })
    });
  }
}
