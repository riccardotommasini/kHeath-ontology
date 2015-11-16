package org.knoesis.khealth.utils;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

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
			+ "PREFIX k: <http://www.knoesis.org/khealth/>";
}
