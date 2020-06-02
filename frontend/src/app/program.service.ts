import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { environment } from '../environments/environment';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class ProgramService {

  constructor(
    private http: HttpClient,
    private authService: AuthService) {}

  createUrl: string = environment.backend_url + '/programs/create';
  deleteUrl: string = environment.backend_url + '/programs/delete';
  updateUrl: string = environment.backend_url + '/programs/update';
  setPublicUrl: string = environment.backend_url + '/programs/setpublic';
  setPrivateUrl: string = environment.backend_url + '/programs/setprivate';
  shareUrl: string = environment.backend_url + '/programs/share';
  unshareUrl: string = environment.backend_url + '/programs/unshare';

  createProgram(
    program: {
      userId: number,
      programName: string,
      programDesc: string,
      code: string,
      privateProgram: boolean
    }): Observable<{
      id: number,
      userId: number,
      programName: string,
      programDesc: string,
      creationDate: string,
      updateDate: string,
      code: string,
      privateProgram: boolean
    }> {
    return this.http.post<{
      id: number,
      userId: number,
      programName: string,
      programDesc: string,
      creationDate: string,
      updateDate: string,
      code: string,
      privateProgram: boolean
    }>(this.createUrl, program, {
      headers: new HttpHeaders({
        'Authorization': 'Bearer '+ this.authService.token
      })
    });
  }

  createSolution(
    program: {
      userId: number,
      exerciseId: number,
      programName: string,
      programDesc: string,
      code: string,
      privateProgram: boolean
    }): Observable<{
      id: number,
      userId: number,
      programName: string,
      programDesc: string,
      creationDate: string,
      updateDate: string,
      code: string,
      privateProgram: boolean
    }> {
    return this.http.post<{
      id: number,
      userId: number,
      programName: string,
      programDesc: string,
      creationDate: string,
      updateDate: string,
      code: string,
      privateProgram: boolean
    }>(this.createUrl, program, {
      headers: new HttpHeaders({
        'Authorization': 'Bearer '+ this.authService.token
      })
    });
  }

  deleteProgram(data: {userId: number, programId: number}): Observable<String> {
    return this.http.post<String>(this.deleteUrl, data, {
      headers: new HttpHeaders({
        'Authorization': 'Bearer '+ this.authService.token
      })
    });
  }

  getProgram(programId: number): Observable<{
    id: number,
    userId: number,
    exerciseId: number,
    userName: string,
    programName: string,
    programDesc: string,
    creationDate: string,
    updateDate: string,
    code: string,
    privateProgram: boolean
  }> {
    return this.http.get<{
      id: number,
      userId: number,
      exerciseId: number,
      userName: string,
      programName: string,
      programDesc: string,
      creationDate: string,
      updateDate: string,
      code: string,
      privateProgram: boolean
    }>(environment.backend_url + '/programs/' + programId, {
      headers: new HttpHeaders({
        'Authorization': 'Bearer '+ this.authService.token
      })
    });
  }

  getExampleProgram(): Observable<any> {
    return this.http.get<any>(environment.backend_url + '/programs/example');
  }

  getProgramFullInfo(programId: number): Observable<{
    id: number,
    userId: number,
    programName: string,
    programDesc: string,
    creationDate: string,
    updateDate: string,
    code: string,
    privateProgram: boolean,
    visibility: string,
    sharedUsers: {
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
      userId: number,
      programName: string,
      programDesc: string,
      creationDate: string,
      updateDate: string,
      code: string,
      privateProgram: boolean,
      visibility: string,
      sharedUsers: {
        id: number,
        userName: string,
        firstName: string,
        lastName: string,
        email: string,
        signUpDate: string,
        role: string
      }[]
    }>(environment.backend_url + '/programs/fullinfo/' + programId, {
      headers: new HttpHeaders({
        'Authorization': 'Bearer '+ this.authService.token
      })
    });
  }

  updateProgram(
    program:
    {
      userId: number,
      programId: number,
      programName: string,
      programDesc: string,
      code: string
    }): Observable<String> {
    return this.http.put<String>(this.updateUrl, program, {
      headers: new HttpHeaders({
        'Authorization': 'Bearer '+ this.authService.token
      })
    });
  }

  getProgramsByUser(id: number):
  Observable<{
    id: number,
    userId: number,
    programName: string,
    programDesc: string,
    creationDate: string,
    updateDate: string,
    code: string,
    privateProgram: boolean,
    visibility: string,
    sharedUsers: {
      id: number,
      userName: string,
      firstName: string,
      lastName: string,
      email: string,
      signUpDate: string,
      role: string
    }[]
  }[]> {
    return this.http.get<
    {
      id: number,
      userId: number,
      programName: string,
      programDesc: string,
      creationDate: string,
      updateDate: string,
      code: string,
      privateProgram: boolean,
      visibility: string,
      sharedUsers: {
        id: number,
        userName: string,
        firstName: string,
        lastName: string,
        email: string,
        signUpDate: string,
        role: string
      }[]
    }[]>(environment.backend_url + '/programs/all/' + id, {
      headers: new HttpHeaders({
        'Authorization': 'Bearer '+ this.authService.token
      })
    });
  }

  getProgramsByKeyword(keyword: string): Observable<{
    id: number,
    userId: number,
    programName: string,
    programDesc: string,
    creationDate: string,
    updateDate: string,
    code: string,
    privateProgram: boolean
  }[]> {
    return this.http.get<{
      id: number,
      userId: number,
      programName: string,
      programDesc: string,
      creationDate: string,
      updateDate: string,
      code: string,
      privateProgram: boolean
    }[]>(environment.backend_url + '/programs/search/' + keyword, {
      headers: new HttpHeaders({
        'Authorization': 'Bearer '+ this.authService.token
      })
    });
  }

  setPublic(userId: number, programId: number): Observable<{
    id: number,
    userId: number,
    programName: string,
    programDesc: string,
    creationDate: string,
    updateDate: string,
    code: string,
    privateProgram: boolean,
    visibility: string,
    sharedUsers: {
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
      userId: number,
      programName: string,
      programDesc: string,
      creationDate: string,
      updateDate: string,
      code: string,
      privateProgram: boolean,
      visibility: string,
      sharedUsers: {
        id: number,
        userName: string,
        firstName: string,
        lastName: string,
        email: string,
        signUpDate: string,
        role: string
      }[]
    }>(this.setPublicUrl, {userId: userId, programId: programId}, {
      headers: new HttpHeaders({
        'Authorization': 'Bearer '+ this.authService.token
      })
    });
  }

  setPrivate(userId: number, programId: number): Observable<{
    id: number,
    userId: number,
    programName: string,
    programDesc: string,
    creationDate: string,
    updateDate: string,
    code: string,
    privateProgram: boolean,
    visibility: string,
    sharedUsers: {
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
      userId: number,
      programName: string,
      programDesc: string,
      creationDate: string,
      updateDate: string,
      code: string,
      privateProgram: boolean,
      visibility: string,
      sharedUsers: {
        id: number,
        userName: string,
        firstName: string,
        lastName: string,
        email: string,
        signUpDate: string,
        role: string
      }[]
    }>(this.setPrivateUrl, {userId: userId, programId: programId}, {
      headers: new HttpHeaders({
        'Authorization': 'Bearer '+ this.authService.token
      })
    });
  }

  getPublicProgramsByUser(userId: number): Observable<{
    id: number,
    userId: number,
    programName: string,
    programDesc: string,
    creationDate: string,
    updateDate: string,
    code: string,
    privateProgram: boolean
  }[]> {
    return this.http.get<{
      id: number,
      userId: number,
      programName: string,
      programDesc: string,
      creationDate: string,
      updateDate: string,
      code: string,
      privateProgram: boolean
    }[]>(environment.backend_url + '/programs/public/' + userId);
  }

  shareProgram(data: {ownerId: number, userId: number, programId: number}): Observable<String> {
    return this.http.put<String>(this.shareUrl, data, {
      headers: new HttpHeaders({
        'Authorization': 'Bearer '+ this.authService.token
      })
    });
  }

  unshareProgram(data: {ownerId: number, userId: number, programId: number}): Observable<String> {
    return this.http.put<String>(this.unshareUrl, data, {
      headers: new HttpHeaders({
        'Authorization': 'Bearer '+ this.authService.token
      })
    });
  }

  // getSharedProgramsWithMe
  getSharedProgramsByUser(id: number): Observable<{
    id: number,
    userId: number,
    programName: string,
    programDesc: string,
    creationDate: string,
    updateDate: string,
    code: string,
    privateProgram: boolean
  }[]> {
    return this.http.get<{
      id: number,
      userId: number,
      programName: string,
      programDesc: string,
      creationDate: string,
      updateDate: string,
      code: string,
      privateProgram: boolean
    }[]>(environment.backend_url + '/programs/shared/' + id, {
      headers: new HttpHeaders({
        'Authorization': 'Bearer '+ this.authService.token
      })
    });
  }

  getSharedProgramsWithMeByUser(myUserId: number, otherUserId: number): Observable<{
    id: number,
    userId: number,
    programName: string,
    programDesc: string,
    creationDate: string,
    updateDate: string,
    code: string,
    privateProgram: boolean
  }[]> {
    return this.http.get<{
      id: number,
      userId: number,
      programName: string,
      programDesc: string,
      creationDate: string,
      updateDate: string,
      code: string,
      privateProgram: boolean
    }[]>(environment.backend_url + '/programs/shared/' + myUserId + '/' + otherUserId, {
      headers: new HttpHeaders({
        'Authorization': 'Bearer '+ this.authService.token
      })
    });
  }

  getProgramsByExercise(exerciseId: number): Observable<{
    id: number,
    userId: number,
    programName: string,
    programDesc: string,
    creationDate: string,
    updateDate: string,
    code: string,
    privateProgram: boolean
  }[]> {
    return this.http.get<{
      id: number,
      userId: number,
      programName: string,
      programDesc: string,
      creationDate: string,
      updateDate: string,
      code: string,
      privateProgram: boolean
    }[]>(environment.backend_url + '/programs/exercise/' + exerciseId, {
      headers: new HttpHeaders({
        'Authorization': 'Bearer '+ this.authService.token
      })
    });
  }
}