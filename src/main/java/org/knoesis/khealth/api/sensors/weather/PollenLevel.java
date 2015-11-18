package org.knoesis.khealth.api.sensors.weather;

import org.joda.time.DateTime;
import org.knoesis.khealth.utils.KHealthUtils;

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
	
}
