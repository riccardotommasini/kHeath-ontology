package org.knoesis.khealth.api.questions;

import org.joda.time.DateTime;
import org.knoesis.khealth.utils.KHealthUtils;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.rdf.model.Model;

public class Drug {

	public static Model query() {

		String queryString = KHealthUtils.prefixes
				+ "CONSTRUCT { ?p a :Patient ; asthma:hasAppliedMedication ?drug ; asthma:hasControlLevel ?cl . "
				+ "?cl a ?clt . "
				+ "?drug a asthma:Drug ; :hasAssociatedTime ?time . } "
				+ "WHERE { "
				+ "?p asthma:hasControlLevel ?cl ."
				+ "?cl a ?clt . "
				+ "?obs ssn:featureOfInterest ?p ; ssn:observationResultTime ?instant ; ssn:observationResult ?res ; ssn:observedProperty ?drug ."
				+ "?instant time:xsdDateTime ?time ."
				+ "?drug a asthma:Drug . " + "?res ssn:hasValue ?val . "
				+ "?val :hasObservationValue ?qv ."
				+ "FILTER (?time > \"2015-06-16T00:00:00\"^^xsd:dateTime ) "
				+ "FILTER (?time < \"2015-06-27T00:00:00\"^^xsd:dateTime) "
				+ "FILTER ( ?qv = \"true\"^^xsd:boolean)" + "}";

		// create and initialize repo

		System.out.println(queryString);
		QueryExecution sparqlService = QueryExecutionFactory.sparqlService(
				KHealthUtils.ontop_endpoint, queryString);
		// ResultSet res = sparqlService.execSelect();

		Model m = sparqlService.execConstruct();
		sparqlService.close();
		return m;

	}

	public static Model daylyQuery(DateTime d) {
		return query(d, 1);
	}

	public static Model weeklyQuery(DateTime d) {
		return query(d, 7);
	}

	public static Model query(DateTime d, int previous) {

		String prefixes = "PREFIX : <http://www.knoesis.org/khealth#> "
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
				+ "PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#> "
				+ "PREFIX r: <http://knoesis.org/ric#> "
				+ "PREFIX asthma: <http://www.knoesis.org/khealth/asthma#> "
				+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
				+ "PREFIX time: <http://www.w3.org/2006/time#> ";
		String from = d.minusDays(previous).toString(KHealthUtils.fmt);
		String to = d.toString(KHealthUtils.fmt);
		System.out.println("From " + from);
		System.out.println("To " + to);
		String queryString = prefixes
				+ "CONSTRUCT { ?p a :Patient ; asthma:hasAppliedMedication ?drug ; asthma:hasControlLevel ?cl . "
				+ "?cl a ?clt . "
				+ "?drug a asthma:Drug ; :hasAssociatedTime ?time . } "
				+ "WHERE { "
				+ "?p asthma:hasControlLevel ?cl ."
				+ "?cl a ?clt . "
				+ "?obs ssn:featureOfInterest ?p ; ssn:observationResultTime ?instant ; ssn:observationResult ?res ; ssn:observedProperty ?drug ."
				+ "?instant time:xsdDateTime ?time ."
				+ "?drug a asthma:Drug . " + "?res ssn:hasValue ?val . "
				+ "?val :hasObservationValue ?qv ." + "FILTER (?time > \""
				+ from + "\"^^xsd:dateTime ) " + "FILTER (?time < \"" + to
				+ "\"^^xsd:dateTime) "
				+ "FILTER ( ?qv = \"true\"^^xsd:boolean)" + "}";

		// create and initialize repo

		System.out.println(queryString);
		QueryExecution sparqlService = QueryExecutionFactory.sparqlService(
				KHealthUtils.ontop_endpoint, queryString);
		// ResultSet res = sparqlService.execSelect();

		Model m = sparqlService.execConstruct();
		sparqlService.close();
		return m;

	}
}
