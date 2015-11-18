package org.knoesis.khealth.api.sensors;

import org.joda.time.DateTime;

import com.hp.hpl.jena.rdf.model.Model;

public interface SensorEndpoint {

	public Model query(DateTime from, DateTime to);

	public Model daylyQuery(DateTime d);

	public Model query(DateTime to, int previous);
}
