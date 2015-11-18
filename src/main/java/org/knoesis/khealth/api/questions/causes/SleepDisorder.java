package org.knoesis.khealth.api.questions.causes;

import org.joda.time.DateTime;
import org.knoesis.khealth.api.questions.QuestionEndpoint;
import org.knoesis.khealth.utils.KHealthUtils;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.rdf.model.Model;

public class SleepDisorder implements QuestionEndpoint {

	public Model daylyQuery(DateTime d) {
		return query(d, 1);
	}

	public Model weeklyQuery(DateTime d) {
		return query(d, 7);
	}

	public Model query(DateTime d, int previous) {

		return query(d.minusDays(previous), d);
	}

	public Model query(DateTime from, DateTime to) {

		String prefixes = "PREFIX : <http://www.knoesis.org/khealth#> "
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
				+ "PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#> "
				+ "PREFIX r: <http://knoesis.org/ric#> "
				+ "PREFIX asthma: <http://www.knoesis.org/khealth/asthma#> "
				+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
				+ "PREFIX time: <http://www.w3.org/2006/time#> ";
		String queryString = prefixes
				+ "CONSTRUCT { ?p a :Patient ; asthma:hasSleepDisorder ?sleepdis . "
				+ "?sleepdis a asthma:SleepDisorder ; :sleepdisDiagnosisTime ?time . } "
				+ "WHERE { "
				+ "?obs ssn:featureOfInterest ?p ; ssn:observationResultTime ?instant ; ssn:observationResult ?res ; ssn:observedProperty ?sleepdis ."
				+ "?instant time:xsdDateTime ?time ."
				+ "?sleepdis a asthma:SleepDisorder . "
				+ "?res ssn:hasValue ?val . "
				+ "?val :hasObservationValue ?qv ." + "FILTER (?time > \""
				+ from.toString(KHealthUtils.fmt) + "\"^^xsd:dateTime ) "
				+ "FILTER (?time < \"" + to.toString(KHealthUtils.fmt)
				+ "\"^^xsd:dateTime) "
				+ "FILTER ( ?qv = \"true\"^^xsd:boolean)" + "}";

		// create and initialize repo
		System.err.println(queryString);
		QueryExecution sparqlService = QueryExecutionFactory.sparqlService(
				KHealthUtils.ontop_endpoint, queryString);
		// ResultSet res = sparqlService.execSelect();

		Model m = sparqlService.execConstruct();
		sparqlService.close();
		return m;
	}
}
