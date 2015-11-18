package org.knoesis.khealth.api.questions;

import org.joda.time.DateTime;
import org.knoesis.khealth.utils.KHealthUtils;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.rdf.model.Model;

public class Drug implements QuestionEndpoint {

	public  Model daylyQuery(DateTime d) {
		return query(d, 1);
	}

	public  Model weeklyQuery(DateTime d) {
		return query(d, 7);
	}

	public  Model query(DateTime d, int previous) {

		return query(d.minusDays(previous), d);
	}

	public  Model query(DateTime from, DateTime to) {

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
				+ "?val :hasObservationValue ?qv ." + "FILTER (?time > \""
				+ from.toString(KHealthUtils.fmt) + "\"^^xsd:dateTime ) "
				+ "FILTER (?time < \"" + to.toString(KHealthUtils.fmt)
				+ "\"^^xsd:dateTime) "
				+ "FILTER ( ?qv = \"true\"^^xsd:boolean)" + "}";

		System.out.println(queryString);
		QueryExecution sparqlService = QueryExecutionFactory.sparqlService(
				KHealthUtils.ontop_endpoint, queryString);
		// ResultSet res = sparqlService.execSelect();

		Model m = sparqlService.execConstruct();
		sparqlService.close();
		return m;

	}
}
