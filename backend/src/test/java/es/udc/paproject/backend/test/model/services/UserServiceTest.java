package es.udc.paproject.backend.test.model.services;

import es.udc.paproject.backend.model.entities.Farm;
import es.udc.paproject.backend.model.entities.FarmDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import es.udc.paproject.backend.model.exceptions.DuplicateInstanceException;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.entities.User;
import es.udc.paproject.backend.model.exceptions.IncorrectLoginException;
import es.udc.paproject.backend.model.exceptions.IncorrectPasswordException;
import es.udc.paproject.backend.model.services.UserService;
import org.springframework.web.context.request.FacesRequestAttributes;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserServiceTest {
	
	private final Long NON_EXISTENT_ID = (long) -1;
	
	@Autowired
	private UserService userService;
	@Autowired
	private FarmDao farmDao;


	private User createUser(String userName, Farm farm) {
		return new User(userName,"55043207K", "17000058787", "password", "firstName",
				"lastName", userName + "@" + userName + ".com", farm);
	}
	
	@Test
	public void testSignUpAndLoginFromId() throws DuplicateInstanceException, InstanceNotFoundException {
		Farm farm = farmDao.findById(1L).get();
		
		User user = createUser("user", farm);
		
		userService.signUp(user);
		
		User loggedInUser = userService.loginFromId(user.getId());
		
		assertEquals(user, loggedInUser);
		assertEquals(User.RoleType.EMPLOYEE, user.getRole());
		
	}
	
	@Test
	public void testSignUpDuplicatedUserName() throws DuplicateInstanceException {
		Farm farm = farmDao.findById(1L).get();
		User user = createUser("user", farm);
		
		userService.signUp(user);
		assertThrows(DuplicateInstanceException.class, () -> userService.signUp(user));
		
	}
	
	@Test
	public void testLoginFromNonExistentId() {
		assertThrows(InstanceNotFoundException.class, () -> userService.loginFromId(NON_EXISTENT_ID));
	}
	
	@Test
	public void testLogin() throws DuplicateInstanceException, IncorrectLoginException {
		Farm farm = farmDao.findById(1L).get();
		User user = createUser("user", farm);
		String clearPassword = user.getPassword();
				
		userService.signUp(user);
		
		User loggedInUser = userService.login(user.getUserName(), clearPassword);
		
		assertEquals(user, loggedInUser);
		
	}
	
	@Test
	public void testLoginWithIncorrectPassword() throws DuplicateInstanceException {
		Farm farm = farmDao.findById(1L).get();
		User user = createUser("user", farm);
		String clearPassword = user.getPassword();
		
		userService.signUp(user);
		assertThrows(IncorrectLoginException.class, () ->
			userService.login(user.getUserName(), 'X' + clearPassword));
		
	}
	
	@Test
	public void testLoginWithNonExistentUserName() {
		assertThrows(IncorrectLoginException.class, () -> userService.login("X", "Y"));
	}
	
	@Test
	public void testUpdateProfile() throws InstanceNotFoundException, DuplicateInstanceException {
		Farm farm = farmDao.findById(1L).get();
		User user = createUser("user", farm);
		
		userService.signUp(user);
		
		user.setFirstName('X' + user.getFirstName());
		user.setLastName('X' + user.getLastName());
		user.setEmail('X' + user.getEmail());
		
		userService.updateProfile(user.getId(), 'X' + user.getFirstName(), 'X' + user.getLastName(),
			'X' + user.getEmail());
		
		User updatedUser = userService.loginFromId(user.getId());
		
		assertEquals(user, updatedUser);
	}
	
	@Test
	public void testUpdateProfileWithNonExistentId() {
		assertThrows(InstanceNotFoundException.class, () ->
			userService.updateProfile(NON_EXISTENT_ID, "X", "X", "X"));
	}
	
	@Test
	public void testChangePassword() throws DuplicateInstanceException, InstanceNotFoundException,
		IncorrectPasswordException, IncorrectLoginException {
		Farm farm = farmDao.findById(1L).get();
		User user = createUser("user", farm);
		String oldPassword = user.getPassword();
		String newPassword = 'X' + oldPassword;
		
		userService.signUp(user);
		userService.changePassword(user.getId(), oldPassword, newPassword);
		userService.login(user.getUserName(), newPassword);
		
	}
	
	@Test
	public void testChangePasswordWithNonExistentId() {
		assertThrows(InstanceNotFoundException.class, () ->
			userService.changePassword(NON_EXISTENT_ID, "X", "Y"));
	}
	
	@Test
	public void testChangePasswordWithIncorrectPassword() throws DuplicateInstanceException {
		Farm farm = farmDao.findById(1L).get();
		User user = createUser("user", farm);
		String oldPassword = user.getPassword();
		String newPassword = 'X' + oldPassword;
		
		userService.signUp(user);
		assertThrows(IncorrectPasswordException.class, () ->
			userService.changePassword(user.getId(), 'Y' + oldPassword, newPassword));
		
	}

	@Test
	public void testDeleteEmployee() throws InstanceNotFoundException, DuplicateInstanceException{
		Farm farm = farmDao.findById(1L).get();
		User user = createUser("user", farm);
		userService.signUp(user);
		User loggedInUser = userService.loginFromId(user.getId());

		userService.deleteEmployee(loggedInUser.getId());

        assertTrue(loggedInUser.getIsEliminated());
	}

	@Test
	public void testDeleteWithNoExistingUser() {
		assertThrows(InstanceNotFoundException.class, () -> userService.deleteEmployee(-1L));
	}

	@Test
	public void testGetEmployees() throws InstanceNotFoundException {
		User user = userService.loginFromId(1L);
		List<User> employees = userService.getEmployees(user.getId());
		assertEquals(employees.size(), 2);
	}

	@Test
	public void testGetEmployeesWithNoExistingUser() {
		assertThrows(InstanceNotFoundException.class, () -> userService.getEmployees(-1L));
	}

	@Test
	public void testGetActiveEmployees() throws InstanceNotFoundException, DuplicateInstanceException {
		Farm farm = farmDao.findById(1L).get();
		User user = createUser("user", farm);
		userService.signUp(user);

		// suspend user
		userService.suspendEmployee(user.getId());
		// get active employees
		List<User> activeEmployees = userService.getActiveEmployees(1L);

		assertEquals(activeEmployees.size(), 2);
	}

	@Test
	public void testGetActiveEmployeesWithNoExistingUser() {
		assertThrows(InstanceNotFoundException.class, () -> userService.getActiveEmployees(-1L));
	}

	@Test
	public void suspendEmployee() throws InstanceNotFoundException, DuplicateInstanceException {
		Farm farm = farmDao.findById(1L).get();
		User user = createUser("user", farm);
		userService.signUp(user);

		// suspend user
		userService.suspendEmployee(user.getId());
		// get Employee
		User suspendedEmployee = userService.loginFromId(user.getId());

        assertTrue(suspendedEmployee.getIsSuspended());
	}

	@Test
	public void suspendEmployeeWithNoExistingUser() {
		assertThrows(InstanceNotFoundException.class, () -> userService.suspendEmployee(-1L));
	}

	@Test
	public void unsuspendEmployee() throws InstanceNotFoundException, DuplicateInstanceException {
		Farm farm = farmDao.findById(1L).get();
		User user = createUser("user", farm);
		userService.signUp(user);

		// suspend employee
		userService.suspendEmployee(user.getId());
		// unsuspend employee
		userService.unsuspendEmployee(user.getId());
		// get Employee
		User suspendedEmployee = userService.loginFromId(user.getId());

		assertFalse(suspendedEmployee.getIsSuspended());
	}

	@Test
	public void unsuspendEmployeeWithNoExistingUser() {
		assertThrows(InstanceNotFoundException.class, () -> userService.unsuspendEmployee(-1L));
	}

}
