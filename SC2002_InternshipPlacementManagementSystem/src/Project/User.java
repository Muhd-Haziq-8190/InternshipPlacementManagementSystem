package Project;

public class User {
	protected String userId;
	protected String name;
	protected String password;
	
	public User(String userId, String name) {
		this.userId = userId;
		this.name = name;
		this.password = "password";
	}
	
	public String getId() {
		return this.userId;
	}
	
	public String getName() {
		return this.userId;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean checkPassword(String password) {
		return this.password.equals(password);
		
	}
	
	public boolean changePassword(String oldPass, String newPass) {
		if(this.checkPassword(oldPass) && newPass != null && !newPass.isEmpty()) {
			this.password = newPass;
			return true;
		}
		return false;
	}
	
	

	
}
