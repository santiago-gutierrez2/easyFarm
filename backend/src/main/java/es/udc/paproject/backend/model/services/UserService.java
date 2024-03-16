package es.udc.paproject.backend.model.services;

import es.udc.paproject.backend.model.exceptions.DuplicateInstanceException;
import es.udc.paproject.backend.model.exceptions.IncorrectLoginException;
import es.udc.paproject.backend.model.exceptions.IncorrectPasswordException;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.entities.User;

import java.util.List;

public interface UserService {
	
	void signUp(User user) throws DuplicateInstanceException;
	
	User login(String userName, String password) throws IncorrectLoginException;
	
	User loginFromId(Long id) throws InstanceNotFoundException;
	
	User updateProfile(Long id, String firstName, String lastName, String email) throws InstanceNotFoundException;
	
	void changePassword(Long id, String oldPassword, String newPassword)
		throws InstanceNotFoundException, IncorrectPasswordException;

	void deleteEmployee(Long employeeId) throws InstanceNotFoundException;

	List<User> getEmployees(Long userId) throws InstanceNotFoundException;

	List<User> getActiveEmployees(Long userId) throws InstanceNotFoundException;

	void suspendEmployee(Long employeeId) throws InstanceNotFoundException;

	void unsuspendEmployee(Long employeeId) throws InstanceNotFoundException;

}
