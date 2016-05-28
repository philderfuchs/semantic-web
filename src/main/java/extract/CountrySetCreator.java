package extract;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import entities.IbdStudy;

public class CountrySetCreator {

	public Set createCountrySet() {
//		File file = new File(getClass().getClassLoader().getResource("ibdIncidenceStats.json").getFile());
		Gson gson = new Gson();
		Set<String> countrySet = new HashSet<>();

		StringBuilder jsonString = new StringBuilder();
		try (Scanner scanner = new Scanner(new File("./json/ibdIncidenceStats.json"))) {
			while (scanner.hasNextLine()) {
				jsonString.append(scanner.nextLine());
			}
			Type type = new TypeToken<ArrayList<IbdStudy>>() {
			}.getType();
			ArrayList<IbdStudy> ibdStudies = (ArrayList<IbdStudy>) gson.fromJson(jsonString.toString(), type);
			
			for(IbdStudy study : ibdStudies) {
				countrySet.add(study.getCountry());
			}
			

		} catch (IOException e) {
			e.printStackTrace();
		}
		return countrySet;
	}
	
}
