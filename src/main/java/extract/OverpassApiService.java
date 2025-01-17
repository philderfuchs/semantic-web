package extract;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class OverpassApiService {
	
	public static String getIdOfCountry(String country) throws ClientProtocolException, IOException {
		if(country.equals("Netherlands")) return "47796";
		if(country.equals("Ireland")) return "6040654";
		if(country.equals("France")) return "2202162";
		if(country.equals("Georgia")) return "161957";
		if(country.equals("Luxembourg")) return "2171347";
		if(country.equals("Malta")) return "4822547";
		if(country.equals("Monaco")) return "2220322";
		
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(
				"http://api.openstreetmap.fr/oapi/interpreter?data=%5Bout%3Ajson%5D%5Btimeout%3A200%5D%3B%28relation%5B%22name%3Aen%22%3D%22"
						+ URLEncoder.encode(CountryMaster.convertStandardCountryNameToOsmName(country))
						+ "%22%5D%5B%22type%22%3D%22boundary%22%5D%3B%29%3Bout%20body%3B%3E%3Bout%20skel%20qt%3B%0A");
		HttpResponse response = client.execute(request);
		String id = new JsonReaderService().getValueOfFirstKeyAppearance("id",
				new Scanner(response.getEntity().getContent()));
		request.releaseConnection();
		request.reset();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return id;
	}
}
