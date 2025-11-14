package domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class User {
	
	@Id
	private String email;
	private String username;
	private String password;
	@OneToOne(cascade=CascadeType.PERSIST)
	private Driver driver = null;
	@OneToOne(cascade=CascadeType.PERSIST)
	private Traveler traveler = null;
	
	public User(String email, String username, String password, String type) {
		this.email = email;
		this.username = username;
		this.password = password;
		if(type.equals("T")) {
			traveler = new Traveler(1000);
		}
		else if(type.equals("D"))
			driver = new Driver();
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public Traveler getTraveler() {
		return traveler;
	}
	
	public Driver getDriver() {
		return driver;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}
}
