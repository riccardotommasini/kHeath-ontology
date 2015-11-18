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

		String inner2 = " SELECT ?p " + " WHERE { ?p asthma:hasSymptom ?s . } "
				+ "GROUP BY (?p) " + " HAVING (count(distinct ?s) = 0) ";

		String inner1 = " SELECT ?p "
				+ "WHERE { ?p asthma:hasSleepDisorder ?s . } "
				+ "GROUP BY (?p) " + "HAVING (count(distinct ?s) < 2) ";

		String queryString = KHealthUtils.prefixes
				+ "CONSTRUCT { "
				+ "?p a :Patient; asthma:hasSeveryLevel ?g ."
				+ "?g a asthma:IntermittenAsthma ; asthma:severityDiagnosisTime \""
				+ timestamp + "\"^^xsd:dateTime . } " + "WHERE { "
				+ "?p a :Patient . "
				+ "BIND (URI(REPLACE(STR(?p), \"patient\", \"severity"
				+ timestamp + "\")) as ?g ) ." + "{ " + inner1 + "} { "
				+ inner2 + "}}";

		Model symptomModel = new Symptoms().query(from, to);
		Model sleepModel = new SleepDisorder().query(from, to);

		Union u = new Union(sleepModel.getGraph(), symptomModel.getGraph());

		Model model = new ModelCom(u);

		return KHealthUtils.executeConstruct("intermittentAsthmaCheck",
				queryString, model);
	}

	public Model mildPersistentAsthmaCheck(DateTime from, DateTime to) {

		String timestamp = DateTime.now().toString(KHealthUtils.fmt);

		String levelInstance = ":severity\\/" + timestamp + "\\/1";

		String queryString = KHealthUtils.prefixes
				+ "CONSTRUCT { "
				+ "?p a :Patient; asthma:hasSeveryLevel  "
				+ levelInstance
				+ " ."
				+ levelInstance
				+ " a asthma:MildPersistentAsthma ; asthma:severityDiagnosisTime \""
				+ timestamp + "\"^^xsd:dateTime . } " + "WHERE { "
				+ "?p a :Patient . " + "{ " + "SELECT ?p "
				+ "WHERE { ?p asthma:hasSleepDisorder ?s . } "
				+ "GROUP BY (?p) " + "HAVING (count(distinct ?s) <= 2) } "
				+ "{ " + "SELECT ?p " + " WHERE { ?p asthma:hasSymptom ?s . } "
				+ "GROUP BY (?p) " + " HAVING (count(distinct ?s) >= 2)}}";

		Model symptomModel = new Symptoms().query(from, to);
		Model sleepModel = new SleepDisorder().query(from, to);

		Union u = new Union(sleepModel.getGraph(), symptomModel.getGraph());

		Model model = new ModelCom(u);

		return KHealthUtils.executeConstruct("mildPersistentAsthmaCheck",
				queryString, model);
	}

	public Model severePersistentAsthmaCheck(DateTime from, DateTime to) {

		String timestamp = DateTime.now().toString(KHealthUtils.fmt);

		String levelInstance = ":severity\\/" + timestamp + "\\/3";

		String queryString = KHealthUtils.prefixes
				+ "CONSTRUCT { "
				+ "?p a :Patient; asthma:hasSeveryLevel "
				+ levelInstance
				+ " ."
				+ levelInstance
				+ " a asthma:SeverePersistentAsthma ; asthma:severityDiagnosisTime \""
				+ timestamp + "\"^^xsd:dateTime . } " + "WHERE { "
				+ "?p a :Patient . " + "{ " + "SELECT ?p "
				+ "WHERE { ?p asthma:hasSleepDisorder ?s . } "
				+ "GROUP BY (?p) " + "HAVING (count(distinct ?s) >= 0) } "
				+ "{ " + "SELECT ?p " + " WHERE { ?p asthma:hasSymptom ?s . } "
				+ "GROUP BY (?p) " + " HAVING (count(distinct ?s) >= 0)}}";

		Model symptomModel = new Symptoms().query(from, to);
		Model sleepModel = new SleepDisorder().query(from, to);

		Union u = new Union(sleepModel.getGraph(), symptomModel.getGraph());

		Model model = new ModelCom(u);

		return KHealthUtils.executeConstruct("severePersistentAsthmaCheck",
				queryString, model);
	}

}
