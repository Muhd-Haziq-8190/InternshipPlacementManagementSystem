package Project;

public class User {
	protected String userId;
	protected String name;
	protected String password;
	
	public User(String userId, String name) {
		this.userId = userId;
		this.name = name;
		this.password = "password"; // Password is defaulted to "password"
	}
	
	
	// Checks if current pass is equal to input pass for authentication
	public boolean checkPassword(String password) {
		return this.password.equals(password);
		
	}
	
	
	public boolean changePassword(String oldPass, String newPass) {
		// Checks for following before changing password:
		// 1) input password does not equal current password
		// 2) New password is not null
		// 3) New password is not an empty string
		
		if(this.checkPassword(oldPass) && newPass != null && !newPass.isEmpty()) {
			this.password = newPass;
			return true;
		}
		return false;
	}
	
	
	// --------- GETTER & SETTER --------- //
	
	public String getId() {
		return this.userId;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
}
