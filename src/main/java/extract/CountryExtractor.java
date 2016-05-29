package extract;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import entities.Country;

public class CountryExtractor {

	public void extract(Set<String> countrySet) throws ClientProtocolException, IOException {
		ArrayList<Country> countries = new ArrayList<>();

		CountrySetCreator setCreator = new CountrySetCreator();
		Iterator<String> it = countrySet.iterator();
		while (it.hasNext()) {
			String country = it.next();

			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(
					"http://api.openstreetmap.fr/oapi/interpreter?data=%5Bout%3Ajson%5D%5Btimeout%3A200%5D%3B%28node%5B%22name%3Aen%22%3D%22"
							+ URLEncoder.encode(setCreator.convertToOpenstreetmapName(country)) + "%22%5D%3B%29%3Bout%20body%3B%3E%3Bout%20skel%20qt%3B%0A");
			HttpResponse response;
			response = client.execute(request);
			long population = 0;
			if (setCreator.convertToOpenstreetmapName(country).equals("Czechia"))
				population = 10516125;
			else {
				population = Long.parseLong(new JsonReaderService().getValueOfFirstKeyAppearance("population",
						new Scanner(response.getEntity().getContent())));
			}
			countries.add(new Country(country, population));

			request.releaseConnection();
			request.reset();
		}

		Type typeOfSrc = new TypeToken<ArrayList<Country>>() {
		}.getType();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		FileUtils.writeStringToFile(new File("json/countries.json"), gson.toJson(countries, typeOfSrc));

	}

}
