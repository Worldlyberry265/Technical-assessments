import { Injectable } from '@angular/core';
import { ClientService } from './http.client';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { User } from '../models/user';

@Injectable({
  providedIn: 'root'
})
export class UsersService {
  
  private cachedUsers: { [key: number]: { data: User[], timestamp: number } } = {};
  private cachedSingleUsers: { [key: number]: { data: User, timestamp: number } } = {};

  constructor(private httpclient: ClientService) { }

  getUsers(PageIndex: number): Observable<User[]> {
    const cachedUsers = this.cachedUsers[PageIndex];
    if (cachedUsers && this.isCacheValid(cachedUsers.timestamp)) {
      return of(cachedUsers.data);
    } else {
      return this.httpclient.getUsers(PageIndex).pipe(
        map(users => {
          this.cachedUsers[PageIndex] = { data: users, timestamp: Date.now() };
          return users;
        })
      );
    }
  }

  getSingleUser(Id: number): Observable<User> {
    const cachedUser = this.cachedSingleUsers[Id];
    if (cachedUser && this.isCacheValid(cachedUser.timestamp)) {
      return of(cachedUser.data);
    } else {
      return this.httpclient.getSingleUser(Id).pipe(
        map(user => {
          this.cachedSingleUsers[Id] = { data: user, timestamp: Date.now() }; 
          return user;
        })
      );
    }
  }

  private isCacheValid(timestamp: number, expiryDuration: number = 3600000): boolean { //Cached for 60 mins, 1000 = 1s
    return (Date.now() - timestamp) < expiryDuration;
  }
}