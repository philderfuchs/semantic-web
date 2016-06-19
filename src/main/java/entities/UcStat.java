package entities;

public class UcStat {
	String country;
	String startYear;
	String endYear;
	double rate;
	
	public UcStat(String country, String startYear, String endYear, double rate) {
		super();
		this.country = country;
		this.startYear = startYear;
		this.endYear = endYear;
		this.rate = rate;
	}
	
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getStartYear() {
		return startYear;
	}
	public void setStartYear(String startYear) {
		this.startYear = startYear;
	}
	public String getEndYear() {
		return endYear;
	}
	public void setEndYear(String endYear) {
		this.endYear = endYear;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}

}
