package entities;

public class IbdStudy {
	String country;
	String startYear;
	String endYear;
	double cdIncidence;
	double ucIncidence;
	
	
	public IbdStudy(String country, String startYear, String endYear, double cdIncidence, double ucIncidence) {
		super();
		this.country = country;
		this.startYear = startYear;
		this.endYear = endYear;
		this.cdIncidence = cdIncidence;
		this.ucIncidence = ucIncidence;
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
