package entities;

public class IbdStudy {
	String country;
	String year;
	double cdIncidence;
	double ucIncidence;
	public IbdStudy(String country, String year, double cdIncidence, double ucIncidence) {
		super();
		this.country = country;
		this.year = year;
		this.cdIncidence = cdIncidence;
		this.ucIncidence = ucIncidence;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public double getCdIncidence() {
		return cdIncidence;
	}
	public void setCdIncidence(double cdIncidence) {
		this.cdIncidence = cdIncidence;
	}
	public double getUcIncidence() {
		return ucIncidence;
	}
	public void setUcIncidence(double ucIncidence) {
		this.ucIncidence = ucIncidence;
	}
	
}
