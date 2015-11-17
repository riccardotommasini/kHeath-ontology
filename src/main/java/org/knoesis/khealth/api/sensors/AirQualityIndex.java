package org.knoesis.khealth.api.sensors;

import org.joda.time.DateTime;
import org.knoesis.khealth.utils.KHealthUtils;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;

public class AirQualityIndex extends SensorEndpoint {

	public Model query(DateTime from, DateTime to) {

		String fromString = from.toString(KHealthUtils.fmt);
		String toString = to.toString(KHealthUtils.fmt);
		System.out.println("From " + fromString);
		System.out.println("To " + toString);

		String observationType = ":AQIObservation";

		Model m = retrieveModel(fromString, toString, observationType);
		
		KHealthUtils.debug(m);

		String inner = "SELECT ?p ?year ?month ?day (avg(?qv) as ?aqi_avg) (max(?time) as ?current)"
				+ "WHERE { "
				+ "?obs a "
				+ observationType
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
						+ "CONSTRUCT { ?i a wea:AirPollution ; wea:hasValue ?aqi_avg ; :hasAssociatedDateTime ?current. }"
						+ "WHERE { "
						+ "?instant time:xsdDateTime ?time  . "
						+ "{ "
						+ inner
						+ "}"
						+ "bind ( URI(CONCAT(CONCAT(CONCAT(\"http://www.knoesis.org/khealth#aqi_\", STR(?year)),STR(?month)), STR(?day))) as ?i ) ."
						+ "}");

		return QueryExecutionFactory.create(query, m).execConstruct();

	}

}
