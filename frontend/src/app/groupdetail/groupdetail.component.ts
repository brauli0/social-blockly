import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { FormControl, Validators } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';

import { GroupService }  from '../group.service';
import { UserService }  from '../user.service';
import { ChatService }  from '../chat.service';
import { AuthService }  from '../auth.service';
import { ExerciseService }  from '../exercise.service';

import { environment } from '../../environments/environment';

@Component({
  selector: 'app-groupdetail',
  templateUrl: './groupdetail.component.html',
  styleUrls: ['./groupdetail.component.css']
})
export class GroupdetailComponent implements OnInit, OnDestroy {
  
  constructor(
    private toastr: ToastrService,
    private route: ActivatedRoute,
    private groupService: GroupService,
    private userService: UserService,
    private chatService: ChatService,
    private authService: AuthService,
    private exerciseService: ExerciseService,
    private router: Router,
    private translate: TranslateService) {}
  
  myUserId: number;
  groupId: number;
  iAmAdmin: boolean = false;

  group: {
    id: number,
    groupName: string,
    creationDate: string,
    chatEnable: boolean
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
  gotanswer: boolean = false;

  foundUsers: {
    id: number,
    userName: string,
    firstName: string,
    lastName: string,
    email: string,
    signUpDate: string,
    role: string
  }[] = [];

  messages: {
    id: number,
    groupId: number,
    userId: number,
    messageText: string,
    messageDate: string,
    userName: string
  }[] = [];
  noMessages: boolean = true;
  lastMessageDate: string;
  stopCheckingForNewMessages: boolean = false;

  exercises: {
    id: number,
	  statementText: string,
	  groupId: number,
	  userId: number,
	  creationDate: string,
	  expirationDate: string
  }[] = [];

  editingGroupName: boolean = false;
  groupNameInput = new FormControl('', Validators.required);
  inputMessage = new FormControl();

  statementInput = new FormControl('', Validators.required);

  yearInput = new FormControl('', Validators.required);
  monthInput = new FormControl('', Validators.required);
  dayInput = new FormControl('', Validators.required);
  hourInput = new FormControl('', Validators.required);
  minuteInput = new FormControl('', Validators.required);
  secondInput = new FormControl('', Validators.required);

  logicCheckbox = new FormControl('');
  loopsCheckbox = new FormControl('');
  mathCheckbox = new FormControl('');
  textCheckbox = new FormControl('');
  listsCheckbox = new FormControl('');
  colorsCheckbox = new FormControl('');
  variablesCheckbox = new FormControl('');
  functionsCheckbox = new FormControl('');

  ngOnInit() {
    this.myUserId = +this.authService.id;

    this.groupId = +this.route.snapshot.paramMap.get('id');
    this.getGroup(this.groupId);
  }

  // ------------
  // CHAT
  // ------------

  private getUsername(userId: number): string {
    let userName: string;
    this.users.forEach(element => {
      if (element.id == userId)
        userName = element.userName;
    });

    return userName;
  }

  getMessages() {
    this.messages = [];
    this.chatService.getGroupMessages(this.groupId).subscribe(response => {
      response.forEach(element => {
        this.messages.push({
          id: element.id,
          groupId: element.groupId,
          userId: element.userId,
          messageText: element.messageText,
          messageDate: new Date(element.messageDate).toLocaleString(),
          userName: this.getUsername(element.userId)
        });
      });

      if (response.length > 0) {
        this.lastMessageDate = response[response.length-1].messageDate;
        this.noMessages = false;
        this.scroll();
      } else {
        this.lastMessageDate = new Date().toLocaleString();
      }
      
      // Check for new messages
      this.checkForNewMessages();
    }, error => {
      console.log(error);
      this.showError('GROUP.error-chat');
    });
  }

  sendMessage() {
    let value: string = this.inputMessage.value;
    if (value.length > 100) {
      this.showError('GROUP.too-much-text');
    }
    else if (value.length > 0) {
      this.chatService.createMessage({
        groupId: this.groupId,
        userId: this.myUserId,
        messageText: value,
        messageDate: new Date()
      }).subscribe(response => {
        this.messages.push({
          id: response.id,
          groupId: response.groupId,
          userId: response.userId,
          messageText: response.messageText,
          messageDate: new Date(response.messageDate).toLocaleString(),
          userName: this.getUsername(response.userId)
        });

        this.inputMessage.setValue('');
        this.scroll();
        
        if (this.noMessages)
          this.noMessages = false;
      }, error => {
        console.log(error);
        if (error.error.globalError == 'operación no permitida') {
          this.showError('GROUP.error-disabled');
          this.inputMessage.setValue('');
        } else {
          this.showError('GROUP.error-send');
        }
      });
    }
  }

  private messageExists(id: number): boolean {
    let exists: boolean = false;
    this.messages.forEach(element => {
      if (element.id == id) {
        exists = true;
      }
    });
    return exists;
  }

  getNewMessages() {
    let last = new Date(this.lastMessageDate);
    let now = new Date();
    // FIX
    last.setHours(last.getHours() + 2);
    now.setHours(now.getHours() + 2);
    
    this.chatService.getGroupMessagesByDate({
      groupId: this.groupId,
      begin: last,
      end: now
    }).subscribe(response => {
      response.forEach(element => {
        if (!this.messageExists(element.id)) {
          this.messages.push({
            id: element.id,
            groupId: element.groupId,
            userId: element.userId,
            messageText: element.messageText,
            messageDate: new Date(element.messageDate).toLocaleString(),
            userName: this.getUsername(element.userId)
          });
        }
      });

      if (response.length > 0) {
        this.lastMessageDate = response[response.length-1].messageDate;
        this.noMessages = false;
        this.scroll();
      }
    }, error => {
      console.log(error);
      this.showError('GROUP.no-connection');
      this.stopCheckingForNewMessages = true;
    });
  }

  private sleep(ms: number) {
    return new Promise(resolve => setTimeout(resolve, ms));
  }

  async checkForNewMessages() {
    while (!this.stopCheckingForNewMessages) {
      await this.sleep(environment.chat_interval);
      this.getNewMessages();
    }
  }

  private scroll() {
    let scrollcode = 'async function sleep() {' +
      'return new Promise(resolve => setTimeout(resolve, 200));' +
    '}' +
    'async function f() {'+
    'await sleep();'+
    'var scrolldiv = document.getElementById("scrolldiv");' +
    'scrolldiv.scrollTop = scrolldiv.scrollHeight;' +
    '}' +
    'f()';

    try {
      eval(scrollcode);
    } catch (e) {
      console.log(e);
    }
  }

  ngOnDestroy() {
    this.stopCheckingForNewMessages = true;
  }

  // ------------
  // EXERCISES
  // ------------

  getExercises() {
    this.exerciseService.getExercisesByGroup(this.groupId).subscribe(response => {
      response.reverse();
      response.forEach(element => {
        this.exercises.push({
          id: element.id,
          statementText: element.statementText,
          groupId: element.groupId,
          userId: element.userId,
          creationDate: new Date(element.creationDate).toLocaleString(),
          expirationDate: new Date(element.expirationDate).toLocaleString()
        });
      });
    }, error => {
      console.log(error);
      this.showError('GROUP.error-exercises');
    });
  }

  setNewExerciseModal() {
    let tomorrow: Date = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);

    this.yearInput.setValue(tomorrow.getFullYear());
    this.monthInput.setValue(tomorrow.getMonth() + 1);
    this.dayInput.setValue(tomorrow.getDate());
    this.hourInput.setValue(tomorrow.getHours());
    this.minuteInput.setValue(tomorrow.getMinutes());
    this.secondInput.setValue(tomorrow.getSeconds());

    this.logicCheckbox.setValue(true);
    this.loopsCheckbox.setValue(true);
    this.mathCheckbox.setValue(true);
    this.textCheckbox.setValue(true);
    this.listsCheckbox.setValue(true);
    this.colorsCheckbox.setValue(true);
    this.variablesCheckbox.setValue(true);
    this.functionsCheckbox.setValue(true);
  }

  private checkFields(): boolean {
    let isCorrect: boolean = true;

    if (this.statementInput.invalid) {
      if (this.statementInput.errors.required) {
        this.showError('EXERCISE.empty-statement');
        isCorrect = false;
      }
    }

    if (this.yearInput.invalid) {
      if (this.yearInput.errors.required) {
        this.showError('EXERCISE.invalid-date');
        isCorrect = false;
      }
    } else {
      if (this.yearInput.value < 0) {
        this.showError('EXERCISE.invalid-date');
        isCorrect = false;
      }
    }

    if (this.monthInput.invalid) {
      if (this.monthInput.errors.required) {
        this.showError('EXERCISE.invalid-date');
        isCorrect = false;
      }
    } else {
      let monthAux = this.monthInput.value;
      monthAux--;
      if (monthAux < 1 || monthAux > 12) {
        this.showError('EXERCISE.invalid-date');
        isCorrect = false;
      }
    }

    if (this.dayInput.invalid) {
      if (this.dayInput.errors.required) {
        this.showError('EXERCISE.invalid-date');
        isCorrect = false;
      }
    } else {
      if (this.dayInput.value < 0 || this.dayInput.value > 31) {
        this.showError('EXERCISE.invalid-date');
        isCorrect = false;
      }
    }

    if (this.hourInput.invalid) {
      if (this.hourInput.errors.required) {
        this.showError('EXERCISE.invalid-date');
        isCorrect = false;
      }
    } else {
      if (this.hourInput.value < 0 || this.hourInput.value > 24) {
        this.showError('EXERCISE.invalid-date');
        isCorrect = false;
      }
    }

    if (this.minuteInput.invalid) {
      if (this.minuteInput.errors.required) {
        this.showError('EXERCISE.invalid-date');
        isCorrect = false;
      }
    } else {
      if (this.minuteInput.value < 0 || this.minuteInput.value > 60) {
        this.showError('EXERCISE.invalid-date');
        isCorrect = false;
      }
    }

    if (this.secondInput.invalid) {
      if (this.secondInput.errors.required) {
        this.showError('EXERCISE.invalid-date');
        isCorrect = false;
      }
    } else {
      if (this.secondInput.value < 0 || this.secondInput.value > 60) {
        this.showError('EXERCISE.invalid-date');
        isCorrect = false;
      }
    }

    return isCorrect;
  }

  saveExercise() {
    if (!this.checkFields()) {
      return;
    }

    let expirationDate = new Date();
    expirationDate.setFullYear(this.yearInput.value);
    expirationDate.setMonth(this.monthInput.value - 1);
    expirationDate.setDate(this.dayInput.value);
    expirationDate.setHours(this.hourInput.value);
    expirationDate.setMinutes(this.minuteInput.value);
    expirationDate.setSeconds(this.secondInput.value);

    let blocksCode: string;
    blocksCode = this.logicCheckbox.value ? 'Y' : 'N';
    blocksCode += this.loopsCheckbox.value ? 'Y' : 'N';
    blocksCode += this.mathCheckbox.value ? 'Y' : 'N';
    blocksCode += this.textCheckbox.value ? 'Y' : 'N';
    blocksCode += this.listsCheckbox.value ? 'Y' : 'N';
    blocksCode += this.colorsCheckbox.value ? 'Y' : 'N';
    blocksCode += this.variablesCheckbox.value ? 'Y' : 'N';
    blocksCode += this.functionsCheckbox.value ? 'Y' : 'N';

    this.exerciseService.createExercise({
      statementText: this.statementInput.value,
      groupId: this.groupId,
      userId: +this.authService.id,
      expirationDate: expirationDate,
      blocks: blocksCode
    }).subscribe(response => {
      this.router.navigate(['/exercise/' + response.id]);
      this.showSuccess('GROUP.exercise-created');
    }, error => {
      console.log(error);
      this.showError('EXERCISE.error-creating');
    });
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
      this.groupNameInput.setValue(this.group.groupName);

      response.users.forEach(element => {
        element.signUpDate = new Date(element.signUpDate).toLocaleString();
        this.users.push(element);
        this.showingUsers.push(element);
      });

      this.gotanswer = true;
      
      // Chat
      this.getMessages();
      
      // Exercises
      this.getExercises();
    }, error => {
      console.log(error);
      this.gotanswer = false;
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
          // Check if this user is already in the group
          if (!this.isMember(element.userName)) {
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

  exitGroup() {
    this.groupService.removeMember({userId: this.myUserId, groupId: this.group.id}).subscribe(response => {
      this.showSuccess('GROUP.exit-group-success');
      this.router.navigate(['/mygroups']);
    }, error => {
      console.log(error);
      this.showError('GROUP.error-exiting-group');
    });
  }

  // ------------
  // GROUP MANAGEMENT
  // ------------

  editGroupName() {
    this.editingGroupName = true;
  }

  save() {
    this.editingGroupName = false;

    if (this.groupNameInput.invalid) {
      if (this.groupNameInput.errors.required) {
        this.showError('GROUP.empty-group-name');
        return;
      }
    }

    if (this.group.groupName == this.groupNameInput.value) {
      return;
    }

    this.groupService.updateGroup({groupId: this.group.id, groupName: this.groupNameInput.value, chatEnable: this.group.chatEnable}).subscribe(response => {
      this.showSuccess('GROUP.group-name-updated');
      this.group = response;
      this.groupNameInput.setValue(this.group.groupName);
    }, error => {
      console.log(error);
      this.showError('GROUP.unk-error-updating-group');
    });
  }

  enableChat() {
    this.groupService.updateGroup({groupId: this.group.id, groupName: this.group.groupName, chatEnable: true}).subscribe(response => {
      this.showSuccess('GROUP.chat-enabled');
      this.group = response;
    }, error => {
      console.log(error);
      this.showError('GROUP.unk-error-updating-group');
    });
  }

  disableChat() {
    this.groupService.updateGroup({groupId: this.group.id, groupName: this.group.groupName, chatEnable: false}).subscribe(response => {
      this.showSuccess('GROUP.chat-disabled');
      this.group = response;
    }, error => {
      console.log(error);
      this.showError('GROUP.unk-error-updating-group');
    });
  }

  deleteGroup() {
    this.groupService.deleteGroup({groupId: this.groupId}).subscribe(response => {
      this.stopCheckingForNewMessages = true;
      this.showSuccess('Group ' + '\'' + this.group.groupName + '\'' + ' has been deleted');
      this.router.navigate(['/mygroups']);
    }, error => {
      console.log(error);
      this.showError('Group ' + '\'' + this.group.groupName + '\'' + ' could not been deleted');
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