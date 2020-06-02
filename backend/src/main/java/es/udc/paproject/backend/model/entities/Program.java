package es.udc.paproject.backend.model.entities;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Program {
    private Long id;
    private User user;
    private Exercise exercise;
    private String programName;
    private String programDesc;
    private Calendar creationDate;
    private Calendar updateDate;
    private String code;
    private Boolean privateProgram;

    public Program() {}

	public Program(User user, String programName, String programDesc,
			String code, Boolean privateProgram) {
		this.user = user;
		this.programName = programName;
		this.programDesc = programDesc;
		this.code = code;
		this.privateProgram = privateProgram;
	}

	public Program(User user, Exercise exercise, String programName, String programDesc,
			String code, Boolean privateProgram) {
		this.user = user;
		this.exercise = exercise;
		this.programName = programName;
		this.programDesc = programDesc;
		this.code = code;
		this.privateProgram = privateProgram;
	}

	public Program(Long id, User user, String programName, String programDesc,
			Calendar creationDate, Calendar updateDate, String code, Boolean privateProgram) {
		this.id = id;
		this.user = user;
		this.programName = programName;
		this.programDesc = programDesc;
		this.creationDate = creationDate;
		this.updateDate = updateDate;
		this.code = code;
		this.privateProgram = privateProgram;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name="userId")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="exerciseId")
	public Exercise getExercise() {
		return exercise;
	}

	public void setExercise(Exercise exercise) {
		this.exercise = exercise;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public String getProgramDesc() {
		return programDesc;
	}

	public void setProgramDesc(String programDesc) {
		this.programDesc = programDesc;
	}

	public Calendar getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Calendar creationDate) {
		this.creationDate = creationDate;
	}

	public Calendar getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Calendar updateDate) {
		this.updateDate = updateDate;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Boolean getPrivateProgram() {
		return privateProgram;
	}

	public void setPrivateProgram(Boolean privateProgram) {
		this.privateProgram = privateProgram;
	}
}
