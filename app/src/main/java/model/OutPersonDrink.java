package model;

public class OutPersonDrink {

	private String time;
	private String boozeType;
	private String quantity;
	private String bac;

	public OutPersonDrink() {

	}

	public OutPersonDrink(String time, String boozeType, String quantity,
			String bac) {
		super();
		this.time = time;
		this.boozeType = boozeType;
		this.quantity = quantity;
		this.bac = bac;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getBoozeType() {
		return boozeType;
	}

	public void setBoozeType(String boozeType) {
		this.boozeType = boozeType;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getBac() {
		return bac;
	}

	public void setBac(String bac) {
		this.bac = bac;
	}

}
