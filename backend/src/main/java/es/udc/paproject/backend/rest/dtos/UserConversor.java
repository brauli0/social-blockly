package es.udc.paproject.backend.rest.dtos;

import java.util.ArrayList;
import java.util.List;

import es.udc.paproject.backend.model.entities.User;

public class UserConversor {

	private UserConversor() {}

	public final static UserDto toUserDto(User user) {
		return new UserDto(user.getId(), user.getUserName(), user.getFirstName(),
				user.getLastName(), user.getEmail(), user.getSignUpDate(), user.getLastLoginDate(),
				user.getRole().toString());
	}

	public final static List<UserDto> toUserDtos(List<User> users) {
		List<UserDto> userDtos = new ArrayList<>();

		for (User u : users) {
			userDtos.add(new UserDto(u.getId(), u.getUserName(), u.getFirstName(),
					u.getLastName(), u.getEmail(), u.getSignUpDate(), u.getLastLoginDate(),
					u.getRole().toString()));
		}

		return userDtos;
	}

	public final static User toUser(UserDto userDto) {

		return new User(userDto.getUserName(), userDto.getPassword(), userDto.getFirstName(), userDto.getLastName(),
			userDto.getEmail(), userDto.getSignUpDate(), userDto.getLastLoginDate(), userDto.getRole());
	}

	public final static AuthenticatedUserDto toAuthenticatedUserDto(String serviceToken, User user) {

		return new AuthenticatedUserDto(serviceToken, toUserDto(user));

	}

}
