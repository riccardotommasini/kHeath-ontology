package org.knoesis.khealth.api;

import org.joda.time.DateTime;
import org.knoesis.khealth.utils.KHealthUtils;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class TreatementLevel {

	private static final String prefixes = "PREFIX : <http://www.knoesis.org/khealth#> "
			+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
			+ "PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#> "
			+ "PREFIX asthma: <http://www.knoesis.org/khealth/asthma#> "
			+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
			+ "PREFIX k: <http://www.knoesis.org/khealth/>";

	public static void main(String[] args) {
		insermittentAsthmaCheck();
		mildPersistentAsthmaCheck();
		severePersistentAsthmaCheck();
	}

	@SuppressWarnings("unused")
	public static void demo() {

		String timestamp = DateTime.now().toString(KHealthUtils.fmt);

		Model model = Drug.query();

		System.out.println("-----Debug------");

		System.out.println("model");

		Query query = QueryFactory.create("SELECT ?s ?p ?o WHERE {?s ?p ?o}");
		ResultSet results = QueryExecutionFactory.create(query, model)
				.execSelect();
		ResultSetFormatter.out(System.out, results, query);

		System.out.println("-----Debug End------");

	}

	public static Model insermittentAsthmaCheck() {

		String timestamp = DateTime.now().toString(KHealthUtils.fmt);
		String inner = "SELECT ?p "
				+ "WHERE { ?p asthma:hasAppliedMedication ?d . } "
				+ "GROUP BY (?p) " + "HAVING (count(distinct ?d) < 2) }";

		String queryString = prefixes
				+ "CONSTRUCT { "
				+ "?p asthma:hasAppliedTreatment ?g . "
				+ "?g a asthma:IntermittentAsthmaTreatement ; asthma:treatmentApplicationTime \""
				+ timestamp + "\"^^xsd:dateTime . " + "} " + "WHERE { "
				+ "?p a :Patient "
				+ "BIND (URI(REPLACE(STR(?p), \"patient\", \"treatment"
				+ timestamp + "\")) as ?g) ." + "{ " + inner + " }";

		Model model = Drug.query(DateTime.parse("2015-06-27T00:00:00"), 7);

		System.out.println("-----Debug------");

		System.out.println("symptomModel");

		Query query = QueryFactory.create(prefixes + " SELECT ?p "
				+ "WHERE { ?p asthma:hasAppliedMedication ?d . } "
				+ "GROUP BY (?p) " + "HAVING (count(distinct ?d) >= 0) ");
		ResultSet results = QueryExecutionFactory.create(query, model)
				.execSelect();
		ResultSetFormatter.out(System.out, results, query);

		System.out.println("-----Debug End------");
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

	public static Model mildPersistentAsthmaCheck() {

		String timestamp = DateTime.now().toString(KHealthUtils.fmt);
		String inner = "SELECT ?p "
				+ "WHERE { ?p asthma:hasAppliedMedication ?d . } "
				+ "GROUP BY (?p) " + "HAVING (count(distinct ?d) >= 2) }";

		String queryString = prefixes
				+ "CONSTRUCT { "
				+ "?p asthma:hasAppliedTreatment ?g . "
				+ "?g a asthma:MildPersistentAsthmaTreatement ; asthma:treatmentApplicationTime \""
				+ timestamp + "\"^^xsd:dateTime . " + "} " + "WHERE { "
				+ "?p a :Patient "
				+ "BIND (URI(REPLACE(STR(?p), \"patient\", \"treatment"
				+ timestamp + "\")) as ?g) ." + "{ " + inner + " }";

		Model model = Drug.query(DateTime.parse("2015-06-27T00:00:00"), 7);

		System.out.println("-----Debug------");

		System.out.println("symptomModel");

		Query query = QueryFactory.create(prefixes + " SELECT ?p "
				+ "WHERE { ?p asthma:hasAppliedMedication ?d . } "
				+ "GROUP BY (?p) " + "HAVING (count(distinct ?d) >= 0) ");
		ResultSet results = QueryExecutionFactory.create(query, model)
				.execSelect();
		ResultSetFormatter.out(System.out, results, query);

		System.out.println("-----Debug End------");
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

	public static Model severePersistentAsthmaCheck() {

		String timestamp = DateTime.now().toString(KHealthUtils.fmt);
		String inner = " SELECT ?p (count(?day) as ?days) " + "WHERE { "
				+ "?p asthma:hasAppliedMedication ?d . "
				+ "?d :hasAssociatedTime ?time "
				+ ". "
				+ "bind( day(xsd:dateTime(?time)) as ?day ) . } "
				+ "GROUP BY ?p ?day HAVING(count(distinct ?d) > 0) ";

		String outer = " SELECT ?p " + "WHERE {" + inner + "}"
				+ " GROUP BY ?p " + "HAVING (count(?days) > 0)";

		String queryString = prefixes
				+ " CONSTRUCT { "
				+ "?p asthma:hasAppliedTreatment ?g ."
				+ " ?g a asthma:SeverePersistentAsthmaTreatement ; asthma:treatmentApplicationTime \""
				+ timestamp + "\"^^xsd:dateTime . " + "} " + "WHERE { "
				+ "?p a :Patient . "
				+ "BIND (URI(REPLACE(STR(?p), \"patient\", \"treatment"
				+ timestamp + "\")) as ?g) . " + "{" + outer + " }}";

		Model model = Drug.query(DateTime.parse("2015-06-21T00:00:00"), 7);

		System.out.println("-----Debug------");

		System.out.println("INNER ");

		Query query = QueryFactory.create(prefixes + inner);

		ResultSet results = QueryExecutionFactory.create(query, model)
				.execSelect();
		ResultSetFormatter.out(System.out, results, query);

		System.out.println("Outer ");

		query = QueryFactory.create(prefixes + outer);

		results = QueryExecutionFactory.create(query, model).execSelect();
		ResultSetFormatter.out(System.out, results, query);

		System.out.println("-----Debug End------");
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
}
