import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { SingleUserURL, UsersURL } from 'src/enviroments/enviroments';
import { User } from '../models/user';

@Injectable()
export class ClientService {


  UsersListURL = UsersURL;
  UserURL = SingleUserURL;



  constructor(private http: HttpClient) {
  }


  getUsers(PageIndex: number): Observable<User[]> {
    return this.http.get<User[]>(this.UsersListURL + PageIndex);
  }

  getSingleUser(Id: number): Observable<User> {
    return this.http.get<User>(this.UserURL + Id);
  }

}

