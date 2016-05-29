package extract;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import entities.FastFoodVenue;
import entities.SuicideRateStudy;

public class SuicideRateExtractor {

	public void extract(Set<String> countrySet)
			throws ClientProtocolException, IOException, ParserConfigurationException, SAXException {
		ArrayList<SuicideRateStudy> suicideRateStudies = new ArrayList<>();

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//		Document doc = dBuilder
//				.parse("http://apps.who.int/gho/athena/api/GHO/SDGSUICIDE?filter=REGION:EUR&profile=simple");
		Document doc = dBuilder
		.parse("http://apps.who.int/gho/athena/api/GHO/SDGSUICIDE?profile=simple");
		doc.getDocumentElement().normalize();
		NodeList nList = doc.getElementsByTagName("Fact");

		for (int i = 0; i < nList.getLength(); i++) {
			Node nNode = nList.item(i);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				String currentCountryName = this.convertToStandardCountryName(
						eElement.getElementsByTagName("COUNTRY").item(0).getTextContent());
				if (countrySet.contains(currentCountryName)) {
					suicideRateStudies.add(new SuicideRateStudy(currentCountryName,
							Double.parseDouble(eElement.getElementsByTagName("Numeric").item(0).getTextContent())));
				}
			}
		}

		Type typeOfSrc = new TypeToken<ArrayList<SuicideRateStudy>>() {
		}.getType();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		FileUtils.writeStringToFile(new File("json/suicideRateStats.json"), gson.toJson(suicideRateStudies, typeOfSrc));
	}

	private String convertToStandardCountryName(String s) {
		if (s.equals("United Kingdom of Great Britain and Northern Ireland")) {
			return "United Kingdom";
		} else if (s.equals("Netherlands")) {
			return "The Netherlands";
		} else {
			return s;
		}
	}

}
