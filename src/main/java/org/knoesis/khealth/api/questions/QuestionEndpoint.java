package org.knoesis.khealth.api.questions;

import org.joda.time.DateTime;

import com.hp.hpl.jena.rdf.model.Model;

public interface QuestionEndpoint {
	public Model daylyQuery(DateTime d);

	public Model weeklyQuery(DateTime d);

	public Model query(DateTime d, int previous);

	public Model query(DateTime from, DateTime to);
}
