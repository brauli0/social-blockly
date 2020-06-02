import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';

import { GroupService }  from '../group.service';
import { UserService }  from '../user.service';
import { AuthService }  from '../auth.service';

@Component({
  selector: 'app-group-members',
  templateUrl: './group-members.component.html',
  styleUrls: ['./group-members.component.css']
})
export class GroupMembersComponent implements OnInit {

  constructor(
    private toastr: ToastrService,
    private groupService: GroupService,
    private userService: UserService,
    private authService: AuthService,
    private route: ActivatedRoute,
    private translate: TranslateService) {}
  
  groupId: number;
  myUserId: number;
  iAmAdmin: boolean;

  group: {
    id: number,
    groupName: string,
    creationDate: string,
  };

  users: {
    id: number,
    userName: string,
    firstName: string,
    lastName: string,
    email: string,
    signUpDate: string,
    role: string
  }[]
  showingUsers: {
    id: number,
    userName: string,
    firstName: string,
    lastName: string,
    email: string,
    signUpDate: string,
    role: string
  }[]

  foundUsers: {
    id: number,
    userName: string,
    firstName: string,
    lastName: string,
    email: string,
    signUpDate: string,
    role: string,
    isMember: boolean
  }[] = [];

  ngOnInit() {
    this.groupId = +this.route.snapshot.paramMap.get('id');
    this.myUserId = +this.authService.id;

    this.getGroup(this.groupId);
  }

  // ------------
  // MEMBER MANAGEMENT
  // ------------

  getGroup(id: number) {
    this.group = undefined;
    this.users = [];
    this.showingUsers = [];
    this.foundUsers = [];
    this.groupService.getGroup(id).subscribe(response => {
      this.group = response;
      this.iAmAdmin = response.iAmAdmin;

      response.users.forEach(element => {
        element.signUpDate = new Date(element.signUpDate).toLocaleString();
        this.users.push(element);
        this.showingUsers.push(element);
      });
    }, error => {
      console.log(error);
    });
  }

  removeMember(id: number, userName: string) {
    this.groupService.removeMember({userId: id, groupId: this.group.id}).subscribe(response => {
      this.showSuccess('\'' + userName + '\'' + ' has been removed');
      this.getGroup(this.group.id);
    }, error => {
      console.log(error);
      this.showError('\'' + userName + '\'' + ' could not been removed');
    });
  }

  addMember(id: number, userName: string) {
    this.groupService.addMember({userId: id, groupId: this.group.id}).subscribe(response => {
      this.showSuccess('\'' + userName + '\'' + ' has been added');
      this.getGroup(this.group.id);
    }, error => {
      console.log(error);
      this.showError('\'' + userName + '\'' + ' could not been added');
    });
  }

  searchMembers(value: string) {
    value = value.toLowerCase();
    if (value.length == 0) {
      this.showingUsers = this.users;
    } else {
      this.showingUsers = [];
      this.users.forEach(element => {
        let name = element.userName.toLowerCase() + ' ' + 
          element.firstName.toLowerCase() + ' ' + 
          element.lastName.toLowerCase() + ' ' +
          element.email.toLowerCase();
        if (name.includes(value)) {
          this.showingUsers.push(element);
        }
      });
    }
  }

  private isMember(username: string): boolean {
    let isMember = false;
    this.users.forEach(element => {
      if (username === element.userName) {
        isMember = true;
      }
    });
    return isMember;
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
          this.foundUsers.push({id: element.id,
            userName: element.userName,
            firstName: element.firstName,
            lastName: element.lastName,
            email: element.email,
            signUpDate: new Date(element.signUpDate).toLocaleString(),
            role: element.role,
            isMember: this.isMember(element.userName)
          });
        });
      }, error => {
        console.log(error);
        this.showError('GROUP.error-searching');
      });
    }
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
