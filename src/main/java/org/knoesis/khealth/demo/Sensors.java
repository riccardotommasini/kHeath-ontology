package org.knoesis.khealth.demo;

import org.joda.time.DateTime;
import org.knoesis.khealth.api.sensors.PollenLevel;
import org.knoesis.khealth.api.sensors.weather.AirQualityIndex;
import org.knoesis.khealth.api.sensors.weather.Humidity;
import org.knoesis.khealth.api.sensors.weather.Temperature;
import org.knoesis.khealth.api.sensors.weather.WeatherEndpoint;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.rdf.model.impl.InfModelImpl;
import com.hp.hpl.jena.reasoner.InfGraph;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.util.FileManager;

public class Sensors {
	public static void main(String[] args) {
		WeatherEndpoint aqi = new AirQualityIndex();
		Humidity humidity = new Humidity();
		Temperature temp = new Temperature();
		PollenLevel p = new PollenLevel();

		FileManager.get().addLocatorClassLoader(Sensors.class.getClassLoader());
		Model ontology = FileManager.get().loadModel(
				"./src/main/resources/final.rdf", null, "RDF/XML");

		System.err.println("Model Loaded");
		Reasoner reasoner = ReasonerRegistry.getOWLReasoner();

		InfModel ontology_star = ModelFactory
				.createInfModel(reasoner, ontology);

		Reasoner binded = reasoner.bindSchema(ontology_star.getGraph());

		System.err.println("Reasoner Ready");
		// DateTime.parse("2014-08-22T00:00:00"),
		// DateTime.parse("2014-09-15T00:00:00")));
		// KHealthUtils.debug(temp.queryOutdoor(
		// DateTime.parse("2014-08-22T00:00:00"),
		// DateTime.parse("2014-09-15T00:00:00")));

		// KHealthUtils.debug(humidity.queryIndoor(
		// DateTime.parse("2014-08-22T00:00:00"),
		// DateTime.parse("2014-10-27T00:00:00")));
		// KHealthUtils.debug(humidity.queryOutdoor(
		// DateTime.parse("2014-09-22T00:00:00"),
		// DateTime.parse("2014-10-27T00:00:00")));

		// System.err.println("AIR POLLUTION");
		// KHealthUtils
		// .debug(aqi.query(DateTime.parse("2014-10-22T00:00:00"), 70));

		Model abox = p.query(DateTime.parse("2014-10-13T00:00:00"),
				DateTime.parse("2014-10-27T00:00:00"));
		System.err.println("Pollen data Collected");

		Graph abox_graph = abox.getGraph();

		InfGraph bind = binded.bind(abox_graph);
		InfModelImpl abox_star = new InfModelImpl(bind);

		System.err.println("Reasoning Computed");

		Model difference = abox_star.difference(ontology_star);

		System.err.println("Difference Start");

		StmtIterator iterator = difference.listStatements();

		while (iterator.hasNext()) {
			Triple asTriple = iterator.next().asTriple();
			System.out.println(asTriple);
		}

		System.err.println("Difference End");

		System.err.println("ontology_star Start");

		iterator = ontology_star.listStatements();

		while (iterator.hasNext()) {
			Triple asTriple = iterator.next().asTriple();
			System.out.println(asTriple);
		}
		System.err.println("Difference End");

		System.err.println("abox_star Start");

		iterator = abox_star.listStatements();

		while (iterator.hasNext()) {
			Triple asTriple = iterator.next().asTriple();
			System.out.println(asTriple);
		}
		System.err.println("Difference End");

	}
}
