package entities;

public class FastFoodVenue {
	String country;
	String name;
	long id;
	
	public FastFoodVenue(String country, String name, long id) {
		super();
		this.country = country;
		this.name = name;
		this.id = id;
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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
}
