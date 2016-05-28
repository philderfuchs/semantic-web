package extract;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class CountryExtractor {

	public void extract(Set countrySet) {
		// try {
		// this.createCountryIdMap(countrySet);
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		Iterator<String> it = countrySet.iterator();
		try {
			while (it.hasNext()) {
				String country = it.next();

				System.out.println(country);
				System.out.println(this.getIdOfCountry(country));
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String getIdOfCountry(String country) throws ClientProtocolException, IOException {
		RestClient restClient = new RestClient();
		Scanner scanner = restClient.getScannerFromUrl(
				"http://api.openstreetmap.fr/oapi/interpreter?data=%5Bout%3Ajson%5D%5Btimeout%3A200%5D%3B%28relation%5B%22name%3Aen%22%3D%22"
						+ URLEncoder.encode(country)
						+ "%22%5D%5B%22type%22%3D%22boundary%22%5D%3B%29%3Bout%20body%3B%3E%3Bout%20skel%20qt%3B%0A");

		String line = "";
		String id = "";
		while (scanner.hasNextLine()) {
			line = scanner.nextLine();
			if (line.contains("id")) {
				id = line.split(":\\s+")[1].split(",")[0];
				break;
			}
		}

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return id;
	}

	// private Map createCountryIdMap(Set countrySet) throws
	// ClientProtocolException, IOException {
	// Map<String, String> countryIdMap = new HashMap();
	// Iterator<String> it = countrySet.iterator();
	//
	// // while (it.hasNext()) {
	// String country = it.next();
	// HttpClient client = new DefaultHttpClient();
	// HttpGet request = new HttpGet(
	// "http://overpass-api.de/api/interpreter?data=%5Bout%3Ajson%5D%5Btimeout%3A200%5D%3B%28relation%5B%22name%3Aen%22%3D%22"
	// + URLEncoder.encode(country)
	// +
	// "%22%5D%5B%22type%22%3D%22boundary%22%5D%3B%29%3Bout%20body%3B%3E%3Bout%20skel%20qt%3B%0A");
	// HttpResponse response = client.execute(request);
	// BufferedReader rd = new BufferedReader(new
	// InputStreamReader(response.getEntity().getContent()));
	// String line = "";
	//
	// while ((line = rd.readLine()) != null) {
	// if (line.contains("id")) {
	// countryIdMap.put(country, line.split(":\\s+")[1].split(",")[0]);
	// break;
	// }
	// }
	// request.releaseConnection();
	// request.reset();
	//
	// System.out.println(country);
	// System.out.println(countryIdMap.get(country));
	//
	// // Kill all requests
	// HttpGet killRequest = new
	// HttpGet("http://overpass-api.de/api/kill_my_queries");
	// client.execute(killRequest);
	// killRequest.releaseConnection();
	// killRequest.reset();
	//
	// // try {
	// // System.out.println("Waiting 10s....");
	// // Thread.sleep(10000);
	// // System.out.println("Continuing....");
	// // } catch (InterruptedException e) {
	// // // TODO Auto-generated catch block
	// // e.printStackTrace();
	// // }
	// // }
	//
	// return null;
	// }

}
