package es.udc.paproject.backend.model.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import es.udc.paproject.backend.model.entities.Farm;
import es.udc.paproject.backend.model.exceptions.IncorrectLoginException;
import es.udc.paproject.backend.model.exceptions.IncorrectPasswordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.paproject.backend.model.exceptions.DuplicateInstanceException;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.entities.User;
import es.udc.paproject.backend.model.entities.UserDao;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	
	@Autowired
	private PermissionChecker permissionChecker;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserDao userDao;
	
	@Override
	public void signUp(User user) throws DuplicateInstanceException {
		
		if (userDao.existsByUserName(user.getUserName())) {
			throw new DuplicateInstanceException("project.entities.user", user.getUserName());
		}
			
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole(User.RoleType.EMPLOYEE);
		
		userDao.save(user);
		
	}

	@Override
	@Transactional(readOnly=true)
	public User login(String userName, String password) throws IncorrectLoginException {
		
		Optional<User> user = userDao.findByUserName(userName);
		
		if (user.isEmpty()) {
			throw new IncorrectLoginException(userName, password);
		}
		
		if (!passwordEncoder.matches(password, user.get().getPassword())) {
			throw new IncorrectLoginException(userName, password);
		}
		
		return user.get();
		
	}
	
	@Override
	@Transactional(readOnly=true)
	public User loginFromId(Long id) throws InstanceNotFoundException {
		return permissionChecker.checkUser(id);
	}

	@Override
	public User updateProfile(Long id, String firstName, String lastName, String email) throws InstanceNotFoundException {
		
		User user = permissionChecker.checkUser(id);
		
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		
		return user;

	}

	@Override
	public void changePassword(Long id, String oldPassword, String newPassword)
		throws InstanceNotFoundException, IncorrectPasswordException {
		
		User user = permissionChecker.checkUser(id);
		
		if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
			throw new IncorrectPasswordException();
		} else {
			user.setPassword(passwordEncoder.encode(newPassword));
		}
		
	}

	@Override
	public void deleteEmployee(Long employeeId) throws InstanceNotFoundException {

		User employee = permissionChecker.checkUser(employeeId);

		// delete employee set to true isEliminated
		employee.setIsEliminated(true);
		userDao.save(employee);
	}

	@Override
	public List<User> getEmployees(Long userId) throws InstanceNotFoundException{

		Optional<User> admin = userDao.findById(userId);

		if (admin.isEmpty()) {
			throw new InstanceNotFoundException("project.entities.user", userId);
		}

		Farm farm = admin.get().getFarm();

        return farm.getUsers().stream()
				.filter(user -> !user.getIsEliminated() && user.getRole() == User.RoleType.EMPLOYEE).collect(Collectors.toList());
	}

}
