package org.knoesis.khealth.api.sensors.weather;

import org.joda.time.DateTime;
import org.knoesis.khealth.utils.KHealthUtils;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.rdf.model.Model;

public abstract class WeatherEndpoint {

	public  Model daylyQuery(DateTime d) {
		return query(d, 1);
	}

	public  Model query(DateTime to, int previous) {
		return query(to.minusDays(previous), to);
	}

	public abstract  Model query(DateTime from, DateTime to);

	public  Model retrieveModel(String fromString, String toString,
			String observationType) {
		
		String queryString = "PREFIX wea: <https://www.auto.tuwien.ac.at/downloads/thinkhome/ontology/WeatherOntology.owl#> "
				+ "PREFIX : <http://www.knoesis.org/khealth#> "
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
				+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
				+ "PREFIX time: <http://www.w3.org/2006/time#> "
				+ "PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#> "
				+ "PREFIX asthma: <http://www.knoesis.org/khealth/asthma#> "
				+ "CONSTRUCT { "
				+ "?obs a "
				+ observationType
				+ " ; ssn:featureOfInterest ?p ; ssn:observationResult ?res ; ssn:observationResultTime ?instant ."
				+ "?res ssn:hasValue ?val . "
				+ "?val :hasObservationValue ?qv . "
				+ "?instant time:xsdDateTime ?time . "
				+ "} "
				+ "WHERE {"
				+ "?obs a "
				+ observationType
				+ " ; ssn:featureOfInterest ?p ; ssn:observationResult ?res ; ssn:observationResultTime ?instant ."
				+ "?res ssn:hasValue ?val . "
				+ "?val :hasObservationValue ?qv . "
				+ "?instant time:xsdDateTime ?time ."
				+ "FILTER (?time >= \""
				+ fromString
				+ "\"^^xsd:dateTime) "
				+ "FILTER (?time <= \""
				+ toString + "\"^^xsd:dateTime) }";

		System.out.println(queryString);
		QueryExecution sparqlService = QueryExecutionFactory.sparqlService(
				KHealthUtils.ontop_endpoint, queryString);
		// ResultSet res = sparqlService.execSelect();

		Model m = sparqlService.execConstruct();
		sparqlService.close();
		return m;
	}


}
