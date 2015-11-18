package org.knoesis.khealth.api.sensors.weather;

import org.joda.time.DateTime;
import org.knoesis.khealth.utils.KHealthUtils;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.Model;

public class PollenLevel extends WeatherEndpoint {

	public Model query(DateTime from, DateTime to) {

		String fromString = from.toString(KHealthUtils.fmt);
		String toString = to.toString(KHealthUtils.fmt);
		System.out.println("From " + fromString);
		System.out.println("To " + toString);

		String observationType = ":PollenLevelObservation";

		return retrieveModel(fromString, toString, observationType);

	}

	public boolean isLow(Model m) {
		Query sel = QueryFactory.create(KHealthUtils.prefixes
				+ " ASK WHERE {?s :hasObservationValue \"0\"^^xsd:integer .}");
		return QueryExecutionFactory.create(sel, m).execAsk();
	}

	public boolean isMedium(Model m) {
		Query sel = QueryFactory.create(KHealthUtils.prefixes
				+ "ASK WHERE {?s :hasObservationValue \"1\"^^xsd:integer .}");
		return QueryExecutionFactory.create(sel, m).execAsk();
	}

	public boolean isHigh(Model m) {
		Query sel = QueryFactory.create(KHealthUtils.prefixes
				+ "ASK WHERE {?s :hasObservationValue \"2\"^^xsd:integer .}");
		return QueryExecutionFactory.create(sel, m).execAsk();
	}

}
