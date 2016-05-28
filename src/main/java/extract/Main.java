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
		CountryExtractor countryExtractor = new CountryExtractor();
		countryExtractor.extract(countrySet);
		
//		for(String s : countrySet) {
//			System.out.println(s);
//		}

	}

}
