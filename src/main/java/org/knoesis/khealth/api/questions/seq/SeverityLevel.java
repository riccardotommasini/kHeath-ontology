package org.knoesis.khealth.api.questions.seq;

import org.joda.time.DateTime;
import org.knoesis.khealth.api.questions.causes.SleepDisorder;
import org.knoesis.khealth.api.questions.causes.Symptoms;
import org.knoesis.khealth.utils.KHealthUtils;

import com.hp.hpl.jena.graph.compose.Union;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.impl.ModelCom;

public class SeverityLevel {

	public Model intermittentAsthmaCheck(DateTime from, DateTime to) {

		String timestamp = DateTime.now().toString(KHealthUtils.fmt);

		String inner_symptoms = " SELECT distinct ?p ?year ?month (count(distinct ?s) as ?symptoms)"
				+ "WHERE { ?p asthma:hasSymptom ?s . "
				+ "?s :symptomDiagnosisTime ?time .  "
				+ "BIND ( month(xsd:dateTime(?time)) as ?month ) "
				+ "BIND ( year(xsd:dateTime(?time)) as ?year ) } "
				+ "GROUP BY ?p ?year ?month ";

		String inner_sleep = " SELECT distinct ?p ?year ?month (count(distinct ?s) as ?disturbs) "
				+ "WHERE {?p asthma:hasSleepDisorder ?s . "
				+ "?s :sleepdisDiagnosisTime ?time . "
				+ "BIND ( month(xsd:dateTime(?time)) as ?month ) "
				+ "BIND ( year(xsd:dateTime(?time)) as ?year ) } "
				+ "GROUP BY ?p ?year ?month ";

		String queryString = KHealthUtils.prefixes
				+ "CONSTRUCT { "
				+ "?p a :Patient; asthma:hasSeveryLevel ?g ."
				+ "?g a asthma:IntermittenAsthma ; asthma:severityDiagnosisTime \""
				+ timestamp + "\"^^xsd:dateTime . } " + "WHERE { "
				+ "?p a :Patient . "
				+ "BIND (URI(REPLACE(STR(?p), \"patient\", \"severity"
				+ timestamp + "\")) as ?g ) ." + "{ " + inner_sleep
				+ " HAVING (count(distinct ?s) < 2)" + "}" + " { "
				+ inner_symptoms + " HAVING (count(distinct ?s) < 2)" + "}"
				+ "}";

		Model symptomModel = new Symptoms().query(from, to);
		Model sleepModel = new SleepDisorder().query(from, to);

		KHealthUtils.debug(symptomModel, KHealthUtils.prefixes + inner_symptoms
				+ " ORDER BY ?p ?year ?month ");
		KHealthUtils.debug(sleepModel, KHealthUtils.prefixes + inner_sleep
				+ " ORDER BY ?p ?year ?month ");

		Union u = new Union(sleepModel.getGraph(), symptomModel.getGraph());

		Model model = new ModelCom(u);

		return KHealthUtils.executeConstruct("Intermittent Asthma Check ",
				queryString, model);
	}

	public Model mildPersistentAsthmaCheck(DateTime from, DateTime to) {

		String timestamp = DateTime.now().toString(KHealthUtils.fmt);

		String inner_symptoms = " SELECT distinct ?p ?year ?month (count(distinct ?s) as ?symptoms)"
				+ "WHERE { ?p asthma:hasSymptom ?s . "
				+ "?s :symptomDiagnosisTime ?time .  "
				+ "BIND ( month(xsd:dateTime(?time)) as ?month ) "
				+ "BIND ( year(xsd:dateTime(?time)) as ?year ) } "
				+ "GROUP BY ?p ?year ?month " + "";

		String inner_sleep = " SELECT distinct ?p ?year ?month (count(distinct ?s) as ?disturbs)"
				+ "WHERE {?p asthma:hasSleepDisorder ?s . "
				+ "?s :sleepdisDiagnosisTime ?time . "
				+ "BIND ( month(xsd:dateTime(?time)) as ?month ) "
				+ "BIND ( year(xsd:dateTime(?time)) as ?year ) } "
				+ "GROUP BY ?p ?year ?month ";

		String queryString = KHealthUtils.prefixes
				+ "CONSTRUCT { "
				+ "?p a :Patient; asthma:hasSeveryLevel ?g ."
				+ "?g a asthma:MildPersistentAsthma ; asthma:severityDiagnosisTime \""
				+ timestamp + "\"^^xsd:dateTime . } " + "WHERE { "
				+ "?p a :Patient . "
				+ "BIND (URI(REPLACE(STR(?p), \"patient\", \"severity"
				+ timestamp + "\")) as ?g ) ." + "{ " + inner_sleep
				+ " HAVING (count(distinct ?s) < 3)" + "} " + "{ "
				+ inner_symptoms + " HAVING (count(distinct ?s) > 1)" + "}"
				+ "}";

		Model symptomModel = new Symptoms().query(from, to);
		Model sleepModel = new SleepDisorder().query(from, to);
		KHealthUtils.debug(symptomModel, KHealthUtils.prefixes + inner_symptoms
				+ " ORDER BY ?p ?year ?month ");
		KHealthUtils.debug(sleepModel, KHealthUtils.prefixes + inner_sleep
				+ " ORDER BY ?p ?year ?month ");
		Union u = new Union(sleepModel.getGraph(), symptomModel.getGraph());

		Model model = new ModelCom(u);

		return KHealthUtils.executeConstruct("Mild Asthma Check ", queryString,
				model);
	}

	public Model severePersistentAsthmaCheck(DateTime from, DateTime to) {

		String timestamp = DateTime.now().toString(KHealthUtils.fmt);

		String inner_symptoms = " SELECT distinct ?p ?year ?month ?day (count(distinct ?s) as ?symptoms)"
				+ "WHERE { ?p asthma:hasSymptom ?s . "
				+ "?s :symptomDiagnosisTime ?time .  "
				+ "BIND ( day(xsd:dateTime(?time)) as ?day ) "
				+ "BIND ( month(xsd:dateTime(?time)) as ?month ) "
				+ "BIND ( year(xsd:dateTime(?time)) as ?year ) } "
				+ "GROUP BY ?p ?year ?month ?day ";

		String inner_sleep = " SELECT distinct ?p ?year ?month (count(distinct ?s) as ?disturbs)"
				+ "WHERE {?p asthma:hasSleepDisorder ?s . "
				+ "?s :sleepdisDiagnosisTime ?time . "
				+ "BIND ( day(xsd:dateTime(?time)) as ?day ) "
				+ "BIND ( month(xsd:dateTime(?time)) as ?month ) "
				+ "BIND ( year(xsd:dateTime(?time)) as ?year ) } "
				+ "GROUP BY ?p ?year ?month ";

		String queryString = KHealthUtils.prefixes
				+ "CONSTRUCT { "
				+ "?p a :Patient; asthma:hasSeveryLevel ?g ."
				+ "?g a asthma:SeverePersistentAsthma ; asthma:severityDiagnosisTime \""
				+ timestamp + "\"^^xsd:dateTime . } " + "WHERE { "
				+ "?p a :Patient . "
				+ "BIND (URI(REPLACE(STR(?p), \"patient\", \"severity"
				+ timestamp + "\")) as ?g ) ." + "{ " + inner_sleep
				+ " HAVING (count(distinct ?s) > 3)" + "} " + "{ "
				+ inner_symptoms + " HAVING (count(distinct ?s) > 0)" + "}"
				+ "}";

		Model symptomModel = new Symptoms().query(from, to);
		Model sleepModel = new SleepDisorder().query(from, to);
		KHealthUtils.debug(symptomModel, KHealthUtils.prefixes + inner_symptoms
				+ " ORDER BY ?p ?year ?month ?day ");
		KHealthUtils.debug(sleepModel, KHealthUtils.prefixes + inner_sleep
				+ " ORDER BY ?p ?year ?month ?day ");
		Union u = new Union(sleepModel.getGraph(), symptomModel.getGraph());

		Model model = new ModelCom(u);

		return KHealthUtils.executeConstruct("Severe Asthma Check ",
				queryString, model);
	}
}
