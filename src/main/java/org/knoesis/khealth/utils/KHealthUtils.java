package org.knoesis.khealth.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;

public class KHealthUtils {
	public static final String ontop_endpoint = "http://localhost:8080/openrdf-sesame/repositories/khealth-24h";
	public static final DateTimeFormatter fmt = DateTimeFormat
			.forPattern("YYYY-MM-dd'T'HH:mm:ss");

	public static final String prefixes = "PREFIX : <http://www.knoesis.org/khealth#> "
			+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
			+ "PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#> "
			+ "PREFIX r: <http://knoesis.org/ric#> "
			+ "PREFIX asthma: <http://www.knoesis.org/khealth/asthma#> "
			+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
			+ "PREFIX k: <http://www.knoesis.org/khealth/>"
			+ "PREFIX time: <http://www.w3.org/2006/time#>";
	
	public static void debug(Model m){
		Query sel = QueryFactory.create("SELECT * WHERE {?s ?p ?o}");
		ResultSet res = QueryExecutionFactory.create(sel, m)
				.execSelect();
		ResultSetFormatter.out(System.out, res, sel);
	}
	
	public static String now(){
		return DateTime.now().toString(KHealthUtils.fmt);
	}
}
