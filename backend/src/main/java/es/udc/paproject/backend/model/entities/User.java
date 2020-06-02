package es.udc.paproject.backend.model.entities;

import java.util.Calendar;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class User {

	public enum RoleType {STUDENT, TEACHER};

	private Long id;
	private String userName;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private Calendar signUpDate;
	private Calendar lastLoginDate;
	private List<UsersGroupRel> groupsRel;
	private RoleType role;

	public User() {}

	public User(String userName, String password, String firstName,
			String lastName, String email, Calendar signUpDate, String role) {

		this.userName = userName;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.signUpDate = signUpDate;
		switch (role) {
			case "STUDENT": this.role = RoleType.STUDENT;
				break;
			case "TEACHER": this.role = RoleType.TEACHER;
				break;
		}

	}

	public User(String userName, String password, String firstName,
			String lastName, String email, Calendar signUpDate, Calendar lastLoginDate, String role) {

		this.userName = userName;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.signUpDate = signUpDate;
		this.lastLoginDate = lastLoginDate;
		switch (role) {
			case "STUDENT": this.role = RoleType.STUDENT;
				break;
			case "TEACHER": this.role = RoleType.TEACHER;
				break;
		}

	}

	public User(Long id, String userName, String password, String firstName, String lastName, String email,
			Calendar lastLoginDate, Calendar signUpDate, List<UsersGroupRel> groupsRel, RoleType role) {
		this.id = id;
		this.userName = userName;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.signUpDate = signUpDate;
		this.lastLoginDate = lastLoginDate;
		this.groupsRel = groupsRel;
		this.role = role;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@OneToMany(mappedBy = "user")
	public List<UsersGroupRel> getGroupsRel() {
		return groupsRel;
	}

	public void setGroupsRel(List<UsersGroupRel> groupsRel) {
		this.groupsRel = groupsRel;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Calendar getSignUpDate() {
		return signUpDate;
	}

	public void setSignUpDate(Calendar signUpDate) {
		this.signUpDate = signUpDate;
	}

	public Calendar getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Calendar lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public RoleType getRole() {
		return role;
	}

	public void setRole(RoleType role) {
		this.role = role;
	}

}
