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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import entities.Country;

public class CountryExtractor {

	public void extract() throws ClientProtocolException, IOException {
		System.out.println("Extracting countries");
		ArrayList<Country> countries = new ArrayList<>();
		Document doc = Jsoup.connect("https://en.wikipedia.org/wiki/Europe").get();
		Elements tbodies = doc.getElementsByTag("tbody");
		Element countryTable = tbodies.get(1);
		for (Element row : countryTable.getElementsByTag("tr")) {
			if (row.child(2).text().equals("Name"))
				continue;
			if (row.className().equals("sortbottom"))
				break;

			countries.add(new Country(row.child(2).text().split("\\s\\[")[0],
					Long.parseLong(row.child(4).text().replace(",", ""))));
		}
		
		System.out.println("Writing Json");

		Type typeOfSrc = new TypeToken<ArrayList<Country>>() {
		}.getType();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		FileUtils.writeStringToFile(new File("json/countries.json"), gson.toJson(countries, typeOfSrc));
		
		System.out.println("Done");

	}

}
