package rdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import entities.Country;
import entities.SuicideRateStudy;

import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;

/**
 * Tutorial 3 Statement attribute accessor methods
 */
public class RdfWriter extends Object {
	// some definitions
	static OntModelSpec modelSpec = OntModelSpec.OWL_DL_MEM_RULE_INF;
	static String rdfStile = "RDF/XML-ABBREV";

	String wikiUrl = "https://en.wikipedia.org/wiki/";

	String paPrefix = "http://www.imn.htwk-leipzig.de/~panders/";
	String geoPrefix = "http://www.geonames.org/ontology#";

	OntModel model = ModelFactory.createOntologyModel(modelSpec);

	OntClass countryClass = model.createClass(paPrefix + "Country");
	DatatypeProperty nameProperty = model.createDatatypeProperty(geoPrefix + "name");
	DatatypeProperty populationProperty = model.createDatatypeProperty(geoPrefix + "population");

	OntClass suicideIncidence = model.createClass(paPrefix + "SuicideIncidence");
	DatatypeProperty rateProperty = model.createDatatypeProperty(paPrefix + "rate");
	DatatypeProperty countryProperty = model.createDatatypeProperty(geoPrefix + "name");

	public void write() {
		this.initializeModel();
		this.addTriples();

		try {
			// model.write(System.out, rdfStile);
			model.write(new FileOutputStream("rdf/ibdOntology.rdf"), rdfStile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void addTriples() {
		try {

			for (Country country : this.<Country> getListFromJson("countries", new TypeToken<ArrayList<Country>>() {
			}.getType())) {
				model.createResource(wikiUrl + URLEncoder.encode(country.getName(), "UTF-8"))
						.addProperty(nameProperty, country.getName())
						.addProperty(populationProperty, model.createTypedLiteral(country.getPopulation()))
						.addProperty(RDF.type, countryClass);
			}

			for (SuicideRateStudy suicideIncidence : this.<SuicideRateStudy> getListFromJson("suicideRateStats",
					new TypeToken<ArrayList<SuicideRateStudy>>() {
					}.getType())) {
				model.createResource(
						paPrefix + "SuicideIncidence/" + URLEncoder.encode(suicideIncidence.getCountry(), "UTF-8"))
						.addProperty(rateProperty, model.createTypedLiteral(suicideIncidence.getRate())).addProperty(
								countryProperty, wikiUrl + URLEncoder.encode(suicideIncidence.getCountry(), "UTF-8"));

			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void initializeModel() {
		nameProperty.addDomain(countryClass);
		populationProperty.addDomain(countryClass);
		populationProperty.addRange(XSD.integer);

		rateProperty.addDomain(suicideIncidence);
		rateProperty.addRange(XSD.xdouble);
		countryProperty.addDomain(suicideIncidence);
		countryProperty.addRange(countryClass);

		model.setNsPrefix("pa", paPrefix);
		model.setNsPrefix("geo", geoPrefix);
	}

	private <T> ArrayList<T> getListFromJson(String filename, Type typeToken) {
		Gson gson = new Gson();

		StringBuilder jsonString = new StringBuilder();
		ArrayList<T> list = null;

		try (Scanner scanner = new Scanner(new File("./json/" + filename + ".json"))) {
			while (scanner.hasNextLine()) {
				jsonString.append(scanner.nextLine());
			}
			list = (ArrayList<T>) gson.fromJson(jsonString.toString(), typeToken);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

}