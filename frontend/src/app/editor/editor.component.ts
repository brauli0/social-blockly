import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { FormControl } from '@angular/forms';

import { ProgramService } from '../program.service';
import { CommentService } from '../comment.service';
import { AuthService } from '../auth.service';
import { RatingService } from '../rating.service';

@Component({
  selector: 'app-editor',
  templateUrl: './editor.component.html',
  styleUrls: ['./editor.component.css']
})
export class EditorComponent implements OnInit {

  constructor(
    private programService: ProgramService,
    private commentService: CommentService,
    private authService: AuthService,
    private ratingService: RatingService,
    private toastr: ToastrService,
    private route: ActivatedRoute,
    private translate: TranslateService) {}

  myUserId: number;
  programId: number;

  program: {
    id: number,
    userId: number,
    userName: string,
    programName: string,
    programDesc: string,
    creationDate: string,
    updateDate: string,
    code: string,
    privateProgram: boolean
  };
  gotanswer: boolean = false;

  comments: {
    id: number,
    commentOrigId: number,
    userId: number,
    userName: string,
    programId: number,
    commentText: string,
    commentDate: string,
    replies: {
      id: number,
      commentOrigId: number,
      userId: number,
      userName: string,
      programId: number,
      commentText: string,
      commentDate: string
    }[]
  }[] = [];

  newCommentInput = new FormControl('');
  newReplyInput = new FormControl('');

  ratings: {
    userId: number,
    programId: number,
    rating: number
  }[] = [];
  averageRating: number;
  averageRatingBar: number;
  nVotes: number;
  iVoted: boolean = false;

  inputRating: number = 5;

  showReturnToProgramsButton: boolean = false;

  getComments() {
    this.comments = [];
    this.commentService.getCommentsByProgram(this.programId).subscribe(response => {
      response.reverse();
      response.forEach(element => {
        if (element.commentOrigId == undefined) {
          element.commentDate = new Date(element.commentDate).toLocaleString();
          this.comments.push({
            id: element.id,
            commentOrigId: element.commentOrigId,
            userId: element.userId,
            userName: element.userName,
            programId: element.programId,
            commentText: element.commentText,
            commentDate: element.commentDate,
            replies: []
          });
        }
      });

      //Replies
      response.forEach(element1 => {
        if (element1.commentOrigId != undefined) {
          this.comments.forEach(element2 => {
            if (element2.id == element1.commentOrigId) {
              element1.commentDate = new Date(element1.commentDate).toLocaleString();
              element2.replies.push(element1);
            }
          });
        }
      });
    }, error => {
      console.log(error);
      this.showError('EDITOR.error-comments');
    });
  }

  private round(x: number): number {
    return Math.round(x * 100)/100;
  }
  
  getRatings() {
    this.ratings = [];
    this.ratingService.getProgramRatings(this.programId).subscribe(response => {
      this.ratings = response;

      if (this.ratings.length > 0) {
        let count: number = 0;
        this.ratings.forEach(element => {
          count += element.rating;
          if (element.userId == this.myUserId) {
            this.iVoted = true;
            this.inputRating = element.rating;
          }
        });
        this.nVotes = this.ratings.length;
        this.averageRating = this.round(count / this.nVotes);
        this.averageRatingBar = this.averageRating * 10;
      } else {
        this.averageRating = undefined;
        this.averageRatingBar = 0;
        this.nVotes = 0;
      }

    }, error => {
      console.log(error);
      this.showError('EDITOR.error-ratings');
    });
  }

  ngOnInit() {
    this.myUserId = +this.authService.id;
    this.programId = +this.route.snapshot.paramMap.get('id');
    
    this.programService.getProgram(this.programId).subscribe(response => {
      this.program = response;
      this.getComments();
      this.getRatings();
      if (response.userId == this.myUserId) {
        this.showReturnToProgramsButton = true;
      }
      this.gotanswer = true;
    }, error => {
      console.log(error);
      this.gotanswer = false;
    });
  }

  update(code: string) {
    let program = {
      userId: +this.authService.id,
      programId: this.program.id,
      programName: this.program.programName,
      programDesc: this.program.programDesc,
      code: code
    };
    this.programService.updateProgram(program).subscribe(response => {
      this.showSuccess('BLOCKLY.program-updated');
    }, error => {
      console.log(error);
      this.showError('BLOCKLY.error-updating');
    });
  }

  createComment() {
    let commentText:string = this.newCommentInput.value;
    this.newCommentInput.setValue('');

    if (commentText.length > 0) {
      this.commentService.createComment({
        userId: this.myUserId,
        programId: this.programId,
        commentText: commentText}
        ).subscribe(response => {
          
          this.getComments();
  
      }, error => {
        console.log(error);
        this.showError('EDITOR.error-create-comment');
      });
    }
  }

  createReply(commentOrigId: number) {
    let commentText:string = this.newReplyInput.value;
    this.newReplyInput.setValue('');

    if (commentText.length > 0) {
      this.commentService.createReply({
        commentOrigId: commentOrigId,
        userId: this.myUserId,
        programId: this.programId,
        commentText: commentText}
        ).subscribe(response => {
          
          this.getComments();
          
      }, error => {
        console.log(error);
        this.showError('EDITOR.error-create-reply');
      });
    }
  }

  deleteComment(commentId: number) {
    this.commentService.deleteComment(commentId).subscribe(response => {
        this.showSuccess('EDITOR.comment-deleted');
        this.getComments();
    }, error => {
      console.log(error);
      this.showError('EDITOR.error-delete-comment');
    });
  }

  sendRating() {
    this.ratingService.createRating({
      userId: this.myUserId,
      programId: this.programId,
      rating: this.inputRating
    }).subscribe(response => {
      this.ratings.push(response);

      let count: number = 0;
      this.ratings.forEach(element => {
        count += element.rating;
      });
      this.nVotes = this.ratings.length;
      this.averageRating = this.round(count / this.nVotes);
      this.averageRatingBar = this.averageRating * 10;

      this.iVoted = true;

      this.showSuccess('EDITOR.rating-sent');
    }, error => {
      console.log(error);
      this.showError('EDITOR.error-send-rating');
    });
  }

  deleteRating() {
    this.ratingService.deleteRating({
      userId: this.myUserId,
      programId: this.programId
    }).subscribe(response => {
      this.showSuccess('EDITOR.rating-deleted');
      this.getRatings();
      this.iVoted = false;
      this.inputRating = 5;
    }, error => {
      console.log(error);
      this.showError('EDITOR.error-send-rating');
    });
  }

  inputRangeChange(event: any) {
    this.inputRating = event;
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
