package model;

public class User {

	private String id;
	private String name;
	private String userId;

	public User() {

	}

	public User(String id, String name, String userId) {
		super();
		this.id = id;
		this.name = name;
		this.userId = userId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
