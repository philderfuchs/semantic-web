package extract;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import entities.Country;
import entities.IbdStudy;

public class CountryMaster {

	public Set<String> createCountrySet() {
		// File file = new
		// File(getClass().getClassLoader().getResource("ibdIncidenceStats.json").getFile());
		Gson gson = new Gson();
		Set<String> countrySet = new HashSet<>();

		StringBuilder jsonString = new StringBuilder();
		try (Scanner scanner = new Scanner(new File("./json/countries.json"))) {
			while (scanner.hasNextLine()) {
				jsonString.append(scanner.nextLine());
			}
			Type type = new TypeToken<ArrayList<Country>>() {
			}.getType();
			ArrayList<Country> countries = (ArrayList<Country>) gson.fromJson(jsonString.toString(), type);

			for (Country country : countries) {
				countrySet.add(country.getName());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Created countrieset");

		return countrySet;
	}

	public static String convertToStandardCountryName(String s) {
		if (s.equals("United Kingdom of Great Britain and Northern Ireland")) {
			return "United Kingdom";
		} else if (s.equals("The Netherlands")) {
			return "Netherlands";
		} else if (s.equals("The former Yugoslav republic of Macedonia")) {
			return "(Former Yugoslav Republic of) Macedonia";
		} else if (s.equals("Russian Federation")) {
			return "Russia";
		} else {
			return s;
		}
	}

	public static String convertStandardCountryNameToOsmName(String name) {

		if (name.equals("(Former Yugoslav Republic of) Macedonia")) {
			return "Macedonia";
		} else if (name.equals("Russia")) {
			return "Russian Federation";
		} else {
			return name;
		}

	}

}
