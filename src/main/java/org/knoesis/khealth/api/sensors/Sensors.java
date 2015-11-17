package org.knoesis.khealth.api.sensors;

import org.joda.time.DateTime;
import org.knoesis.khealth.utils.KHealthUtils;

public class Sensors {
	public static void main(String[] args) {
		SensorEndpoint aqi = new AirQualityIndex();

		System.err.println("TEMPERATURE");

		Temperature temp = new Temperature();
		KHealthUtils.debug(temp.queryIndoor(
				DateTime.parse("2014-08-22T00:00:00"),
				DateTime.parse("2014-09-15T00:00:00")));
		KHealthUtils.debug(temp.queryOutdoor(
				DateTime.parse("2014-08-22T00:00:00"),
				DateTime.parse("2014-09-15T00:00:00")));


		// System.err.println("AIR POLLUTION");
		// KHealthUtils
		// .debug(aqi.query(DateTime.parse("2014-10-22T00:00:00"), 70));

	}
}
