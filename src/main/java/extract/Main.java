package extract;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

public class Main {

	public static void main(String[] args) {
		IncidenceExtractor incidenceExtractor = new IncidenceExtractor();
		incidenceExtractor.extract();
		
		CountrySetCreator creator = new CountrySetCreator();
		Set<String> countrySet = creator.createCountrySet();
		
//		CountryExtractor countryExtractor = new CountryExtractor();
//		try {
//			countryExtractor.extract(countrySet);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
		FastFoodVenueExtractor fastFoodExtractor = new FastFoodVenueExtractor();
		try {
			fastFoodExtractor.extract(countrySet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	
//		SuicideRateExtractor suicideRateExtractor = new SuicideRateExtractor();
//		try {
//			suicideRateExtractor.extract(countrySet);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}

}
