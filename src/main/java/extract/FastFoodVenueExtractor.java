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
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import entities.Country;
import entities.FastFoodVenue;

public class FastFoodVenueExtractor {

	public void extract(Set<String> countrySet) throws ClientProtocolException, IOException {
		ArrayList<FastFoodVenue> fastFoodVenues = new ArrayList<>();

		for(String country : countrySet) {

			new OverpassApiService();
			Long id = 3600000000L + Long.parseLong(OverpassApiService.getIdOfCountry(country));

			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(
					"http://api.openstreetmap.fr/oapi/interpreter?data=%5Bout%3Ajson%5D%5Btimeout%3A200%5D%3Barea%28"
							+ id
							+ "%29%2D%3E%2EsearchArea%3B%28node%5B%22amenity%22%3D%22fast%5Ffood%22%5D%28area%2EsearchArea%29%3B%29%3Bout%20body%3B%3E%3Bout%20skel%20qt%3B%0A");
			HttpResponse response;
			response = client.execute(request);
			Scanner scanner = new Scanner(response.getEntity().getContent());
			StringBuilder builder = new StringBuilder();
			while (scanner.hasNext()) {
				builder.append(scanner.nextLine());
			}
			JSONObject jObject = new JSONObject(builder.toString().trim());
			JSONArray array = jObject.getJSONArray("elements");

			for (int i = 0; i < array.length(); i++) {
				JSONObject node = (JSONObject) array.get(i);
				JSONObject tags = node.getJSONObject("tags");
				String name = tags.has("name") ? tags.getString("name") : "unknown";
				fastFoodVenues.add(new FastFoodVenue(country, name));
			}
			System.out.println("Got fastfood-data. Name: " + country + " | Overpass-Region-Id: " + id + " | Count of venues: " + array.length());

		}
		Type typeOfSrc = new TypeToken<ArrayList<FastFoodVenue>>() {
		}.getType();
		Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		FileUtils.writeStringToFile(new File("json/fastFoodVenues.json"), gson.toJson(fastFoodVenues, typeOfSrc));
	}

}
