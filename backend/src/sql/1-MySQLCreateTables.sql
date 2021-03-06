DROP TABLE Rating;
DROP TABLE ChatMessage;
DROP TABLE Comment;
DROP TABLE Shared;
DROP TABLE Program;
DROP TABLE Exercise;
DROP TABLE UsersGroupRel;
DROP TABLE UsersGroup;
DROP TABLE User;

CREATE TABLE User (
  id BIGINT NOT NULL AUTO_INCREMENT,
  userName VARCHAR(60) COLLATE latin1_bin NOT NULL,
  password VARCHAR(60) NOT NULL, 
  firstName VARCHAR(60) NOT NULL,
  lastName VARCHAR(60) NOT NULL, 
  email VARCHAR(60) NOT NULL,
  signUpDate TIMESTAMP NOT NULL,
  lastLoginDate TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  role TINYINT NOT NULL,
  CONSTRAINT UserPK PRIMARY KEY (id),
  CONSTRAINT UserNameUniqueKey UNIQUE (userName)
) ENGINE = InnoDB;

CREATE TABLE UsersGroup (
  id BIGINT NOT NULL AUTO_INCREMENT,
  groupName VARCHAR(100),
  creationDate TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  chatEnable BOOLEAN DEFAULT TRUE,
  CONSTRAINT GroupPK PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE TABLE UsersGroupRel (
  userId BIGINT NOT NULL,
  groupId BIGINT NOT NULL,
  isAdmin BOOLEAN DEFAULT FALSE,
  accessDate TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT GroupRelPK PRIMARY KEY (userId, groupId),
  CONSTRAINT User_FK FOREIGN KEY (userId)
    REFERENCES User(id)
    ON DELETE CASCADE,
  CONSTRAINT Group_FK FOREIGN KEY (groupId)
    REFERENCES UsersGroup(id)
    ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE Exercise (
  id BIGINT NOT NULL AUTO_INCREMENT,
  statementText VARCHAR(200),
  groupId BIGINT NOT NULL,
  userId BIGINT NOT NULL,
  creationDate TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  expirationDate TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  blocks VARCHAR(10),
  CONSTRAINT ExercisePK PRIMARY KEY (id),
  CONSTRAINT ExerciseGroup_FK FOREIGN KEY (groupId)
    REFERENCES UsersGroup(id)
    ON DELETE CASCADE,
  CONSTRAINT ExerciseUser_FK FOREIGN KEY (userId)
    REFERENCES User(id)
    ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE Program (
	id BIGINT NOT NULL AUTO_INCREMENT,
	userId BIGINT NOT NULL,
  exerciseId BIGINT DEFAULT NULL,
	programName VARCHAR(60),
  programDesc VARCHAR(2000),
  creationDate TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updateDate TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  code VARCHAR(30000),
  privateProgram BIT NOT NULL DEFAULT 0,
	CONSTRAINT ProgramPK PRIMARY KEY (id),
	CONSTRAINT Program_UserFK FOREIGN KEY (userId)
		REFERENCES User(id)
		ON DELETE CASCADE,
  CONSTRAINT ProgramExercise_FK FOREIGN KEY (exerciseId)
    REFERENCES Exercise(id)
    ON DELETE SET NULL
) ENGINE = InnoDB;

CREATE TABLE Shared (
  userId BIGINT NOT NULL,
  programId BIGINT NOT NULL,
  CONSTRAINT SharedPK PRIMARY KEY (userId, programId),
  CONSTRAINT SharedUser_FK FOREIGN KEY (userId)
    REFERENCES User(id)
    ON DELETE CASCADE,
  CONSTRAINT SharedProgram_FK FOREIGN KEY (programId)
    REFERENCES Program(id)
    ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE Comment (
  id BIGINT NOT NULL AUTO_INCREMENT,
  userId BIGINT NOT NULL,
  programId BIGINT NOT NULL,
  commentOrigId BIGINT DEFAULT NULL,
  commentText VARCHAR(100),
  commentDate TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT CommentPK PRIMARY KEY (id),
  CONSTRAINT CommentUser_FK FOREIGN KEY (userId)
    REFERENCES User(id)
    ON DELETE CASCADE,
  CONSTRAINT CommentProgram_FK FOREIGN KEY (programId)
    REFERENCES Program(id)
    ON DELETE CASCADE,
  CONSTRAINT CommentOrig_FK FOREIGN KEY (commentOrigId)
    REFERENCES Comment(id)
    ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE ChatMessage (
  id BIGINT NOT NULL AUTO_INCREMENT,
  groupId BIGINT NOT NULL,
  userId BIGINT NOT NULL,
  messageText VARCHAR(100) NOT NULL,
  messageDate TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT ChatPK PRIMARY KEY (id),
  CONSTRAINT ChatGroup_FK FOREIGN KEY (groupId)
    REFERENCES UsersGroup(id)
    ON DELETE CASCADE,
  CONSTRAINT ChatUser_FK FOREIGN KEY (userId)
    REFERENCES User(id)
    ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE Rating (
  userId BIGINT NOT NULL,
  programId BIGINT NOT NULL,
  rating DECIMAL(4,2),
  CONSTRAINT RatingPK PRIMARY KEY (userId, programId),
  CONSTRAINT RatingUser_FK FOREIGN KEY (userId)
    REFERENCES User(id)
    ON DELETE CASCADE,
  CONSTRAINT RatingProgram_FK FOREIGN KEY (programId)
    REFERENCES Program(id)
    ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE INDEX UserIndexByUserName ON User (userName);