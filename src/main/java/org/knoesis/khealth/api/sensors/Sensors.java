package org.knoesis.khealth.api.sensors;

import org.joda.time.DateTime;
import org.knoesis.khealth.utils.KHealthUtils;

public class Sensors {
	public static void main(String[] args) {
//		KHealthUtils.debug(Temperature.query(
//				DateTime.parse("2014-10-22T00:00:00"), 5));
		KHealthUtils.debug(AirQualityIndex.query(
				DateTime.parse("2014-10-22T00:00:00"), 70));

	}
}
