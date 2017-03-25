package com.gargjayesh.practice.karaf.weatherstation.api;

import java.util.List;

import com.gargjayesh.practice.karaf.views.entities.Humidity;
import com.gargjayesh.practice.karaf.views.entities.Temperature;

/**
 * Created by egarjay on 21-03-2017.
 */
public interface WeatherStation
{
    List<Temperature> getTemperatureForDisplay(boolean clearOld);

    List<Humidity> getHumidityForDisplay(boolean clearOld);
}
