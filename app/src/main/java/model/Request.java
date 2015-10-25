package model;

public class Request {

	private String Id;
	private String names;
	private String status;

	public Request() {

	}

	public Request(String id, String names, String status) {
		super();
		Id = id;
		this.names = names;
		this.status = status;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getNames() {
		return names;
	}

	public void setNames(String names) {
		this.names = names;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
