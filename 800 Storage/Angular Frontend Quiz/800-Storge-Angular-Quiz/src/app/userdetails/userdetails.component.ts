import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-userdetails',
  templateUrl: './userdetails.component.html',
  styleUrls: ['./userdetails.component.scss']
})
export class UserdetailsComponent implements OnInit {

  constructor ( private route : ActivatedRoute) {}
  
  ID !: number;
  FirstName !: string;
  LastName !: string;
  Avatar !: string;


  UserDetails: any; 

  ngOnInit(): void {
    this.UserDetails = this.route.snapshot.data['user'].data;
    this.ID = this.UserDetails.id;
    this.FirstName = this.UserDetails.first_name;
    this.LastName = this.UserDetails.last_name;
    this.Avatar = this.UserDetails.avatar;


  }

}
