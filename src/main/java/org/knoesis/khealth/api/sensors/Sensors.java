package org.knoesis.khealth.api.sensors;

import org.joda.time.DateTime;
import org.knoesis.khealth.utils.KHealthUtils;

public class Sensors {
	public static void main(String[] args) {
		SensorEndpoint aqi = new AirQualityIndex();
		Humidity humidity = new Humidity();
		Temperature temp = new Temperature();
		
		System.err.println("TEMPERATURE");


//		KHealthUtils.debug(temp.queryIndoor(
//				DateTime.parse("2014-08-22T00:00:00"),
//				DateTime.parse("2014-09-15T00:00:00")));
//		KHealthUtils.debug(temp.queryOutdoor(
//				DateTime.parse("2014-08-22T00:00:00"),
//				DateTime.parse("2014-09-15T00:00:00")));

		
//		KHealthUtils.debug(humidity.queryIndoor(
//				DateTime.parse("2014-08-22T00:00:00"),
//				DateTime.parse("2014-10-27T00:00:00")));
		KHealthUtils.debug(humidity.queryOutdoor(
				DateTime.parse("2014-09-22T00:00:00"),
				DateTime.parse("2014-10-27T00:00:00")));


		// System.err.println("AIR POLLUTION");
		// KHealthUtils
		// .debug(aqi.query(DateTime.parse("2014-10-22T00:00:00"), 70));

	}
}
