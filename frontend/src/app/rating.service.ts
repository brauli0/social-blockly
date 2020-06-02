import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { environment } from '../environments/environment';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class RatingService {

  constructor(
    private http: HttpClient,
    private authService: AuthService) {}

  createRatingUrl: string = environment.backend_url + '/rating/create';
  deleteRatingUrl: string = environment.backend_url + '/rating/delete';

  createRating(rating: {userId: number, programId: number, rating: number}): Observable<{
    userId: number,
    programId: number,
    rating: number
  }> {
    return this.http.post<{
      userId: number,
      programId: number,
      rating: number
    }>(this.createRatingUrl, rating, {
      headers: new HttpHeaders({
        'Authorization': 'Bearer '+ this.authService.token
      })
    });
  }

  deleteRating(rating: {userId: number, programId: number}): Observable<String> {
    return this.http.post<String>(this.deleteRatingUrl, rating, {
      headers: new HttpHeaders({
        'Authorization': 'Bearer '+ this.authService.token
      })
    });
  }

  getProgramRatings(programId: number): Observable<{
    userId: number,
    programId: number,
    rating: number
  }[]> {
    return this.http.get<{
      userId: number,
      programId: number,
      rating: number
    }[]>(environment.backend_url + '/rating/all/' + programId, {
      headers: new HttpHeaders({
        'Authorization': 'Bearer '+ this.authService.token
      })
    });
  }
}
