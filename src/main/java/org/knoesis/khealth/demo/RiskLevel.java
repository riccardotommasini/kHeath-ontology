package org.knoesis.khealth.demo;

import org.joda.time.DateTime;
import org.knoesis.khealth.api.sensors.weather.AirQualityIndex;
import org.knoesis.khealth.api.sensors.weather.Humidity;
import org.knoesis.khealth.api.sensors.weather.PollenLevel;
import org.knoesis.khealth.api.sensors.weather.Temperature;
import org.knoesis.khealth.utils.KHealthUtils;

import com.hp.hpl.jena.rdf.model.Model;

public class RiskLevel {

	static AirQualityIndex aqi;
	static Humidity humidity;
	static Temperature temp;
	static PollenLevel p;
	static boolean isPatientInDoor = false;

	public static void main(String[] args) {

		aqi = new AirQualityIndex();
		humidity = new Humidity();
		temp = new Temperature();
		p = new PollenLevel();
		DateTime from = DateTime.parse("2015-06-14T00:00:00");
		DateTime to = DateTime.parse("2015-06-27T00:00:00");

		Model indoorTemp = temp.queryIndoor(from, to);
		Model outdoorTemp = temp.queryOutdoor(from, to);
		Model airquality = aqi.query(from, to);
		Model pollen = p.query(from, to);

		KHealthUtils.debug(outdoorTemp);

		boolean pollen2 = pollen(pollen);
		boolean temperature = isPatientInDoor ? temperature(indoorTemp,
				"INDOOR TEMP") : temperature(outdoorTemp, "OUTDOOR TEMP");
		System.out
				.println("--------------------------------------------------------------------------------------------");
		boolean airquality2 = airquality(airquality);
		System.out
				.println("--------------------------------------------------------------------------------------------");

		if (pollen2 || temperature || airquality2)
			System.out.println("CONDITION ARE RIPE FOR AN ASTHMA ATTAK");
		else
			System.out.println("CONDITION NOT ARE RIPE FOR AN ASTHMA ATTAK");
	}

	private static boolean airquality(Model airquality) {
		System.out
				.println("------------------------------------AIR QUALITY---------------------------------------------");
		System.out
				.println("--------------------------------------------------------------------------------------------");
		boolean hazardous = aqi.isHazardous(airquality);
		System.out.println("|Hazardous      |" + hazardous + "|");
		boolean veryUnhealthy = aqi.isVeryUnhealthy(airquality);
		System.out.println("|Very Unhealthy |" + veryUnhealthy + "|");
		boolean unhealthy = aqi.isUnhealthy(airquality);
		System.out.println("|Unhealthy      |" + unhealthy + "|");
		boolean usg = aqi.isUSG(airquality);
		System.out.println("|USG            |" + usg + "|");
		boolean moderate = aqi.isModerate(airquality);
		System.out.println("|Moderate       |" + moderate + "|");
		boolean good = aqi.isGood(airquality);
		System.out.println("|Good           |" + good + "|");
		System.out
				.println("--------------------------------------------------------------------------------------------");

		return (hazardous || veryUnhealthy || usg || unhealthy);
	}

	private static boolean temperature(Model m, String title) {
		System.out
				.println("--------------------------------------------------------------------------------------------");
		System.out.println("-----------------------------------" + title
				+ "---------------------------------------------");
		System.out
				.println("--------------------------------------------------------------------------------------------");
		boolean high = temp.isHigh(m);
		System.out.println("|HIGH           |" + high + "|");
		boolean medium = temp.isMedium(m);
		System.out.println("|MEDIUM         |" + medium + "|");
		boolean low = temp.isLow(m);
		System.out.println("|LOW            |" + low + "|");
		System.out
				.println("--------------------------------------------------------------------------------------------");

		return low;
	}

	private static boolean pollen(Model m) {
		System.out
				.println("--------------------------------------------------------------------------------------------");
		System.out
				.println("-----------------------------------Pollen Level---------------------------------------------");
		System.out
				.println("--------------------------------------------------------------------------------------------");
		boolean high = p.isHigh(m);
		System.out.println("|HIGH           |" + high + "|");
		boolean medium = p.isMedium(m);
		System.out.println("|MEDIUM         |" + medium + "|");
		boolean low = p.isLow(m);
		System.out.println("|LOW            |" + low + "|");
		System.out
				.println("--------------------------------------------------------------------------------------------");
		return high;
	}

}
