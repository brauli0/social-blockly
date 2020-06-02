import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormControl, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';

import { ExerciseService }  from '../exercise.service';
import { GroupService }  from '../group.service';
import { UserService }  from '../user.service';
import { ProgramService }  from '../program.service';
import { AuthService }  from '../auth.service';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-exercise',
  templateUrl: './exercise.component.html',
  styleUrls: ['./exercise.component.css']
})
export class ExerciseComponent implements OnInit {

  constructor(
    private toastr: ToastrService,
    private exerciseService: ExerciseService,
    private groupService: GroupService,
    private userService: UserService,
    private programService: ProgramService,
    private authService: AuthService,
    private translate: TranslateService,
    private route: ActivatedRoute,
    private router: Router) {}


  exerciseId: number;
  iAmAdmin: boolean = false;

  exercise: {
    id: number,
	  statementText: string,
	  groupId: number,
	  userId: number,
    creationDate: string,
    creationDateString: string,
    expirationDate: string,
    expirationDateString: string
  } = {
    id: 0,
    statementText: '',
    groupId: 0,
    userId: 0,
    creationDate: '',
    creationDateString: '',
    expirationDate: '',
    expirationDateString: ''
  };

  group: {
    id: number,
    groupName: string,
    creationDate: string
  };
  author: {
    id: number,
    userName: string,
    firstName: string,
    lastName: string,
    email: string,
    signUpDate: string,
    role: string
  };

  solutions: {
    id: number,
    userId: number,
    programName: string,
    programDesc: string,
    creationDate: string,
    updateDate: string,
    code: string,
    privateProgram: boolean
  }[] = [];

  pageShowingSolutions: {
    id: number,
    userId: number,
    programName: string,
    programDesc: string,
    creationDate: string,
    updateDate: string,
    code: string,
    privateProgram: boolean
  }[] = [];

  gotanswer: boolean = false;

  statementInput = new FormControl('', Validators.required);

  yearInput = new FormControl('', Validators.required);
  monthInput = new FormControl('', Validators.required);
  dayInput = new FormControl('', Validators.required);
  hourInput = new FormControl('', Validators.required);
  minuteInput = new FormControl('', Validators.required);
  secondInput = new FormControl('', Validators.required);

  pageConfig: {
    elementsByPage: number,
    size: number,
    hasElements: boolean,
    firstShown: number,
    lastShown: number
  } = {elementsByPage: 0, size: 0, hasElements: false, firstShown: 0, lastShown: 0};

  configPagination() {
    this.pageConfig.elementsByPage = environment.elementsByPage;
    this.pageConfig.size = this.solutions.length;
    this.pageConfig.hasElements = this.pageConfig.size > 0;
    if (this.pageConfig.hasElements) {
      this.pageConfig.firstShown = 1;
      this.pageConfig.lastShown =
        this.pageConfig.size > this.pageConfig.elementsByPage ? this.pageConfig.elementsByPage : this.pageConfig.size;
    } else {
      this.pageConfig.firstShown = 0;
      this.pageConfig.lastShown = 0;
    }
    
    this.pageShowingSolutions = [];
    if (this.pageConfig.hasElements) {
      for (let i = this.pageConfig.firstShown - 1; i < this.pageConfig.lastShown; i++) {
        this.pageShowingSolutions.push(this.solutions[i]);
      }
    }
  }

  previousPage() {
    if (this.pageConfig.firstShown < 2) {
      return;
    }

    this.pageConfig.lastShown = this.pageConfig.firstShown - 1;
    this.pageConfig.firstShown -= this.pageConfig.elementsByPage;

    this.pageShowingSolutions = [];
    for (let i = this.pageConfig.firstShown - 1; i < this.pageConfig.lastShown; i++) {
      this.pageShowingSolutions.push(this.solutions[i]);
    }
  }

  nextPage() {
    if (this.pageConfig.lastShown == this.pageConfig.size) {
      return;
    }

    this.pageConfig.firstShown = this.pageConfig.lastShown + 1;
    
    if ((this.pageConfig.lastShown + this.pageConfig.elementsByPage) < this.pageConfig.size) {
      this.pageConfig.lastShown += this.pageConfig.elementsByPage;
    } else {
      this.pageConfig.lastShown = this.pageConfig.size;
    }

    this.pageShowingSolutions = [];
    for (let i = this.pageConfig.firstShown - 1; i < this.pageConfig.lastShown; i++) {
      this.pageShowingSolutions.push(this.solutions[i]);
    }
  }

  ngOnInit() {
    this.exerciseId = +this.route.snapshot.paramMap.get('id');

    this.exerciseService.getExercise(this.exerciseId).subscribe(exercise => {
      this.exercise.id = exercise.id;
      this.exercise.statementText = exercise.statementText;
      this.exercise.groupId = exercise.groupId;
      this.exercise.userId = exercise.userId;
      this.exercise.creationDate = exercise.creationDate;
      this.exercise.creationDateString = new Date(exercise.creationDate).toLocaleString();
      this.exercise.expirationDate = exercise.expirationDate;
      this.exercise.expirationDateString = new Date(exercise.expirationDate).toLocaleString();

      this.groupService.getGroup(this.exercise.groupId).subscribe(group => {
        this.group = group;
        this.iAmAdmin = group.iAmAdmin;
      }, error => {
        console.log(error);
        this.showError('EXERCISE.unk-error');
      });

      this.userService.getUserInfo(exercise.userId.toString()).subscribe(user => {
        let getAuthor = function(userResponse: any): {
          id: number,
          userName: string,
          firstName: string,
          lastName: string,
          email: string,
          signUpDate: string,
          role: string
        } {return userResponse};

        this.author = getAuthor(user);
      }, error => {
        console.log(error);
        this.showError('EXERCISE.unk-error');
      });

      this.gotanswer = true;

      this.programService.getProgramsByExercise(this.exerciseId).subscribe(response => {
        response.reverse();
        response.forEach(element => {
          this.solutions.push({
            id: element.id,
            userId: element.userId,
            programName: element.programName,
            programDesc: element.programDesc,
            creationDate: new Date(element.creationDate).toLocaleString(),
            updateDate: new Date(element.updateDate).toLocaleString(),
            code: element.code,
            privateProgram: element.privateProgram
          });
        });

        this.configPagination();
      }, error => {
        console.log(error);
        this.showError('EXERCISE.unk-error');
      });

    }, error => {
      console.log(error);
    });
  }

  setModal(statement: string) {
    this.statementInput.setValue(statement);
    this.yearInput.setValue(new Date(this.exercise.expirationDate).getFullYear());
    this.monthInput.setValue(new Date(this.exercise.expirationDate).getMonth() + 1);
    this.dayInput.setValue(new Date(this.exercise.expirationDate).getDate());
    this.hourInput.setValue(new Date(this.exercise.expirationDate).getHours());
    this.minuteInput.setValue(new Date(this.exercise.expirationDate).getMinutes());
    this.secondInput.setValue(new Date(this.exercise.expirationDate).getSeconds());
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
      if (this.monthInput.value < 1 || this.monthInput.value > 12) {
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

  areThereChanges() {
    let changes: boolean = false;

    if (this.statementInput.value != this.exercise.statementText) {
      changes = true;
    }

    if (this.yearInput.value != new Date(this.exercise.expirationDate).getFullYear()) {
      changes = true;
    }

    if (this.monthInput.value != new Date(this.exercise.expirationDate).getMonth()) {
      changes = true;
    }

    if (this.dayInput.value != new Date(this.exercise.expirationDate).getDate()) {
      changes = true;
    }

    if (this.hourInput.value != new Date(this.exercise.expirationDate).getHours()) {
      changes = true;
    }

    if (this.minuteInput.value != new Date(this.exercise.expirationDate).getMinutes()) {
      changes = true;
    }

    if (this.secondInput.value != new Date(this.exercise.expirationDate).getSeconds()) {
      changes = true;
    }

    return changes;
  }

  editExercise() {
    if (!this.checkFields()) {
      return;
    }

    if(!this.areThereChanges()) {
      return;
    }

    let expirationDate = new Date();
    expirationDate.setFullYear(this.yearInput.value);
    expirationDate.setMonth(this.monthInput.value - 1);
    expirationDate.setDate(this.dayInput.value);
    expirationDate.setHours(this.hourInput.value);
    expirationDate.setMinutes(this.minuteInput.value);
    expirationDate.setSeconds(this.secondInput.value);

    this.exerciseService.updateExercise({
      exerciseId: this.exerciseId,
      statementText: this.statementInput.value,
      expirationDate: expirationDate
    }).subscribe(response => {
      this.exercise.id = response.id;
      this.exercise.statementText = response.statementText;
      this.exercise.groupId = response.groupId;
      this.exercise.userId = response.userId;
      this.exercise.creationDate = response.creationDate;
      this.exercise.creationDateString = new Date(response.creationDate).toLocaleString();
      this.exercise.expirationDate = response.expirationDate;
      this.exercise.expirationDateString = new Date(response.expirationDate).toLocaleString();
      this.showSuccess('EXERCISE.updated');
    }, error => {
      console.log(error);
      this.showError('EXERCISE.error-updating');
    });
  }

  deleteExercise() {
    this.exerciseService.deleteExercise(this.exerciseId).subscribe(response => {
      this.showSuccess('EXERCISE.deleted');
      this.router.navigate(['/group/' + this.exercise.groupId]);
    }, error => {
      console.log(error);
      this.showError('EXERCISE.error-deleting')
    });
  }

  newSolution() {
    let program = {
      userId: +this.authService.id,
      exerciseId: this.exerciseId,
      programName: 'Exercise solution',
      programDesc: 'Statement: ' +  this.exercise.statementText,
      code: '',
      privateProgram: true
    }
    this.programService.createSolution(program).subscribe(response => {
      this.showSuccess('PROGRAMS.created');
      this.router.navigate(['/program/' + response.id]);
    }, error => {
      console.log(error);
      this.showError('PROGRAMS.error-creating');
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
