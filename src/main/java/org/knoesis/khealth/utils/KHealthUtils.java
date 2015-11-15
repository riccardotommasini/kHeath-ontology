package org.knoesis.khealth.utils;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class KHealthUtils {
	public static final String ontop_endpoint = "http://localhost:8080/openrdf-sesame/repositories/khealth-24h";
	public static final DateTimeFormatter fmt = DateTimeFormat.forPattern("YYYY-MM-dd'T'HH:mm:ss");
}
