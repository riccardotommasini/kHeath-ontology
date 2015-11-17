package org.knoesis.khealth.api.sensors;

import org.joda.time.DateTime;
import org.knoesis.khealth.utils.KHealthUtils;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;

public class Humidity extends SensorEndpoint {

	public static final String outdoor = ":OutdoorHumidityObservation";

	public static final String indoor = ":IndoorHumidityObservation";

	public Model query(DateTime from, DateTime to) {

		return queryIndoor(from, to).union(queryOutdoor(from, to));
	}

	public Model queryIndoor(DateTime from, DateTime to) {

		String fromString = from.toString(KHealthUtils.fmt);
		String toString = to.toString(KHealthUtils.fmt);
		System.out.println("From " + fromString);
		System.out.println("To " + toString);

		Model m = retrieveModel(fromString, toString, indoor);

		String inner = "SELECT ?p ?year ?month ?day (avg(?qv) as ?hum_avg) (max(?time) as ?current)"
				+ "WHERE { "
				+ "?obs a "
				+ indoor
				+ " ; ssn:featureOfInterest ?p ; ssn:observationResult ?res ; ssn:observationResultTime ?instant ."
				+ "?res ssn:hasValue ?val . "
				+ "?val :hasObservationValue ?qv . "
				+ "?instant time:xsdDateTime ?time ."
				+ "bind ( day(xsd:dateTime(?time)) as ?day )"
				+ "bind ( month(xsd:dateTime(?time)) as ?month )"
				+ "bind ( year(xsd:dateTime(?time)) as ?year ) } "
				+ "GROUP BY ?p ?year ?month ?day "
				+ "ORDER BY ?p ?year ?month ?day ";

		Query query = QueryFactory
				.create("PREFIX wea: <https://www.auto.tuwien.ac.at/downloads/thinkhome/ontology/WeatherOntology.owl#> "
						+ "PREFIX : <http://www.knoesis.org/khealth#> "
						+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
						+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
						+ "PREFIX time: <http://www.w3.org/2006/time#> "
						+ "PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#> "
						+ "PREFIX asthma: <http://www.knoesis.org/khealth/asthma#> "
						+ inner);
		ResultSet r = QueryExecutionFactory.create(query, m).execSelect();
		ResultSetFormatter.out(System.out, r, query);

		query = QueryFactory
				.create("PREFIX wea: <https://www.auto.tuwien.ac.at/downloads/thinkhome/ontology/WeatherOntology.owl#> "
						+ "PREFIX : <http://www.knoesis.org/khealth#> "
						+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
						+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
						+ "PREFIX time: <http://www.w3.org/2006/time#> "
						+ "PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#> "
						+ "PREFIX asthma: <http://www.knoesis.org/khealth/asthma#> "
						+ "CONSTRUCT { ?i a :IndoorHumidity ; wea:hasValue ?hum_avg . }"
						+ "WHERE { "
						+ "?instant time:xsdDateTime ?time  . "
						+ "{ "
						+ inner
						+ "}"
						+ "bind ( URI(CONCAT(CONCAT(CONCAT(\"http://www.knoesis.org/khealth#hum-in_\", STR(?year)),STR(?month)), STR(?day))) as ?i ) ."
						+ "}");

		return QueryExecutionFactory.create(query, m).execConstruct();
	}

	public Model queryOutdoor(DateTime from, DateTime to) {

		String fromString = from.toString(KHealthUtils.fmt);
		String toString = to.toString(KHealthUtils.fmt);
		System.out.println("From " + fromString);
		System.out.println("To " + toString);

		Model m = retrieveModel(fromString, toString, outdoor);

		KHealthUtils.debug(m);

		String inner = "SELECT ?p ?year ?month ?day (avg(?qv) as ?hum_avg) (max(?time) as ?current)"
				+ "WHERE { "
				+ "?obs a "
				+ outdoor
				+ " ; ssn:featureOfInterest ?p ; ssn:observationResult ?res ; ssn:observationResultTime ?instant ."
				+ "?res ssn:hasValue ?val . "
				+ "?val :hasObservationValue ?qv . "
				+ "?instant time:xsdDateTime ?time ."
				+ "bind ( day(xsd:dateTime(?time)) as ?day )"
				+ "bind ( month(xsd:dateTime(?time)) as ?month )"
				+ "bind ( year(xsd:dateTime(?time)) as ?year ) } "
				+ "GROUP BY ?p ?year ?month ?day "
				+ "ORDER BY ?p ?year ?month ?day ";

		Query query = QueryFactory
				.create("PREFIX wea: <https://www.auto.tuwien.ac.at/downloads/thinkhome/ontology/WeatherOntology.owl#> "
						+ "PREFIX : <http://www.knoesis.org/khealth#> "
						+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
						+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
						+ "PREFIX time: <http://www.w3.org/2006/time#> "
						+ "PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#> "
						+ "PREFIX asthma: <http://www.knoesis.org/khealth/asthma#> "
						+ inner);
		ResultSet r = QueryExecutionFactory.create(query, m).execSelect();
		ResultSetFormatter.out(System.out, r, query);

		query = QueryFactory
				.create("PREFIX wea: <https://www.auto.tuwien.ac.at/downloads/thinkhome/ontology/WeatherOntology.owl#> "
						+ "PREFIX : <http://www.knoesis.org/khealth#> "
						+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
						+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
						+ "PREFIX time: <http://www.w3.org/2006/time#> "
						+ "PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#> "
						+ "PREFIX asthma: <http://www.knoesis.org/khealth/asthma#> "
						+ "CONSTRUCT { ?i a :OutdoorHumidity ; wea:hasValue ?hum_avg . }"
						+ "WHERE { "
						+ "?instant time:xsdDateTime ?time  . "
						+ "{ "
						+ inner
						+ "}"
						+ "bind ( URI(CONCAT(CONCAT(CONCAT(\"http://www.knoesis.org/khealth#hum-out_\", STR(?year)),STR(?month)), STR(?day))) as ?i ) ."
						+ "}");

		return QueryExecutionFactory.create(query, m).execConstruct();
	}
}
