import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { AuthenticationComponent } from './authentication/authentication.component'
import { WelcomeComponent } from './welcome/welcome.component';
import { RegisterComponent } from './register/register.component';
import { ProfileComponent } from './profile/profile.component';
import { MyprogramsComponent } from './myprograms/myprograms.component';
import { EditorComponent } from './editor/editor.component';
import { MygroupsComponent } from './mygroups/mygroups.component';
import { GroupdetailComponent } from './groupdetail/groupdetail.component';
import { ExploreComponent } from './explore/explore.component';
import { UserComponent } from './user/user.component';
import { ProgramPermissionsComponent } from './program-permissions/program-permissions.component';
import { GroupMembersComponent } from './group-members/group-members.component';
import { ExerciseComponent } from './exercise/exercise.component';
import { NotFoundComponent } from './not-found/not-found.component';

import { AuthGuardService } from './auth-guard.service';

const routes: Routes = [
  { path: '', redirectTo: 'welcome', pathMatch: 'full' },
  { path: 'welcome', component: WelcomeComponent },
  { path: 'login', component: AuthenticationComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'profile', component: ProfileComponent, canActivate: [AuthGuardService] },
  { path: 'myprograms', component: MyprogramsComponent, canActivate: [AuthGuardService] },
  { path: 'program/:id', component: EditorComponent, canActivate: [AuthGuardService] },
  { path: 'mygroups', component: MygroupsComponent, canActivate: [AuthGuardService] },
  { path: 'group/:id', component: GroupdetailComponent, canActivate: [AuthGuardService] },
  { path: 'explore', component:  ExploreComponent, canActivate: [AuthGuardService] },
  { path: 'user/:username', component:  UserComponent, canActivate: [AuthGuardService] },
  { path: 'program/permissions/:id', component: ProgramPermissionsComponent, canActivate: [AuthGuardService] },
  { path: 'groupmanagement/:id', component:  GroupMembersComponent, canActivate: [AuthGuardService] },
  { path: 'exercise/:id', component:  ExerciseComponent, canActivate: [AuthGuardService] },
  { path: '**', component: NotFoundComponent }
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forRoot(routes)
  ],
  exports: [RouterModule]
})
export class AppRoutingModule {}