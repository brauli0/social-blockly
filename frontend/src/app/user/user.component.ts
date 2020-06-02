import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';

import { UserService }  from '../user.service';
import { ProgramService }  from '../program.service';
import { GroupService }  from '../group.service';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  constructor(
    private route: ActivatedRoute,
    private toastr: ToastrService,
    private userService: UserService,
    private programService: ProgramService,
    private groupService: GroupService,
    private authService: AuthService,
    private router: Router,
    private translate: TranslateService) { }

  myUserId: number;
  userexists = false;

  user: {
    id: number,
    userName: string,
    firstName: string,
    lastName: string,
    email: string,
    signUpDate: string,
    role: string
  };

  sharedPrograms: {
    id: number,
    userId: number,
    programName: string,
    programDesc: string,
    creationDate: string,
    updateDate: string,
    code: string,
    privateProgram: boolean
  }[] = [];

  programs: {
    id: number,
    userId: number,
    programName: string,
    programDesc: string,
    creationDate: string,
    updateDate: string,
    code: string,
    privateProgram: boolean
  }[] = [];

  groups: {
    id: number,
    groupName: string,
    creationDate: string;
  }[] = [];

  getGroupsInCommon(
    usergroups: {id: number, groupName: string, creationDate: string;}[],
    mygroups: {id: number, groupName: string, creationDate: string;}[])
    {
    usergroups.forEach(element1 => {
      mygroups.forEach(element2 => {
        if (element1.id == element2.id) {
          element1.creationDate = new Date(element1.creationDate).toLocaleString();
          this.groups.push(element1);
        }
      });
    });
    // QUITAR REPETIDOS
  }

  ngOnInit() {
    this.myUserId = +this.authService.id;
    const username = this.route.snapshot.paramMap.get('username');
    
    this.userService.getUserPublicInfo(username).subscribe(response => {
      if (response.id == this.myUserId) {
        this.router.navigate(['/profile']);
      }
      response.signUpDate = new Date(response.signUpDate).toLocaleString();
      this.user = response;

      this.userexists = true;

      // Get shared programs
      this.programService.getSharedProgramsWithMeByUser(+this.authService.id, this.user.id).subscribe(response => {
        response.forEach(element => {
          this.sharedPrograms.push(
            {
              id: element.id,
              userId: element.userId,
              programName: element.programName,
              programDesc: element.programDesc,
              creationDate: new Date(element.creationDate).toLocaleString(),
              updateDate: new Date(element.updateDate).toLocaleString(),
              code: element.code,
              privateProgram: element.privateProgram
            }
          );
        });
      }, error => {
        console.log(error);
        this.showError('USER.unk-error');
      });

      // Get programs
      this.programService.getPublicProgramsByUser(this.user.id).subscribe(response => {
        response.forEach(element => {
          this.programs.push(
            {
              id: element.id,
              userId: element.userId,
              programName: element.programName,
              programDesc: element.programDesc,
              creationDate: new Date(element.creationDate).toLocaleString(),
              updateDate: new Date(element.updateDate).toLocaleString(),
              code: element.code,
              privateProgram: element.privateProgram
            }
          );
        });
      }, error => {
        console.log(error);
        this.showError('USER.unk-error');
      });

      // Get groups in common
      this.groupService.getAllGroupsByUser(this.user.id).subscribe(response => {
        let usergroups = response;
        this.groupService.getAllGroupsByUser(+this.authService.id).subscribe(response => {
          let mygroups = response;
          this.getGroupsInCommon(usergroups, mygroups);
        }, error => {
          console.log(error);
          this.showError('USER.unk-error');
        });
      }, error => {
        console.log(error);
        this.showError('USER.unk-error');
      });

    }, error => {
      // This user doesn't exist
      console.log(error);
      this.userexists = false;
    });
  }

  showError(message: string) {
    this.translate.get(message).subscribe(res => {
      this.toastr.error('', res);
    });
  }
}
