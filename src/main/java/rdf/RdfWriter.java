package rdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import entities.CdStat;
import entities.Country;
import entities.FastFoodVenue;
import entities.IbdStudy;
import entities.SuicideRateStudy;
import entities.UcStat;

import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.datatypes.xsd.impl.XSDYearType;
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
	String osmNodeUrl = "https://www.openstreetmap.org/node/";

	String paPrefix = "http://www.imn.htwk-leipzig.de/~panders/";
	String geoPrefix = "http://www.geonames.org/ontology#";
	String dboPrefix = "http://dbpedia.org/ontology/";
	String timePrefix = "http://www.w3.org/2006/time/";
	String foafPrefix = "http://xmlns.com/foaf/0.1/";
	String yagoPrefix = "http://yago-knowledge.org/resource/";

	OntModel model = ModelFactory.createOntologyModel(modelSpec);

	OntClass countryClass = model.createClass(dboPrefix + "Country");
	DatatypeProperty geoNameProperty = model.createDatatypeProperty(geoPrefix + "officialName");
	DatatypeProperty populationProperty = model.createDatatypeProperty(geoPrefix + "population");

	OntClass statClass = model.createClass(paPrefix + "Stat");
	OntClass scdStatClass = model.createClass(paPrefix + "ScdStat");
	OntClass ibdStatClass = model.createClass(paPrefix + "IbdStat");
	OntClass cdStatClass = model.createClass(paPrefix + "CdStat");
	OntClass ucStatClass = model.createClass(paPrefix + "UcStat");
	OntClass obsTimeInterval = model.createClass(timePrefix + "Interval");
	ObjectProperty samplingTimeProperty = model.createObjectProperty(paPrefix + "samplingTime");
	DatatypeProperty hasBeginningProperty = model.createDatatypeProperty(timePrefix + "hasBeginning");
	DatatypeProperty hasEndProperty = model.createDatatypeProperty(timePrefix + "hasEnd");
	DatatypeProperty incidenceProperty = model.createDatatypeProperty(paPrefix + "incidence");

	OntClass fastFoodRestaurantClass = model.createClass(yagoPrefix + "wikicat_Fast-food_restaurants");
	DatatypeProperty nameProperty = model.createDatatypeProperty(foafPrefix + "name");

	ObjectProperty countryProperty = model.createObjectProperty(dboPrefix + "country");

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

			Map<String, Resource> countryResourceMap = new HashMap<>();

			// Triplify countries
			ArrayList<Country> countries = this.<Country> getListFromJson("countries",
					new TypeToken<ArrayList<Country>>() {
					}.getType());
			for (Country country : countries) {
				countryResourceMap.put(country.getName(),
						model.createResource(wikiUrl + URLEncoder.encode(country.getName(), "UTF-8"))
								.addProperty(geoNameProperty, country.getName())
								.addProperty(populationProperty, model.createTypedLiteral(country.getPopulation()))
								.addProperty(RDF.type, countryClass));
			}

			ArrayList<SuicideRateStudy> suicideRateStudies = this.<SuicideRateStudy> getListFromJson("suicideRateStats",
					new TypeToken<ArrayList<SuicideRateStudy>>() {
					}.getType());
			for (SuicideRateStudy suicideStat : suicideRateStudies) {
				Resource obsInterval = model
						.createResource(paPrefix + "ScdObservationInterval"
								+ URLEncoder.encode(suicideStat.getCountry(), "UTF-8"))
						.addProperty(hasBeginningProperty, model.createTypedLiteral("2012", new XSDYearType("gYear")))
						.addProperty(hasEndProperty, model.createTypedLiteral("2012", new XSDYearType("gYear")));
				model.createResource(paPrefix + "ScdStat/" + URLEncoder.encode(suicideStat.getCountry(), "UTF-8"))
						.addProperty(samplingTimeProperty, obsInterval)
						.addProperty(incidenceProperty, model.createTypedLiteral(suicideStat.getRate()))
						.addProperty(countryProperty, countryResourceMap.get(suicideStat.getCountry()))
						.addProperty(RDF.type, scdStatClass);
			}

			ArrayList<CdStat> cdStats = this.<CdStat> getListFromJson("cdStats", new TypeToken<ArrayList<CdStat>>() {
			}.getType());
			int idCounter = 0;
			for (CdStat cdStat : cdStats) {
				Resource obsInterval = model.createResource(paPrefix + "CdObservationInterval" + idCounter)
						.addProperty(hasBeginningProperty,
								model.createTypedLiteral(cdStat.getStartYear(), new XSDYearType("gYear")))
						.addProperty(hasEndProperty,
								model.createTypedLiteral(cdStat.getEndYear(), new XSDYearType("gYear")));
				model.createResource(
						paPrefix + "CdStat/" + URLEncoder.encode(cdStat.getCountry(), "UTF-8") + idCounter++)
						.addProperty(samplingTimeProperty, obsInterval)
						.addProperty(incidenceProperty, model.createTypedLiteral(cdStat.getRate()))
						.addProperty(countryProperty, countryResourceMap.get(cdStat.getCountry()))
						.addProperty(RDF.type, cdStatClass);
			}

			ArrayList<UcStat> ucStats = this.<UcStat> getListFromJson("ucStats", new TypeToken<ArrayList<UcStat>>() {
			}.getType());
			idCounter = 0;
			for (UcStat ucStat : ucStats) {
				Resource obsInterval = model.createResource(paPrefix + "UcObservationInterval" + idCounter)
						.addProperty(hasBeginningProperty,
								model.createTypedLiteral(ucStat.getStartYear(), new XSDYearType("gYear")))
						.addProperty(hasEndProperty,
								model.createTypedLiteral(ucStat.getEndYear(), new XSDYearType("gYear")));
				model.createResource(
						paPrefix + "UcStat/" + URLEncoder.encode(ucStat.getCountry(), "UTF-8") + idCounter++)
						.addProperty(samplingTimeProperty, obsInterval)
						.addProperty(incidenceProperty, model.createTypedLiteral(ucStat.getRate()))
						.addProperty(countryProperty, countryResourceMap.get(ucStat.getCountry()))
						.addProperty(RDF.type, ucStatClass);
			}

			ArrayList<FastFoodVenue> fastFoodVenues = this.<FastFoodVenue> getListFromJson("fastFoodVenues",
					new TypeToken<ArrayList<FastFoodVenue>>() {
					}.getType());
			for (FastFoodVenue venue : fastFoodVenues) {
				Resource r = model.createResource(osmNodeUrl + String.valueOf(venue.getId()))
						.addProperty(countryProperty, countryResourceMap.get(venue.getCountry()))
						.addProperty(RDF.type, fastFoodRestaurantClass);
				if (!venue.getName().equals("unknown")) {
					r.addProperty(nameProperty, venue.getName());
				}
			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void initializeModel() {
		// NS Prefixes
		model.setNsPrefix("pa", paPrefix);
		model.setNsPrefix("geo", geoPrefix);
		model.setNsPrefix("dbo", dboPrefix);
		model.setNsPrefix("time", timePrefix);

		// Country
		geoNameProperty.addDomain(countryClass);
		populationProperty.addDomain(countryClass);
		populationProperty.addRange(XSD.integer);

		// connecting properties
		countryProperty.addDomain(statClass);
		countryProperty.addDomain(fastFoodRestaurantClass);
		countryProperty.addRange(countryClass);

		// Stats
		statClass.addSubClass(ibdStatClass);
		statClass.addSubClass(scdStatClass);
		ibdStatClass.addSubClass(cdStatClass);
		ibdStatClass.addSubClass(ucStatClass);
		samplingTimeProperty.addDomain(statClass);
		samplingTimeProperty.addRange(obsTimeInterval);
		hasBeginningProperty.addDomain(obsTimeInterval);
		hasBeginningProperty.addRange(XSD.gYear);
		hasEndProperty.addDomain(obsTimeInterval);
		hasEndProperty.addRange(XSD.gYear);
		incidenceProperty.addDomain(statClass);
		incidenceProperty.addRange(XSD.xdouble);

		// FastFoodVenue
		nameProperty.addDomain(fastFoodRestaurantClass);
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