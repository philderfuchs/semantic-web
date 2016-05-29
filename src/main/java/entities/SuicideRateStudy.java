package entities;

public class SuicideRateStudy {
	String country;
	Double rate;
	
	public SuicideRateStudy(String country, Double rate) {
		super();
		this.country = country;
		this.rate = rate;
	}
	
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	
}
