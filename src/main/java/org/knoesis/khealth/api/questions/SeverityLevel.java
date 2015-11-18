package org.knoesis.khealth.api.questions;

import org.joda.time.DateTime;
import org.knoesis.khealth.utils.KHealthUtils;

import com.hp.hpl.jena.graph.compose.Union;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.rdf.model.impl.ModelCom;

public class SeverityLevel {

	private Model query(String queryString, Model model) {
		Query query;
		System.err.println(queryString);

		query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		Model resultModel = qexec.execConstruct();
		StmtIterator listStatements = resultModel.listStatements();
		while (listStatements.hasNext()) {
			System.out.println(listStatements.next());
		}

		return resultModel;
	}

	public Model intermittentAsthmaCheck() {

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

		Model symptomModel = new Symptoms().query(
				DateTime.parse("2015-06-21T00:00:00"), 2);
		Model sleepModel = new SleepDisorder().query(
				DateTime.parse("2015-06-21T00:00:00"), 2);

		Union u = new Union(sleepModel.getGraph(), symptomModel.getGraph());

		Model model = new ModelCom(u);

		System.out.println("-----Debug------");

		System.out.println("symptomModel");

		Query query = QueryFactory.create("SELECT ?s ?p ?o WHERE {?s ?p ?o}");
		ResultSet results = QueryExecutionFactory.create(query, symptomModel)
				.execSelect();
		ResultSetFormatter.out(System.out, results, query);

		System.out.println("sleepModel");

		query = QueryFactory.create("SELECT ?s ?p ?o WHERE {?s ?p ?o}");
		results = QueryExecutionFactory.create(query, sleepModel).execSelect();
		ResultSetFormatter.out(System.out, results, query);

		System.out.println("Union");
		query = QueryFactory.create("SELECT ?s ?p ?o WHERE {?s ?p ?o}");
		results = QueryExecutionFactory.create(query, model).execSelect();
		ResultSetFormatter.out(System.out, results, query);
		System.out.println(queryString);

		System.out.println("innerer");
		query = QueryFactory.create(KHealthUtils.prefixes + inner2);
		results = QueryExecutionFactory.create(query, model).execSelect();
		ResultSetFormatter.out(System.out, results, query);
		System.out.println(queryString);

		System.out.println("inner");
		query = QueryFactory.create(KHealthUtils.prefixes + inner1);
		results = QueryExecutionFactory.create(query, model).execSelect();
		ResultSetFormatter.out(System.out, results, query);
		System.out.println(queryString);

		System.out.println("-----Debug End------");

		return query(queryString, model);
	}

	public Model mildPersistentAsthmaCheck() {

		String timestamp = DateTime.now().toString(KHealthUtils.fmt);

		String levelInstance = "k:severity\\/" + timestamp + "\\/1";

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

		Model symptomModel = new Symptoms().query(
				DateTime.parse("2015-06-21T00:00:00"), 2);
		Model sleepModel = new SleepDisorder().query(
				DateTime.parse("2015-06-21T00:00:00"), 2);

		Union u = new Union(sleepModel.getGraph(), symptomModel.getGraph());

		Model model = new ModelCom(u);

		System.out.println("-----Debug------");

		System.out.println("symptomModel");

		Query query = QueryFactory.create("SELECT ?s ?p ?o WHERE {?s ?p ?o}");
		ResultSet results = QueryExecutionFactory.create(query, symptomModel)
				.execSelect();
		ResultSetFormatter.out(System.out, results, query);

		System.out.println("sleepModel");

		query = QueryFactory.create("SELECT ?s ?p ?o WHERE {?s ?p ?o}");
		results = QueryExecutionFactory.create(query, sleepModel).execSelect();
		ResultSetFormatter.out(System.out, results, query);

		System.out.println("Union");
		query = QueryFactory.create("SELECT ?s ?p ?o WHERE {?s ?p ?o}");
		results = QueryExecutionFactory.create(query, model).execSelect();
		ResultSetFormatter.out(System.out, results, query);
		System.out.println(queryString);

		System.out.println("-----Debug End------");

		return query(queryString, model);
	}

	public Model severePersistentAsthmaCheck() {

		String timestamp = DateTime.now().toString(KHealthUtils.fmt);

		String levelInstance = "k:severity\\/" + timestamp + "\\/3";

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

		Model symptomModel = new Symptoms().query(
				DateTime.parse("2015-06-21T00:00:00"), 2);
		Model sleepModel = new SleepDisorder().query(
				DateTime.parse("2015-06-21T00:00:00"), 2);

		Union u = new Union(sleepModel.getGraph(), symptomModel.getGraph());

		Model model = new ModelCom(u);

		System.out.println("-----Debug------");
		Query query = QueryFactory.create("SELECT ?s ?p ?o WHERE {?s ?p ?o}");
		ResultSet results = QueryExecutionFactory.create(query, model)
				.execSelect();
		ResultSetFormatter.out(System.out, results, query);

		System.out.println(queryString);

		System.out.println("-----Debug End------");

		return query(queryString, model);
	}

}
