package es.udc.paproject.backend.rest.dtos.UserDTOs;

import es.udc.paproject.backend.model.entities.Farm;
import es.udc.paproject.backend.model.entities.User;

import java.util.List;
import java.util.stream.Collectors;

import static es.udc.paproject.backend.rest.dtos.FarmDTOs.FarmConversor.toFarmDto;

public class UserConversor {
	
	private UserConversor() {}
	
	public final static UserDto toUserDto(User user) {
		return new UserDto(user.getId(), user.getUserName(), user.getDni(), user.getNss(), user.getIsSuspended(), user.getFirstName(),
				user.getLastName(), user.getEmail(), user.getRole().toString(), toFarmDto(user.getFarm()));
	}

	public final static List<UserDto> toUserDtos(List<User> users) {
		return users.stream().map(UserConversor::toUserDto).collect(Collectors.toList());
	}
	
	/*public final static User toUser(UserDto userDto) {
		
		return new User(userDto.getUserName(), userDto.getPassword(), userDto.getFirstName(), userDto.getLastName(),
			userDto.getEmail());
	}*/

	public final static User toUser(UserDto userDto, Farm farm) {
		return new User(userDto.getUserName(), userDto.getDni(), userDto.getNss(), userDto.getPassword(), userDto.getFirstName(), userDto.getLastName(),
				userDto.getEmail(), farm);
	}
	
	public final static AuthenticatedUserDto toAuthenticatedUserDto(String serviceToken, User user) {
		
		return new AuthenticatedUserDto(serviceToken, toUserDto(user));
		
	}

}
