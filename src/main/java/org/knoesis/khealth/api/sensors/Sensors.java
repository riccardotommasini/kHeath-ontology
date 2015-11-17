package org.knoesis.khealth.api.sensors;

import org.joda.time.DateTime;
import org.knoesis.khealth.utils.KHealthUtils;

public class Sensors {
	public static void main(String[] args) {
		SensorEndpoint aqi = new AirQualityIndex();
		 SensorEndpoint temp = new Temperature();
		 KHealthUtils
		 .debug(temp.query(DateTime.parse("2014-08-27T00:00:00"), 5));

		System.err.println("AIR POLLUTION");
		KHealthUtils
				.debug(aqi.query(DateTime.parse("2014-10-22T00:00:00"), 70));

	}
}
