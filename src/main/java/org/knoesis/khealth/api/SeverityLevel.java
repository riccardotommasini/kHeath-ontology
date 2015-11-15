package org.knoesis.khealth.api;

import java.util.Iterator;

import org.joda.time.DateTime;
import org.knoesis.khealth.utils.KHealthUtils;

import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.graph.compose.Union;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.impl.ModelCom;

public class SeverityLevel {

	private static final String prefixes = "PREFIX : <http://www.knoesis.org/kheath#> "
			+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
			+ "PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#> "
			+ "PREFIX r: <http://knoesis.org/ric#> "
			+ "PREFIX asthma: <http://www.knoesis.org/khealth/asthma#> "
			+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
			+ "PREFIX k: <http://www.knoesis.org/kheath/>";

	public static void main(String[] args) {
		intermittentAsthmaCheck();
		mildPersistentAsthmaCheck();
		severePersistentAsthmaCheck();
	}

	public static void intermittentAsthmaCheck() {

		String timestamp = DateTime.now().toString(KHealthUtils.fmt);

		String levelInstance = "k:\\/severity\\/" + timestamp + "\\/0";

		String intermittentAsthma = prefixes
				+ "CONSTRUCT { "
				+ "?p a :Patient; asthma:hasSeveryLevel "
				+ levelInstance
				+ " ."
				+ levelInstance
				+ " a asthma:IntermittenAsthma ; asthma:severityDiagnosisTime \""
				+ timestamp + "\"^^xsd:dateTime . } " + "WHERE { "
				+ "?p a :Patient . " + "{ " + "SELECT ?p "
				+ "WHERE { ?p asthma:hasSleepDisorder ?s . } "
				+ "GROUP BY (?p) " + "HAVING (count(distinct ?s) < 2) } "
				+ "{ " + "SELECT ?p " + " WHERE { ?p asthma:hasSymptom ?s . } "
				+ "GROUP BY (?p) " + " HAVING (count(distinct ?s) = 0)}}";

		Model symptomModel = Symptoms.query(
				DateTime.parse("2015-06-21T00:00:00"), 7);
		Model sleepModel = SleepDisorder.query(
				DateTime.parse("2015-06-21T00:00:00"), 7);

		Union u = new Union(sleepModel.getGraph(), symptomModel.getGraph());

		Model model = new ModelCom(u);

		System.out.println("-----Debug------");
		Query query = QueryFactory.create("SELECT ?s ?p ?o WHERE {?s ?p ?o}");
		ResultSet results = QueryExecutionFactory.create(query, model)
				.execSelect();
		ResultSetFormatter.out(System.out, results, query);

		System.out.println(intermittentAsthma);

		System.out.println("-----Debug End------");

		query = QueryFactory.create(intermittentAsthma);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		Iterator<Triple> execConstructTriples = qexec.execConstructTriples();
		while (execConstructTriples.hasNext()) {
			System.out.println(execConstructTriples.next());
		}
	}

	public static void mildPersistentAsthmaCheck() {

		String timestamp = DateTime.now().toString(KHealthUtils.fmt);

		String levelInstance = "k:\\/severity\\/" + timestamp + "\\/1";

		String intermittentAsthma = prefixes
				+ "CONSTRUCT { "
				+ "?p a :Patient; asthma:hasSeveryLevel "
				+ levelInstance
				+ " ."
				+ levelInstance
				+ " a asthma:MildPersistentAsthma ; asthma:severityDiagnosisTime \""
				+ timestamp + "\"^^xsd:dateTime . } " + "WHERE { "
				+ "?p a :Patient . " + "{ " + "SELECT ?p "
				+ "WHERE { ?p asthma:hasSleepDisorder ?s . } "
				+ "GROUP BY (?p) " + "HAVING (count(distinct ?s) <= 2) } "
				+ "{ " + "SELECT ?p " + " WHERE { ?p asthma:hasSymptom ?s . } "
				+ "GROUP BY (?p) " + " HAVING (count(distinct ?s) > 2)}}";

		Model symptomModel = Symptoms.query(
				DateTime.parse("2015-06-21T00:00:00"), 7);
		Model sleepModel = SleepDisorder.query(
				DateTime.parse("2015-06-21T00:00:00"), 1);

		Union u = new Union(sleepModel.getGraph(), symptomModel.getGraph());

		Model model = new ModelCom(u);

		System.out.println("-----Debug------");
		Query query = QueryFactory.create("SELECT ?s ?p ?o WHERE {?s ?p ?o}");
		ResultSet results = QueryExecutionFactory.create(query, model)
				.execSelect();
		ResultSetFormatter.out(System.out, results, query);

		System.out.println(intermittentAsthma);

		System.out.println("-----Debug End------");

		query = QueryFactory.create(intermittentAsthma);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		Iterator<Triple> execConstructTriples = qexec.execConstructTriples();
		while (execConstructTriples.hasNext()) {
			System.out.println(execConstructTriples.next());
		}
	}

	public static void severePersistentAsthmaCheck() {

		String timestamp = DateTime.now().toString(KHealthUtils.fmt);

		String levelInstance = "k:\\/severity\\/" + timestamp + "\\/3";

		String queryString = prefixes
				+ "CONSTRUCT { "
				+ "?p a :Patient; asthma:hasSeveryLevel "
				+ levelInstance
				+ " ."
				+ levelInstance
				+ " a asthma:SeverePersistentAsthma ; asthma:severityDiagnosisTime \""
				+ timestamp + "\"^^xsd:dateTime . } " + "WHERE { "
				+ "?p a :Patient . " + "{ " + "SELECT ?p "
				+ "WHERE { ?p asthma:hasSleepDisorder ?s . } "
				+ "GROUP BY (?p) " + "HAVING (count(distinct ?s) >= 4) } "
				+ "{ " + "SELECT ?p " + " WHERE { ?p asthma:hasSymptom ?s . } "
				+ "GROUP BY (?p) " + " HAVING (count(distinct ?s) >= 0)}}";

		Model symptomModel = Symptoms.query(
				DateTime.parse("2015-06-21T00:00:00"), 7);
		Model sleepModel = SleepDisorder.query(
				DateTime.parse("2015-06-21T00:00:00"), 30);

		Union u = new Union(sleepModel.getGraph(), symptomModel.getGraph());

		Model model = new ModelCom(u);

		System.out.println("-----Debug------");
		Query query = QueryFactory.create("SELECT ?s ?p ?o WHERE {?s ?p ?o}");
		ResultSet results = QueryExecutionFactory.create(query, model)
				.execSelect();
		ResultSetFormatter.out(System.out, results, query);

		System.out.println(queryString);

		System.out.println("-----Debug End------");

		query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		Iterator<Triple> execConstructTriples = qexec.execConstructTriples();
		while (execConstructTriples.hasNext()) {
			System.out.println(execConstructTriples.next());
		}
	}

}
