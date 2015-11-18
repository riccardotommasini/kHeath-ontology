package org.knoesis.khealth.demo;

import org.knoesis.khealth.api.sensors.weather.AirQualityIndex;
import org.knoesis.khealth.api.sensors.weather.Humidity;
import org.knoesis.khealth.api.sensors.weather.PollenLevel;
import org.knoesis.khealth.api.sensors.weather.Temperature;
import org.knoesis.khealth.api.sensors.weather.WeatherEndpoint;

public class Sensors {
	public static void main(String[] args) {
		
		
		WeatherEndpoint aqi = new AirQualityIndex();
		Humidity humidity = new Humidity();
		Temperature temp = new Temperature();
		PollenLevel p = new PollenLevel();


	}
}
