import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UsersListComponent } from './users-list/users-list.component';
import { UserdetailsComponent } from './userdetails/userdetails.component';
import { UserResolverService } from './userdetails/user-resolver.service';

const routes: Routes = [

  { path: 'userslist' , component: UsersListComponent},
  { path: 'userdetails/:id' , component: UserdetailsComponent , resolve: {user: UserResolverService}},
  
  { path: '**' , component: UsersListComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
