package org.knoesis.khealth.demo;

import org.joda.time.DateTime;
import org.knoesis.khealth.api.sensors.weather.AirQualityIndex;
import org.knoesis.khealth.api.sensors.weather.Humidity;
import org.knoesis.khealth.api.sensors.weather.PollenLevel;
import org.knoesis.khealth.api.sensors.weather.Temperature;

import com.hp.hpl.jena.rdf.model.Model;

public class Sensors {

	static AirQualityIndex aqi;
	static Humidity humidity;
	static Temperature temp;
	static PollenLevel p;

	public static void main(String[] args) {

		aqi = new AirQualityIndex();
		humidity = new Humidity();
		temp = new Temperature();
		p = new PollenLevel();

		Model indoorTemp = temp.queryIndoor(
				DateTime.parse("2014-08-22T00:00:00"),
				DateTime.parse("2014-09-15T00:00:00"));
		Model outdoorTemp = temp.queryOutdoor(
				DateTime.parse("2014-08-22T00:00:00"),
				DateTime.parse("2014-09-15T00:00:00"));
		Model airquality = aqi.query(DateTime.parse("2014-10-11T00:00:00"),
				DateTime.parse("2014-10-12T00:00:00"));
		Model pollen = p.query(DateTime.parse("2014-10-13T00:00:00"),
				DateTime.parse("2014-10-14T00:00:00"));

		pollen(pollen);
		temperature(outdoorTemp, "OUTDOOR TEMP");
		temperature(indoorTemp, "INDOOR TEMP");
		System.out
				.println("--------------------------------------------------------------------------------------------");
		airquality(airquality);
		System.out
				.println("--------------------------------------------------------------------------------------------");

	}

	private static void airquality(Model airquality) {
		System.out
				.println("------------------------------------AIR QUALITY---------------------------------------------");
		System.out
				.println("--------------------------------------------------------------------------------------------");
		System.out.println("|Hazardous      |" + aqi.isHazardous(airquality)
				+ "|");
		System.out.println("|Very Unhealthy |"
				+ aqi.isVeryUnhealthy(airquality) + "|");
		System.out.println("|Unhealthy      |" + aqi.isUnhealthy(airquality)
				+ "|");
		System.out.println("|USG            |" + aqi.isUSG(airquality) + "|");
		System.out.println("|Moderate       |" + aqi.isModerate(airquality)
				+ "|");
		System.out.println("|Good           |" + aqi.isGood(airquality) + "|");
		System.out
				.println("--------------------------------------------------------------------------------------------");
	}

	private static void temperature(Model m, String title) {
		System.out
				.println("--------------------------------------------------------------------------------------------");
		System.out.println("-----------------------------------" + title
				+ "---------------------------------------------");
		System.out
				.println("--------------------------------------------------------------------------------------------");
		System.out.println("|HIGH           |" + temp.isHigh(m) + "|");
		System.out.println("|MEDIUM         |" + temp.isMedium(m) + "|");
		System.out.println("|LOW            |" + temp.isLow(m) + "|");
		System.out
				.println("--------------------------------------------------------------------------------------------");
	}

	private static void pollen(Model m) {
		System.out
				.println("--------------------------------------------------------------------------------------------");
		System.out
				.println("-----------------------------------Pollen Level---------------------------------------------");
		System.out
				.println("--------------------------------------------------------------------------------------------");
		System.out.println("|HIGH           |" + p.isHigh(m) + "|");
		System.out.println("|MEDIUM         |" + p.isMedium(m) + "|");
		System.out.println("|LOW            |" + p.isLow(m) + "|");
		System.out
				.println("--------------------------------------------------------------------------------------------");

	}

}
