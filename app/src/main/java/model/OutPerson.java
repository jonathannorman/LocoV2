package model;

public class OutPerson {

	private String names;
	private String bac;
	private String latitude;
	private String longitude;
	private String friendId;

	public OutPerson() {

	}

	public OutPerson(String names, String bac, String latitude,
			String longitude, String friendId) {
		super();
		this.names = names;
		this.bac = bac;
		this.latitude = latitude;
		this.longitude = longitude;
		this.friendId = friendId;
	}

	public String getNames() {
		return names;
	}

	public void setNames(String names) {
		this.names = names;
	}

	public String getBac() {
		return bac;
	}

	public void setBac(String bac) {
		this.bac = bac;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getFriendId() {
		return friendId;
	}

	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}

	

}
