package org.knoesis.khealth.api.questions.seq;

import org.joda.time.DateTime;
import org.knoesis.khealth.api.questions.causes.Drug;
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


	public static void main(String[] args) {
		insermittentAsthmaCheck();
		mildPersistentAsthmaCheck();
		severePersistentAsthmaCheck();
	}

	public static Model insermittentAsthmaCheck() {

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

		Model model = new Drug().query(DateTime.parse("2015-06-27T00:00:00"), 7);

		System.out.println("-----Debug------");

		System.out.println("symptomModel");

		Query query = QueryFactory.create(KHealthUtils.prefixes + " SELECT ?p "
				+ "WHERE { ?p asthma:hasAppliedMedication ?d . } "
				+ "GROUP BY (?p) " + "HAVING (count(distinct ?d) >= 0) ");
		ResultSet results = QueryExecutionFactory.create(query, model)
				.execSelect();
		ResultSetFormatter.out(System.out, results, query);

		System.out.println("-----Debug End------");

		return query(queryString, model);
	}

	public static Model mildPersistentAsthmaCheck() {

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

		Model model = new Drug().query(DateTime.parse("2015-06-27T00:00:00"), 7);


		System.out.println("-----Debug------");

		System.out.println("symptomModel");

		Query query = QueryFactory.create(KHealthUtils.prefixes + " SELECT ?p "
				+ "WHERE { ?p asthma:hasAppliedMedication ?d . } "
				+ "GROUP BY (?p) " + "HAVING (count(distinct ?d) >= 0) ");
		ResultSet results = QueryExecutionFactory.create(query, model)
				.execSelect();
		ResultSetFormatter.out(System.out, results, query);

		System.out.println("-----Debug End------");
		System.err.println(queryString);

		Model resultModel = query(queryString, model);

		return resultModel;
	}

	public static Model severePersistentAsthmaCheck() {

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

		Model model = new Drug().query(DateTime.parse("2015-06-27T00:00:00"), 7);

		System.out.println("-----Debug------");

		System.out.println("INNER ");

		Query query = QueryFactory.create(KHealthUtils.prefixes + inner);

		ResultSet results = QueryExecutionFactory.create(query, model)
				.execSelect();
		ResultSetFormatter.out(System.out, results, query);

		System.out.println("Outer ");

		query = QueryFactory.create(KHealthUtils.prefixes + outer);

		results = QueryExecutionFactory.create(query, model).execSelect();
		ResultSetFormatter.out(System.out, results, query);

		System.out.println("-----Debug End------");

		return query(queryString, model);

	}

	private static Model query(String queryString, Model model) {

		System.err.println(queryString);

		Query query;
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
