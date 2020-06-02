import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';

import { UserService }  from '../user.service';
import { AuthService }  from '../auth.service';
import { ProgramService }  from '../program.service';

@Component({
  selector: 'app-explore',
  templateUrl: './explore.component.html',
  styleUrls: ['./explore.component.css']
})
export class ExploreComponent implements OnInit {

  constructor(
    private toastr: ToastrService,
    private userService: UserService,
    private authService: AuthService,
    private programService: ProgramService,
    private translate: TranslateService) {}

  me: string;

  foundUsers: {
    id: number,
    userName: string,
    firstName: string,
    lastName: string,
    email: string,
    signUpDate: string,
    role: string
  }[] = [];

  foundPrograms: {
    id: number,
    userId: number,
    programName: string,
    programDesc: string,
    creationDate: string,
    updateDate: string,
    code: string
  }[] = [];

  owner: {
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
    privateProgram: boolean,
    userName: string
  }[] = [];

  sharedProgramsReceived: boolean = false;

  ngOnInit() {
    this.me = this.authService.userName;

    this.programService.getSharedProgramsByUser(+this.authService.id).subscribe(response => {
      response.forEach(element => {
        this.sharedPrograms.push({
          id: element.id,
          userId: element.userId,
          programName: element.programName,
          programDesc: element.programDesc,
          creationDate: new Date(element.creationDate).toLocaleString(),
          updateDate: new Date(element.updateDate).toLocaleString(),
          code: element.code,
          privateProgram: element.privateProgram,
          userName: ''
        });
      });

      this.sharedPrograms.forEach(element => {
        this.userService.getUserInfo(element.userId.toString()).subscribe(response => {
          let getUserName = function(response: any): string {return response.userName};
          element.userName = getUserName(response);
        }, error => {
          console.log(error);
          this.showError('EXPLORE.unk-error');
        });
      });

      this.sharedProgramsReceived = true;
    }, error => {
      console.log(error);
      this.showError('EXPLORE.error-shared');
    });
  }

  checkSearch(value: string): boolean {
    let forbiddenCharacters: string[] = ['?', '\\', '/', '.', '#'];
    let allowed: boolean = true;
    
    forbiddenCharacters.forEach(element => {
      if (value.includes(element)) {
        allowed = false;
      }
    })
    
    return allowed;
  }

  searchUsers(value: string) {
    this.foundUsers = [];
    if (value.length != 0 && this.checkSearch(value)) {
      this.userService.getUsersByKeyword(value).subscribe(response => {
        response.forEach(element => {
          // Check if this user is me and don't show it
          if (element.userName != this.me) {
            element.signUpDate = new Date(element.signUpDate).toLocaleString();
            this.foundUsers.push(element);
          }
        });
      }, error => {
        console.log(error);
        this.showError('EXPLORE.error-users');
      });
    }
  }

  searchPrograms(value: string) {
    this.foundPrograms = [];
    if (value.length != 0 && this.checkSearch(value)) {
      this.programService.getProgramsByKeyword(value).subscribe(response => {
        response.forEach(element => {
          element.creationDate = new Date(element.creationDate).toLocaleString();
          element.updateDate = new Date(element.updateDate).toLocaleString();
          this.foundPrograms.push(element);
        });
      }, error => {
        console.log(error);
        this.showError('EXPLORE.error-programs');
      });
    }
  }

  private setOwner(user: any) {
    this.owner = user;
  }

  getOwner(userId: number): any {
    this.owner = undefined;
    this.userService.getUserInfo(userId.toString()).subscribe(response => {
      this.setOwner(response);
      return response;
    }, error => {
      console.log(error);
      this.showError('EXPLORE.unk-error');
    });
  }

  showError(message: string) {
    this.translate.get(message).subscribe(res => {
      this.toastr.error('', res);
    });
  }
}
