import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { environment } from '../environments/environment';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  constructor(
    private http: HttpClient,
    private authService: AuthService) {}

  createCommentUrl: string = environment.backend_url + '/comment/create';
  createReplyUrl: string = environment.backend_url + '/comment/reply';
  deleteUrl: string = environment.backend_url + '/comment/delete';

  createComment(comment: {userId: number, programId: number, commentText: string}):
    Observable<{
      id: number,
      commentOrigId: number,
      userId: number,
      userName: string,
      programId: number,
      commentText: string,
      commentDate: string
    }> {
    return this.http.post<{
      id: number,
      commentOrigId: number,
      userId: number,
      userName: string,
      programId: number,
      commentText: string,
      commentDate: string
    }>(this.createCommentUrl, comment, {
      headers: new HttpHeaders({
        'Authorization': 'Bearer '+ this.authService.token
      })
    });
  }

  createReply(comment: {commentOrigId:number, userId: number, programId: number, commentText: string}):
    Observable<{
      id: number,
      commentOrigId: number,
      userId: number,
      userName: string,
      programId: number,
      commentText: string,
      commentDate: string
    }> {
      return this.http.post<{
        id: number,
        commentOrigId: number,
        userId: number,
        userName: string,
        programId: number,
        commentText: string,
        commentDate: string
      }>(this.createReplyUrl, comment, {
        headers: new HttpHeaders({
          'Authorization': 'Bearer '+ this.authService.token
        })
      });
  }

  deleteComment(id: number): Observable<String> {
    return this.http.post<String>(this.deleteUrl, id, {
      headers: new HttpHeaders({
        'Authorization': 'Bearer '+ this.authService.token
      })
    });
  }

  getCommentsByProgram(programId: number): Observable<{
    id: number,
    commentOrigId: number,
    userId: number,
    userName: string,
    programId: number,
    commentText: string,
    commentDate: string
  }[]> {
    return this.http.get<{
      id: number,
      commentOrigId: number,
      userId: number,
      userName: string,
      programId: number,
      commentText: string,
      commentDate: string
    }[]>(environment.backend_url + '/comment/program/' + programId, {
      headers: new HttpHeaders({
        'Authorization': 'Bearer '+ this.authService.token
      })
    });
  }

  getCommentReplies(commentId: number): Observable<{
    id: number,
    commentOrigId: number,
    userId: number,
    userName: string,
    programId: number,
    commentText: string,
    commentDate: string
  }[]> {
    return this.http.get<{
      id: number,
      commentOrigId: number,
      userId: number,
      userName: string,
      programId: number,
      commentText: string,
      commentDate: string
    }[]>(environment.backend_url + '/comment/comment/' + commentId, {
      headers: new HttpHeaders({
        'Authorization': 'Bearer '+ this.authService.token
      })
    });
  }
}
