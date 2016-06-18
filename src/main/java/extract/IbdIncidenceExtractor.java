package extract;

import entities.IbdStudy;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.google.gson.reflect.TypeToken;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class IbdIncidenceExtractor {

	public void extract(Set<String> countrySet) {

		File file = new File(getClass().getClassLoader().getResource("incidence.csv").getFile());
		try (Scanner scanner = new Scanner(file)) {
			ArrayList<IbdStudy> ibdStudies = new ArrayList<>();

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				// System.out.println(line);
				String[] cells = line.split("<comma>\\s+|\\,");
				if (cells.length == 5) {
					String country = cells[0].split("[1-9]")[0];
					if (country.equals("Country") || country.contains("Europe") || this.skipCountry(country))
						continue;
					if (country.equals("UK"))
						country = "United Kingdom";
					// if(country.equals("Czech Republic"))
					// country = "Czechia";
					if (country.equals("France"))
						continue;
					String year = cells[2].contains("–") ? cells[2].split("–")[1] : cells[2];
					year = year.equals("") ? "1993" : year;
					double cdIncidence = cells[3].equals("NA") ? -1 : Double.parseDouble(cells[3]);
					double ucIncidence = cells[4].equals("NA") ? -1 : Double.parseDouble(cells[4]);
					ibdStudies.add(new IbdStudy(country, year, cdIncidence, ucIncidence));
				}
			}

			scanner.close();

			Type typeOfSrc = new TypeToken<ArrayList<IbdStudy>>() {
			}.getType();
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			FileUtils.writeStringToFile(new File("json/ibdIncidenceStats.json"), gson.toJson(ibdStudies, typeOfSrc));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean skipCountry(String s) {
		if (s.equals("Faroe Islands")) {
			return true;
		} else if (s.equals("Czech Republic")) {
			return true;
		} else {
			return false;
		}
	}
}
