package org.knoesis.khealth.api;

import org.joda.time.DateTime;
import org.knoesis.khealth.utils.KHealthUtils;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.rdf.model.Model;

public class Drug {

	public static Model query() {

		String prefixes = "PREFIX : <http://www.knoesis.org/kheath#> "
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
				+ "PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#> "
				+ "PREFIX r: <http://knoesis.org/ric#> "
				+ "PREFIX asthma: <http://www.knoesis.org/khealth/asthma#> "
				+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
				+ "PREFIX time: <http://www.w3.org/2006/time#> ";
		String queryString = prefixes
				+ "CONSTRUCT { ?p a :Patient ; asthma:hasSymptom ?symptom . "
				+ "?symptom a asthma:Symptom ; :symptomDiagnosisTime ?time . } "
				+ "WHERE { "
				+ "?obs ssn:featureOfInterest ?p ; ssn:observationResultTime ?instant ; ssn:observationResult ?res ; ssn:observedProperty ?symptom ."
				+ "?instant time:xsdDateTime ?time ."
				+ "?symptom a asthma:Symptom . " + "?res ssn:hasValue ?val . "
				+ "?val :hasObservationValue ?qv ."
				+ "FILTER (?time > \"2015-06-19T00:00:00\"^^xsd:dateTime ) "
				+ "FILTER (?time < \"2015-06-21T00:00:00\"^^xsd:dateTime) "
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
	
	public static Model daylyQuery(DateTime d){
		return query(d,1);
	}
	
	public static Model weeklyQuery(DateTime d){
		return query(d,7);
	}
	
	public static Model query(DateTime d, int previous) {
		String prefixes = "PREFIX : <http://www.knoesis.org/kheath#> "
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
				+ "PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#> "
				+ "PREFIX r: <http://knoesis.org/ric#> "
				+ "PREFIX asthma: <http://www.knoesis.org/khealth/asthma#> "
				+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
				+ "PREFIX time: <http://www.w3.org/2006/time#> ";
		String queryString = prefixes
				+ "CONSTRUCT { ?p a :Patient ; asthma:hasSymptom ?symptom . "
				+ "?symptom a asthma:Symptom ; :symptomDiagnosisTime ?time . } "
				+ "WHERE { "
				+ "?obs ssn:featureOfInterest ?p ; ssn:observationResultTime ?instant ; ssn:observationResult ?res ; ssn:observedProperty ?symptom ."
				+ "?instant time:xsdDateTime ?time ."
				+ "?symptom a asthma:Symptom . " + "?res ssn:hasValue ?val . "
				+ "?val :hasObservationValue ?qv ."
				+ "FILTER (?time > \""+d.minusDays(previous).toString(KHealthUtils.fmt)+"\"^^xsd:dateTime ) "
				+ "FILTER (?time < \""+d.toString(KHealthUtils.fmt)+"\"^^xsd:dateTime) "
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
