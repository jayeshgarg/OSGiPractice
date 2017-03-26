package com.gargjayesh.practice.karaf.weatherstation.impl.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.gargjayesh.practice.karaf.views.entities.Humidity;
import com.gargjayesh.practice.karaf.views.entities.Temperature;

/**
 * Created by egarjay on 22-03-2017.
 */
public final class DummyDataStorage
{
    private static List<Humidity> humidityData = new ArrayList<>();

    private static List<Temperature> temperatureData = new ArrayList<>();

    public static void addHumidityDataList(List<Humidity> humidityDataFromSensorList)
    {
        humidityData.addAll(humidityDataFromSensorList);
    }

    public static void addTemperatureDataList(List<Temperature> temperatureDataFromSensorList)
    {
        temperatureData.addAll(temperatureDataFromSensorList);
    }

    public static void addHumidityData(Humidity humidityDataFromSensor)
    {
        humidityData.add(humidityDataFromSensor);
    }

    public static void addTemperatureData(Temperature temperatureDataFromSensor)
    {
        temperatureData.add(temperatureDataFromSensor);
    }

    public static List<Humidity> getHumidityDataForDisplay()
    {
        List<Humidity> result = new ArrayList<>(Collections.unmodifiableList(humidityData));
        humidityData.clear();
        return result;
    }

    public static List<Humidity> getHumidityDataForDisplayWithoutClear()
    {
        List<Humidity> result = new ArrayList<>(Collections.unmodifiableList(humidityData));
        return result;
    }

    public static List<Temperature> getTemperatureDataForDisplay()
    {
        List<Temperature> result = new ArrayList<>(Collections.unmodifiableList(temperatureData));
        temperatureData.clear();
        return result;
    }

    public static List<Temperature> getTemperatureDataForDisplayWithoutClear()
    {
        List<Temperature> result = new ArrayList<>(Collections.unmodifiableList(temperatureData));
        return result;
    }
}
