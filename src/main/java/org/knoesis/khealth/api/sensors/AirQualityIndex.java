package org.knoesis.khealth.api.sensors;

import org.joda.time.DateTime;
import org.knoesis.khealth.utils.KHealthUtils;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;

public class AirQualityIndex {

	public static Model daylyQuery(DateTime d) {
		return query(d, 1);
	}

	public static Model query(DateTime d, int previous) {

		String from = d.minusDays(previous).toString(KHealthUtils.fmt);
		String to = d.toString(KHealthUtils.fmt);
		System.out.println("From " + from);
		System.out.println("To " + to);

		String queryString = "PREFIX wea: <https://www.auto.tuwien.ac.at/downloads/thinkhome/ontology/WeatherOntology.owl#> "
				+ "PREFIX : <http://www.knoesis.org/khealth#> "
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
				+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
				+ "PREFIX time: <http://www.w3.org/2006/time#> "
				+ "PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#> "
				+ "PREFIX asthma: <http://www.knoesis.org/khealth/asthma#> "
				+ "CONSTRUCT { "
				+ "?obs a :AQIObservation ; ssn:featureOfInterest ?p ; ssn:observationResult ?res ; ssn:observationResultTime ?instant ."
				+ "?res ssn:hasValue ?val . "
				+ "?val :hasObservationValue ?qv . "
				+ "?instant time:xsdDateTime ?time . "
				+ "}"
				+ "WHERE {"
				+ "?obs a :AQIObservation ; ssn:featureOfInterest ?p ; ssn:observationResult ?res ; ssn:observationResultTime ?instant ."
				+ "?res ssn:hasValue ?val . "
				+ "?val :hasObservationValue ?qv . "
				+ "?instant time:xsdDateTime ?time ."
				+ "FILTER (?time >= \""
				+ from
				+ "\"^^xsd:dateTime) "
				+ "FILTER (?time <= \""
				+ to
				+ "\"^^xsd:dateTime) }";

		System.out.println(queryString);
		QueryExecution sparqlService = QueryExecutionFactory.sparqlService(
				KHealthUtils.ontop_endpoint, queryString);
		// ResultSet res = sparqlService.execSelect();

		Model m = sparqlService.execConstruct();
		sparqlService.close();
		String inner = "SELECT ?p ?year ?month ?day (avg(?qv) as ?aqi_avg) "
				+ "WHERE { "
				+ "?obs a :AQIObservation ; ssn:featureOfInterest ?p ; ssn:observationResult ?res ; ssn:observationResultTime ?instant ."
				+ "?res ssn:hasValue ?val . "
				+ "?val :hasObservationValue ?qv . "
				+ "?instant time:xsdDateTime ?time ."
				+ "bind ( day(xsd:dateTime(?time)) as ?day )"
				+ "bind ( month(xsd:dateTime(?time)) as ?month )"
				+ "bind ( year(xsd:dateTime(?time)) as ?year ) } "
				+ "GROUP BY ?p ?year ?month ?day "
				+ "ORDER BY ?p ?year ?month ?day ";

		Query sel = QueryFactory
				.create("PREFIX wea: <https://www.auto.tuwien.ac.at/downloads/thinkhome/ontology/WeatherOntology.owl#> "
						+ "PREFIX : <http://www.knoesis.org/khealth#> "
						+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
						+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
						+ "PREFIX time: <http://www.w3.org/2006/time#> "
						+ "PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#> "
						+ "PREFIX asthma: <http://www.knoesis.org/khealth/asthma#> "
						+ inner);
		ResultSet res = QueryExecutionFactory.create(sel, m).execSelect();
		ResultSetFormatter.out(System.out, res, sel);

		Query query = QueryFactory
				.create("PREFIX wea: <https://www.auto.tuwien.ac.at/downloads/thinkhome/ontology/WeatherOntology.owl#> "
						+ "PREFIX : <http://www.knoesis.org/khealth#> "
						+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
						+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
						+ "PREFIX time: <http://www.w3.org/2006/time#> "
						+ "PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#> "
						+ "PREFIX asthma: <http://www.knoesis.org/khealth/asthma#> "
						+ "CONSTRUCT { ?i "
						+ " a wea:AirPollution ; wea:hasValue ?aqi_avg ; :hasAssociatedDateTime ?time. }"
						+ "WHERE { "
						+ "?instant time:xsdDateTime ?time  "
						+ "BIND (URI (CONCAT(STR(:aiq\\/), STR(?year) , STR(?month) ,STR(?day) )) as ?i) { "
						+ inner + "}}");

		return QueryExecutionFactory.create(query, m).execConstruct();

	}
}
