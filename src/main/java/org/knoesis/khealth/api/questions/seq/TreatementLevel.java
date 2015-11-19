package org.knoesis.khealth.api.questions.seq;

import org.joda.time.DateTime;
import org.knoesis.khealth.api.questions.causes.Drug;
import org.knoesis.khealth.utils.KHealthUtils;

import com.hp.hpl.jena.rdf.model.Model;

public class TreatementLevel {

	public Model insermittentTreatment(DateTime from, DateTime to) {

		String timestamp = DateTime.now().toString(KHealthUtils.fmt);
		String inner = "SELECT ?p ?year ?month (count(distinct ?d) as ?medicines)"
				+ "WHERE {?p asthma:hasAppliedMedication ?d ."
				+ "?d :hasAssociatedTime ?time .  "
				+ "BIND ( month(xsd:dateTime(?time)) as ?month ) "
				+ "BIND ( year(xsd:dateTime(?time)) as ?year ) "
				+ "} "
				+ "GROUP BY ?p ?year ?month ";

		String queryString = KHealthUtils.prefixes
				+ "CONSTRUCT { "
				+ "?p asthma:hasAppliedTreatment ?g . "
				+ "?g a asthma:IntermittentAsthmaTreatement ; asthma:treatmentApplicationTime \""
				+ timestamp + "\"^^xsd:dateTime . " + "} " + "WHERE { "
				+ "?p a :Patient "
				+ "BIND (URI(REPLACE(STR(?p), \"patient\", \"treatment"
				+ timestamp + "\")) as ?g) ." + "{ " + inner
				+ " HAVING (count(distinct ?d) < 2) }}";

		Model model = new Drug().query(from, to);

		KHealthUtils.debug(model, KHealthUtils.prefixes + inner
				+ " ORDER BY ?p ?year ?month ");

		return KHealthUtils.executeConstruct(
				"Intermittent Treatment Application ", queryString, model);

	}

	public Model mildTreatment(DateTime from, DateTime to) {

		String timestamp = DateTime.now().toString(KHealthUtils.fmt);
		String inner = "SELECT ?p ?year ?month (count(distinct ?d) as ?medicines)"
				+ "WHERE {?p asthma:hasAppliedMedication ?d ."
				+ "?d :hasAssociatedTime ?time .  "
				+ "BIND ( month(xsd:dateTime(?time)) as ?month ) "
				+ "BIND ( year(xsd:dateTime(?time)) as ?year ) "
				+ "} "
				+ "GROUP BY ?p ?year ?month ";

		String queryString = KHealthUtils.prefixes
				+ "CONSTRUCT { "
				+ "?p asthma:hasAppliedTreatment ?g . "
				+ "?g a asthma:MildPersistentAsthmaTreatement ; asthma:treatmentApplicationTime \""
				+ timestamp + "\"^^xsd:dateTime . " + "} " + "WHERE { "
				+ "?p a :Patient "
				+ "BIND (URI(REPLACE(STR(?p), \"patient\", \"treatment"
				+ timestamp + "\")) as ?g) ." + "{ " + inner
				+ " HAVING (count(distinct ?d) > 1) }}";

		Model model = new Drug().query(from, to);

		KHealthUtils.debug(model, KHealthUtils.prefixes + inner
				+ " ORDER BY ?p ?year ?month ");

		return KHealthUtils.executeConstruct("Mild Treatment Application ",
				queryString, model);

	}

	public Model severeTreatment(DateTime from, DateTime to) {

		String timestamp = DateTime.now().toString(KHealthUtils.fmt);
		String inner = "SELECT ?p ?year ?month  ?day (count(distinct ?d) as ?medicines)"
				+ "WHERE {?p asthma:hasAppliedMedication ?d ."
				+ "?d :hasAssociatedTime ?time .  "
				+ "BIND ( day(xsd:dateTime(?time)) as ?day ) "
				+ "BIND ( month(xsd:dateTime(?time)) as ?month ) "
				+ "BIND ( year(xsd:dateTime(?time)) as ?year ) "
				+ "} "
				+ "GROUP BY ?p ?year ?month  ?day ";

		String queryString = KHealthUtils.prefixes
				+ "CONSTRUCT { "
				+ "?p asthma:hasAppliedTreatment ?g . "
				+ "?g a asthma:SeverePersistentAsthmaTreatement ; asthma:treatmentApplicationTime \""
				+ timestamp + "\"^^xsd:dateTime . " + "} " + "WHERE { "
				+ "?p a :Patient "
				+ "BIND (URI(REPLACE(STR(?p), \"patient\", \"treatment"
				+ timestamp + "\")) as ?g) ." + "{ " + inner
				+ " HAVING (count(distinct ?d) > 1) }}";

		Model model = new Drug().query(from, to);

		KHealthUtils.debug(model, KHealthUtils.prefixes + inner
				+ " ORDER BY ?p ?year ?month ");

		return KHealthUtils.executeConstruct("Severe Treatment Application ",
				queryString, model);

	}
}
