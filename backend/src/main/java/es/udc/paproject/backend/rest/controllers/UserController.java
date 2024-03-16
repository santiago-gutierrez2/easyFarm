package es.udc.paproject.backend.rest.controllers;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import es.udc.paproject.backend.model.entities.Farm;
import es.udc.paproject.backend.model.entities.FarmDao;
import es.udc.paproject.backend.model.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.udc.paproject.backend.model.entities.User;
import es.udc.paproject.backend.model.services.UserService;
import es.udc.paproject.backend.rest.common.ErrorsDto;
import es.udc.paproject.backend.rest.common.JwtGenerator;
import es.udc.paproject.backend.rest.common.JwtInfo;
import es.udc.paproject.backend.rest.dtos.UserDTOs.AuthenticatedUserDto;
import es.udc.paproject.backend.rest.dtos.UserDTOs.ChangePasswordParamsDto;
import es.udc.paproject.backend.rest.dtos.UserDTOs.LoginParamsDto;
import es.udc.paproject.backend.rest.dtos.UserDTOs.UserDto;

import static es.udc.paproject.backend.rest.dtos.UserDTOs.UserConversor.*;

@RestController
@RequestMapping("/users")
public class UserController {
	
	private final static String INCORRECT_LOGIN_EXCEPTION_CODE = "project.exceptions.IncorrectLoginException";
	private final static String INCORRECT_PASSWORD_EXCEPTION_CODE = "project.exceptions.IncorrectPasswordException";
	private final static String FARM_DOESNT_EXIST_CODE = "project.exception.FarmDoesntExistException";
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private JwtGenerator jwtGenerator;
	
	@Autowired
	private UserService userService;

	@Autowired
	private FarmDao farmDao;
	
	@ExceptionHandler(IncorrectLoginException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ErrorsDto handleIncorrectLoginException(IncorrectLoginException exception, Locale locale) {
		
		String errorMessage = messageSource.getMessage(INCORRECT_LOGIN_EXCEPTION_CODE, null,
				INCORRECT_LOGIN_EXCEPTION_CODE, locale);

		return new ErrorsDto(errorMessage);
		
	}

	@ExceptionHandler(FarmDoesntExistException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ErrorsDto handleFarmDoesntExistException(FarmDoesntExistException exception, Locale locale) {
		String errorMessage = messageSource.getMessage(FARM_DOESNT_EXIST_CODE, null,
				FARM_DOESNT_EXIST_CODE, locale);

		return new ErrorsDto(errorMessage);
	}
	
	@ExceptionHandler(IncorrectPasswordException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ErrorsDto handleIncorrectPasswordException(IncorrectPasswordException exception, Locale locale) {
		
		String errorMessage = messageSource.getMessage(INCORRECT_PASSWORD_EXCEPTION_CODE, null,
				INCORRECT_PASSWORD_EXCEPTION_CODE, locale);

		return new ErrorsDto(errorMessage);
		
	}

	@PostMapping("/createEmployee")
	public UserDto createEmployee( @RequestAttribute Long userId,
		@Validated({UserDto.AllValidations.class}) @RequestBody UserDto userDto) throws DuplicateInstanceException, FarmDoesntExistException {

		Optional<Farm> farm = farmDao.findById(userDto.getFarm().getId());

		if (farm.isPresent()) {
			User user = toUser(userDto, farm.get());
			userService.signUp(user);

			return toUserDto(user);

			/*URI location = ServletUriComponentsBuilder
					.fromCurrentRequest().path("/{id}")
					.buildAndExpand(user.getId()).toUri();

			return ResponseEntity.created(location).body(toAuthenticatedUserDto(generateServiceToken(user), user));*/
		} else {
			throw new FarmDoesntExistException();
		}
	}
	
	@PostMapping("/login")
	public AuthenticatedUserDto login(@Validated @RequestBody LoginParamsDto params)
		throws IncorrectLoginException {
		
		User user = userService.login(params.getUserName(), params.getPassword());
			
		return toAuthenticatedUserDto(generateServiceToken(user), user);
		
	}
	
	@PostMapping("/loginFromServiceToken")
	public AuthenticatedUserDto loginFromServiceToken(@RequestAttribute Long userId, 
		@RequestAttribute String serviceToken) throws InstanceNotFoundException {
		
		User user = userService.loginFromId(userId);
		
		return toAuthenticatedUserDto(serviceToken, user);
		
	}

	@PutMapping("/{id}")
	public UserDto updateProfile(@RequestAttribute Long userId, @PathVariable Long id,
		@Validated({UserDto.UpdateValidations.class}) @RequestBody UserDto userDto) 
		throws InstanceNotFoundException, PermissionException {
				
		if (!id.equals(userId)) {
			throw new PermissionException();
		}
		
		return toUserDto(userService.updateProfile(id, userDto.getFirstName(), userDto.getLastName(),
			userDto.getEmail()));
		
	}
	
	@PostMapping("/{id}/changePassword")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void changePassword(@RequestAttribute Long userId, @PathVariable Long id,
		@Validated @RequestBody ChangePasswordParamsDto params)
		throws PermissionException, InstanceNotFoundException, IncorrectPasswordException {
		
		if (!id.equals(userId)) {
			throw new PermissionException();
		}
		
		userService.changePassword(id, params.getOldPassword(), params.getNewPassword());
		
	}

	@DeleteMapping("/{employeeId}/deleteEmployee")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteEmployee(@RequestAttribute Long userId, @PathVariable Long employeeId)
		throws PermissionException, InstanceNotFoundException {

		if (userId.equals(employeeId)) {
			throw new PermissionException();
		}

		userService.deleteEmployee(employeeId);

	}

	@GetMapping("/getEmployees")
	public List<UserDto> getEmployees(@RequestAttribute Long userId) throws InstanceNotFoundException {

		List<User> employees =  userService.getEmployees(userId);
		return toUserDtos(employees);
	}

	@GetMapping("/getActiveEmployees")
	public List<UserDto> getActiveEmployees(@RequestAttribute Long userId) throws InstanceNotFoundException {
		List<User> employees = userService.getActiveEmployees(userId);
		return toUserDtos(employees);
	}

	@PutMapping("/suspend/{employeeId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void suspendEmployee(@RequestAttribute Long userId, @PathVariable Long employeeId)
			throws PermissionException, InstanceNotFoundException {
		if (userId.equals(employeeId)) {
			throw new PermissionException();
		}
		userService.suspendEmployee(employeeId);
	}

	@PutMapping("/unsuspend/{employeeId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void unsuspendEmployee(@RequestAttribute Long userId, @PathVariable Long employeeId)
			throws PermissionException, InstanceNotFoundException {
		if (userId.equals(employeeId)) {
			throw new PermissionException();
		}
		userService.unsuspendEmployee(employeeId);
	}

	private String generateServiceToken(User user) {
		
		JwtInfo jwtInfo = new JwtInfo(user.getId(), user.getUserName(), user.getRole().toString());
		
		return jwtGenerator.generate(jwtInfo);
		
	}
	
}
