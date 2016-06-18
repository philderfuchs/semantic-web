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
					String country = CountryMaster.convertToStandardCountryName(cells[0].split("[1-9]")[0]);

					if (country.equals("Country") || country.contains("Europe") || !countrySet.contains(country))
						continue;
					if (cells[3].equals("NA") || cells[4].equals("NA"))
						continue;

					String startYear, endYear;
					if (cells[2].equals("")) {
						startYear = "1991";
						endYear = "1993";
					} else {
						startYear = cells[2].split("–")[0];
						endYear = cells[2].split("–")[1];
					}
					double cdIncidence = Double.parseDouble(cells[3]);
					double ucIncidence = Double.parseDouble(cells[4]);
					ibdStudies.add(new IbdStudy(country, startYear, endYear, cdIncidence, ucIncidence));
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

}
