import { Component, OnInit, ViewChild } from '@angular/core';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';

import { UsersService } from '../Services/users.service';
import { PageEvent } from '@angular/material/paginator';
import { Router } from '@angular/router';

@Component({
  selector: 'app-users-list',
  templateUrl: './users-list.component.html',
  styleUrls: ['./users-list.component.scss']
})
export class UsersListComponent implements OnInit {


  isLoading: boolean = true;


  dataSource = new MatTableDataSource([]);
  displayedColumns = ['avatar', 'first_name', 'last_name', 'id'];
  UsersCount: number = 0;
  pageSize: number = 6;

  @ViewChild(MatSort) sort !: MatSort;

  constructor(
    private userService: UsersService,
    private router: Router) { }

  ngOnInit(): void {
    this.getData(1);
  }

  getData(pageIndex: number) {
    this.isLoading = true;
    this.userService.getUsers(pageIndex).subscribe({
      next: (users: any) => {
        this.UsersCount = users.total;
        this.dataSource = new MatTableDataSource(users.data);
        this.dataSource.sort = this.sort;
        this.isLoading = false;

      }
    });
  }

  onPageChange(event: PageEvent) {
    this.getData(event.pageIndex + 1);
  }

  SearchUsersById(Id: string) {
    if (+Id > 0) {
      this.isLoading = true;
      this.userService.getSingleUser(+Id).subscribe({
        next: (user: any) => {
          this.UsersCount = 1;
          let usersArray: any = [{
            avatar: user.data.avatar,
            first_name: user.data.first_name,
            last_name: user.data.last_name,
            id: user.data.id,
          }];
          this.dataSource = new MatTableDataSource(usersArray);
          this.isLoading = false;
        },
        error: () => {
          this.dataSource = new MatTableDataSource([]);
          this.isLoading = false;
        }
      });
    } else {
      this.getData(1);
    }
  }

  ShowUserInfo(Id: number) {
    this.router.navigate(['userdetails/' + Id])
  }
}