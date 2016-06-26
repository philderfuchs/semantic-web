package extract;

import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.http.client.ClientProtocolException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WeatherExtractor {
	
	public void extract() {
		
		Document doc;
		try {
			doc = Jsoup.connect("http://www.wetter.de/klima/europa/deutschland-c49.html").get();
			Elements wrappers = doc.getElementsByClass("wrapper-climate-data");
			Elements tbodies = wrappers.get(2).getElementsByTag("tbody");
			
			double sum = 0;
			for(Element e : tbodies.get(0).getElementsByTag("td")) {
				sum += Double.parseDouble(e.text().replace(",", "."));
			}
			System.out.println(sum / tbodies.get(0).getElementsByTag("td").size());
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
