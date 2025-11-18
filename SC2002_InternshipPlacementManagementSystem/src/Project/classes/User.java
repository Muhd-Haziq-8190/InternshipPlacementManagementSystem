package Project.classes;

public abstract class User {
	private String userId;
	private String name;
	private String password;
	
	public User(String userId, String name) {
		this.userId = userId;
		this.name = name;
		this.password = "password"; // Password is defaulted to "password"
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
