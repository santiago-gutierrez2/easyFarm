package es.udc.paproject.backend.model.entities;

import javax.persistence.*;

@Entity
public class User {
	
	public enum RoleType {EMPLOYEE, ADMIN};

	private Long id;
	private String userName;
	private String dni;
	private String nss;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private RoleType role;
	private boolean isEliminated;
	private boolean isSuspended;
	private Farm farm; // MANY TO ONE

	public User() {}

	public User(String userName, String dni, String nss, String password, String firstName, String lastName, String email, Farm farm) {
		this.userName = userName;
		this.dni = dni;
		this.nss = nss;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.farm = farm;
		this.isEliminated = false;
		this.isSuspended = false;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public RoleType getRole() {
		return role;
	}

	public void setRole(RoleType role) {
		this.role = role;
	}

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "farmId")
	public Farm getFarm() {return farm;}

	public void setFarm(Farm farm) {this.farm = farm;}

	public boolean getIsEliminated() {
		return isEliminated;
	}

	public void setIsEliminated(boolean eliminated) {
		isEliminated = eliminated;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getNss() {
		return nss;
	}

	public void setNss(String nss) {
		this.nss = nss;
	}

	public boolean getIsSuspended() {
		return isSuspended;
	}

	public void setIsSuspended(boolean suspended) {
		isSuspended = suspended;
	}
}
