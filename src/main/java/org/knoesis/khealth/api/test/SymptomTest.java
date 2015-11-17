package org.knoesis.khealth.api.test;

import org.knoesis.khealth.api.questions.Symptoms;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;

public class SymptomTest {

	public static void main(String[] args) {

		String prefixes = "PREFIX : <http://www.knoesis.org/kheath#> "
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
				+ "PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#> "
				+ "PREFIX r: <http://knoesis.org/ric#> "
				+ "PREFIX asthma: <http://www.knoesis.org/khealth/asthma#> "
				+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
				+ "PREFIX time: <http://www.w3.org/2006/time#> ";

		Model model = Symptoms.query();

		System.out.println("-----Debug------");
		Query query = QueryFactory.create(prefixes
				+ "SELECT ?p (count(distinct ?s) as ?count)"
				+ " WHERE { ?p asthma:hasSymptom ?s . } " + "GROUP BY (?p) ");
		ResultSet results = QueryExecutionFactory.create(query, model)
				.execSelect();
		ResultSetFormatter.out(System.out, results, query);

		System.out.println("-----Debug End------");

	}

}
