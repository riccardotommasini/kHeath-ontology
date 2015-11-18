package org.knoesis.khealth.api.questions.seq;

import org.joda.time.DateTime;
import org.knoesis.khealth.api.questions.causes.Drug;
import org.knoesis.khealth.utils.KHealthUtils;

import com.hp.hpl.jena.rdf.model.Model;

public class TreatementLevel {

	public Model insermittentTreatment(DateTime from, DateTime to) {

		String timestamp = DateTime.now().toString(KHealthUtils.fmt);
		String inner = "SELECT ?p "
				+ "WHERE { ?p asthma:hasAppliedMedication ?d . } "
				+ "GROUP BY (?p) " + "HAVING (count(distinct ?d) < 2) }";

		String queryString = KHealthUtils.prefixes
				+ "CONSTRUCT { "
				+ "?p asthma:hasAppliedTreatment ?g . "
				+ "?g a asthma:IntermittentAsthmaTreatement ; asthma:treatmentApplicationTime \""
				+ timestamp + "\"^^xsd:dateTime . " + "} " + "WHERE { "
				+ "?p a :Patient "
				+ "BIND (URI(REPLACE(STR(?p), \"patient\", \"treatment"
				+ timestamp + "\")) as ?g) ." + "{ " + inner + " }";

		Model model = new Drug().query(from, to);

		return KHealthUtils.executeConstruct("insermittentTreatment ",
				queryString, model);

	}

	public Model mildTreatment(DateTime from, DateTime to) {

		String timestamp = DateTime.now().toString(KHealthUtils.fmt);
		String inner = "SELECT ?p "
				+ "WHERE { ?p asthma:hasAppliedMedication ?d . } "
				+ "GROUP BY (?p) " + "HAVING (count(distinct ?d) >= 2) }";

		String queryString = KHealthUtils.prefixes
				+ "CONSTRUCT { "
				+ "?p asthma:hasAppliedTreatment ?g . "
				+ "?g a asthma:MildPersistentAsthmaTreatement ; asthma:treatmentApplicationTime \""
				+ timestamp + "\"^^xsd:dateTime . " + "} " + "WHERE { "
				+ "?p a :Patient "
				+ "BIND (URI(REPLACE(STR(?p), \"patient\", \"treatment"
				+ timestamp + "\")) as ?g) ." + "{ " + inner + " }";

		Model model = new Drug().query(from, to);

		System.err.println(queryString);

		return KHealthUtils.executeConstruct("mildTreatment ", queryString,
				model);

	}

	public Model severeTreatment(DateTime from, DateTime to) {

		String timestamp = DateTime.now().toString(KHealthUtils.fmt);
		String inner = " SELECT ?p (count(?day) as ?days) " + "WHERE { "
				+ "?p asthma:hasAppliedMedication ?d . "
				+ "?d :hasAssociatedTime ?time " + ". "
				+ "bind( day(xsd:dateTime(?time)) as ?day ) . } "
				+ "GROUP BY ?p ?day HAVING(count(distinct ?d) > 0) ";

		String outer = " SELECT ?p " + "WHERE {" + inner + "}"
				+ " GROUP BY ?p " + "HAVING (count(?days) > 0)";

		String queryString = KHealthUtils.prefixes
				+ " CONSTRUCT { "
				+ "?p asthma:hasAppliedTreatment ?g ."
				+ " ?g a asthma:SeverePersistentAsthmaTreatement ; asthma:treatmentApplicationTime \""
				+ timestamp + "\"^^xsd:dateTime . " + "} " + "WHERE { "
				+ "?p a :Patient . "
				+ "BIND (URI(REPLACE(STR(?p), \"patient\", \"treatment"
				+ timestamp + "\")) as ?g) . " + "{" + outer + " }}";

		Model model = new Drug().query(from, to);

		return KHealthUtils.executeConstruct("severeTreatment ", queryString,
				model);

	}
}
