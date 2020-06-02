import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormControl } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';

import { ProgramService } from '../program.service';
import { UserService }  from '../user.service';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-program-permissions',
  templateUrl: './program-permissions.component.html',
  styleUrls: ['./program-permissions.component.css']
})
export class ProgramPermissionsComponent implements OnInit {

  constructor(
    private programService: ProgramService,
    private userService: UserService,
    private authService: AuthService,
    private route: ActivatedRoute,
    private toastr: ToastrService,
    private translate: TranslateService) {}

  myUserId: number;
  programId: number;

  program: {
    id: number,
    userId: number,
    programName: string,
    programDesc: string,
    creationDate: string,
    updateDate: string,
    code: string,
    privateProgram: boolean,
    visibility: string,
    sharedUsers: {
      id: number,
      userName: string,
      firstName: string,
      lastName: string,
      email: string,
      signUpDate: string,
      role: string
    }[]
  };
  gotanswer: boolean = false;

  visibilitySelect = new FormControl('');

  foundUsers: {
    id: number,
    userName: string,
    firstName: string,
    lastName: string,
    email: string,
    signUpDate: string,
    role: string
  }[] = [];

  private getProgramInit() {
    this.programService.getProgramFullInfo(this.programId).subscribe(response => {
      this.program = response;
      this.program.sharedUsers.forEach(element => {
        element.signUpDate = new Date(element.signUpDate).toLocaleString();
      });
      this.visibilitySelect.setValue(this.program.visibility);
      this.gotanswer = true;
    }, error => {
      console.log(error);
      this.gotanswer = false;
    });
  }

  private getProgram() {
    this.programService.getProgramFullInfo(this.programId).subscribe(response => {
      this.program = response;
      this.program.sharedUsers.forEach(element => {
        element.signUpDate = new Date(element.signUpDate).toLocaleString();
      });
    }, error => {
      console.log(error);
    });
  }

  ngOnInit() {
    this.myUserId = +this.authService.id;
    this.programId = +this.route.snapshot.paramMap.get('id');

    this.getProgramInit();
  }

  private changeVisibility(programId: number, newVisibility: string) {
    if (newVisibility == 'Public') {
      this.programService.setPublic(this.myUserId, programId).subscribe(response => {
        this.program = response;
      }, error => {
        console.log(error);
        this.showError('PROGRAMS.error-updating');
      });
    } else if (newVisibility == 'Private') {
      this.programService.setPrivate(this.myUserId, programId).subscribe(response => {
        this.program = response;
      }, error => {
        console.log(error);
        this.showError('PROGRAMS.error-updating');
      });
    } else if (newVisibility == 'Shared') {
      this.programService.setPrivate(this.myUserId, programId).subscribe(response => {
        this.program = response;
      }, error => {
        console.log(error);
        this.showError('PROGRAMS.error-updating');
      });
    } else {
      console.log('This should not happen. program-permissions.component.ts: changeVisibility()');
    }
  }

  onSelectChange(value: string) {
    this.changeVisibility(this.programId, value);
  }

  private checkSearch(value: string): boolean {
    let forbiddenCharacters: string[] = ['?', '\\', '/', '.', '#'];
    let allowed: boolean = true;
    
    forbiddenCharacters.forEach(element => {
      if (value.includes(element)) {
        allowed = false;
      }
    })
    
    return allowed;
  }

  private isShared(username: string): boolean {
    let isShared = false;
    this.program.sharedUsers.forEach(element => {
      if (username === element.userName) {
        isShared = true;
      }
    });
    return isShared;
  }

  searchUsers(value: string) {
    this.foundUsers = [];
    if (value.length != 0 && this.checkSearch(value)) {
      this.userService.getUsersByKeyword(value).subscribe(response => {
        response.forEach(element => {
          // Check if this user is already 'shared'
          if (!this.isShared(element.userName) && element.id != this.myUserId) {
            element.signUpDate = new Date(element.signUpDate).toLocaleString();
            this.foundUsers.push(element);
          }
        });
      }, error => {
        console.log(error);
        this.showError('GROUP.error-searching');
      });
    }
  }

  shareProgram(userId: number) {
    this.programService.shareProgram({ownerId: this.myUserId, userId: userId, programId: this.programId}).subscribe(response => {
      this.getProgram();
      this.showSuccess('PERMISSIONS.shared');
    }, error => {
      console.log(error);
      this.showError('PERMISSIONS.error-sharing');
    });
  }

  stopSharing(userId: number) {
    this.programService.unshareProgram({ownerId: this.myUserId, userId: userId, programId: this.programId}).subscribe(response => {
      this.getProgram();
      this.showSuccess('PERMISSIONS.removed');
    }, error => {
      console.log(error);
      this.showError('PERMISSIONS.error-sharing');
    });
  }

  showSuccess(message: string) {
    this.translate.get(message).subscribe(res => {
      this.toastr.success('', res);
    });
  }

  showError(message: string) {
    this.translate.get(message).subscribe(res => {
      this.toastr.error('', res);
    });
  }
}
