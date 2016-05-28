package entities;

public class FastFoodVenue {
	String country;
	String name;
	
	public FastFoodVenue(String country, String name) {
		super();
		this.country = country;
		this.name = name;
	}
	
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
