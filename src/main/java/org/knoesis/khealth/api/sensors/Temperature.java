package org.knoesis.khealth.api.sensors;

import org.joda.time.DateTime;
import org.knoesis.khealth.utils.KHealthUtils;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.Model;

public class Temperature {

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
				+ "?obs a :TemperatureObservation ; ssn:observationResult ?res ; ssn:observationResultTime ?instant ."
				+ "?res ssn:hasValue ?val . "
				+ "?val :hasObservationValue ?qv . "
				+ "?instant time:xsdDateTime ?time ."
				+ "}"
				+ "WHERE {"
				+ "?obs a :TemperatureObservation ; ssn:observationResult ?res ; ssn:observationResultTime ?instant ."
				+ "?res ssn:hasValue ?val . "
				+ "?val :hasObservationValue ?qv . "
				+ "?instant time:xsdDateTime ?time ."
				+ "FILTER (?time >= \""
				+ from
				+ "\"^^xsd:dateTime) "
				+ "FILTER (?time < \""
				+ to
				+ "\"^^xsd:dateTime) }";

		System.out.println(queryString);
		QueryExecution sparqlService = QueryExecutionFactory.sparqlService(
				KHealthUtils.ontop_endpoint, queryString);
		// ResultSet res = sparqlService.execSelect();

		Model m = sparqlService.execConstruct();
		sparqlService.close();

		String inner = "SELECT (avg(?qv) as ?temp_avg) " + "WHERE { "
				+ "?val :hasObservationValue ?qv . "
				+ "?instant time:xsdDateTime ?time . "
				+ "bind ( day(xsd:dateTime(?time)) as ?day ) } "
				+ "GROUP BY ?day";

		Query query = QueryFactory
				.create("PREFIX wea: <https://www.auto.tuwien.ac.at/downloads/thinkhome/ontology/WeatherOntology.owl#> "
						+ "PREFIX : <http://www.knoesis.org/khealth#> "
						+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
						+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
						+ "PREFIX time: <http://www.w3.org/2006/time#> "
						+ "PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#> "
						+ "PREFIX asthma: <http://www.knoesis.org/khealth/asthma#> "
						+ "CONSTRUCT { :weather\\/temp\\/"
						+ KHealthUtils.now()
						+ " a :Temperature ; wea:hasValue ?temp_avg . }"
						+ "WHERE { " + inner + "}");

		return QueryExecutionFactory.create(query, m).execConstruct();

	}
}
