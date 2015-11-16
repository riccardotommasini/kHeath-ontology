package org.knoesis.khealth.api;

import org.knoesis.khealth.utils.KHealthUtils;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class ContorlLevel {

	public static void main(String[] args) {

		 Model intermittentSeverity = SeverityLevel.intermittentAsthmaCheck();
		 Model intermittentTreatement = TreatementLevel
		 .insermittentAsthmaCheck();

		 Model mildSeverity = SeverityLevel.mildPersistentAsthmaCheck();
		 Model mildTreatement = TreatementLevel.mildPersistentAsthmaCheck();

		Model severeSeverity = SeverityLevel.severePersistentAsthmaCheck();
		Model severeTreatement = TreatementLevel.severePersistentAsthmaCheck();

		System.err.println("severeSeverity");

		Query sel = QueryFactory.create("SELECT * WHERE {?s ?p ?o}");
		ResultSet res = QueryExecutionFactory.create(sel, severeSeverity)
				.execSelect();
		ResultSetFormatter.out(System.out, res, sel);

		System.err.println("severeTreatement");

		res = QueryExecutionFactory.create(sel, severeTreatement).execSelect();
		ResultSetFormatter.out(System.out, res, sel);

		System.err.println("UNION");

		System.out.println("Models complete");

		System.err.println("INTERMITTENT");

		query(ModelFactory.createDefaultModel().union(intermittentTreatement)
				.union(intermittentSeverity),
				"asthma:IntermittentAsthmaTreatement",
				"asthma:IntermittentAsthma");
		System.err.println("MILD");

		query(ModelFactory.createDefaultModel().union(mildTreatement)
				.union(mildSeverity),
				"asthma:MildPersistentAsthmaTreatement",
				"asthma:MildPersistentAsthma");
		System.err.println("SEVERE");

		query(ModelFactory.createDefaultModel().union(severeTreatement)
				.union(severeSeverity),
				"asthma:SeverePersistentAsthmaTreatement",
				"asthma:SeverePersistentAsthma");

	}

	public static void query(Model m, String treatement, String severity) {

		String queryString = " WHERE { "
				+ "?p a :Patient; asthma:hasSeveryLevel ?s ; asthma:hasAppliedTreatment ?t ."
				+ "?t a " + treatement + " . " + "?s a " + severity + " ."
				+ "}";

		String askQuery = KHealthUtils.prefixes + " ASK " + queryString;
		String selectQuery = KHealthUtils.prefixes + " SELECT * " + queryString;
		Query query;
		QueryExecution qexec;
		ResultSet results;

		query = QueryFactory.create(askQuery);
		qexec = QueryExecutionFactory.create(query, m);

		System.out.println("CONTROL LEVEL " + qexec.execAsk());

		query = QueryFactory.create(selectQuery);
		results = QueryExecutionFactory.create(query, m).execSelect();
		ResultSetFormatter.out(System.out, results, query);

	}
}
