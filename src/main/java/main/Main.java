package main;

import java.io.IOException;
import java.util.Set;

import extract.*;

import rdf.RdfWriter;

public class Main {

	public static void main(String[] args) {
		RdfWriter writer = new RdfWriter();
		writer.write();
		
//		new WeatherExtractor().extract();
		
//		CountryExtractor countryExtractor = new CountryExtractor();
//		try {
//			countryExtractor.extract();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		CountryMaster master = new CountryMaster();
//		Set<String> countrySet = master.createCountrySet();
//		
//		FastFoodVenueExtractor fastFoodExtractor = new FastFoodVenueExtractor();
//		try {
//			fastFoodExtractor.extract(countrySet);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

//		IbdIncidenceExtractor ibdIncidenceExtractor = new IbdIncidenceExtractor();
//		ibdIncidenceExtractor.extract(countrySet);
//		SuicideRateExtractor suicideRateExtractor = new SuicideRateExtractor();
//		try {
//			suicideRateExtractor.extract(countrySet);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
				

	}

}
