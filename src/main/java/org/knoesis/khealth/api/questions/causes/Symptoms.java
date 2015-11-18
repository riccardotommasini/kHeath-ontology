package org.knoesis.khealth.api.questions.causes;

import org.joda.time.DateTime;
import org.knoesis.khealth.api.questions.QuestionEndpoint;
import org.knoesis.khealth.utils.KHealthUtils;

import com.hp.hpl.jena.rdf.model.Model;

public class Symptoms implements QuestionEndpoint {

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

		String queryString = KHealthUtils.prefixes
				+ "CONSTRUCT { ?p a :Patient ; asthma:hasSymptom ?symptom . "
				+ "?symptom a asthma:Symptom ; :symptomDiagnosisTime ?time . } "
				+ "WHERE { "
				+ "?obs ssn:featureOfInterest ?p ; ssn:observationResultTime ?instant ; ssn:observationResult ?res ; ssn:observedProperty ?symptom ."
				+ "?instant time:xsdDateTime ?time ."
				+ "?symptom a asthma:Symptom . " + "?res ssn:hasValue ?val . "
				+ "?val :hasObservationValue ?qv . " + "FILTER (?time > \""
				+ from.toString(KHealthUtils.fmt) + "\"^^xsd:dateTime ) "
				+ "FILTER (?time < \"" + to.toString(KHealthUtils.fmt)
				+ "\"^^xsd:dateTime) "
				+ "FILTER ( ?qv = \"true\"^^xsd:boolean)" + "}";

		return KHealthUtils
				.executeConstruct("Symptoms Occurence ", queryString);
	}

}
