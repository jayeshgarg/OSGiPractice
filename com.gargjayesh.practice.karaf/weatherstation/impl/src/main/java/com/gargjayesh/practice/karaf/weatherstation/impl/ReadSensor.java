package com.gargjayesh.practice.karaf.weatherstation.impl;

import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargjayesh.practice.karaf.sensor.api.HumiditySensor;
import com.gargjayesh.practice.karaf.sensor.api.TemperatureSensor;
import com.gargjayesh.practice.karaf.views.entities.Humidity;
import com.gargjayesh.practice.karaf.views.entities.Temperature;

/**
 * Created by egarjay on 21-03-2017.
 */
public class ReadSensor<T> extends TimerTask
{
    private static final Logger LOG = LoggerFactory.getLogger(ReadSensor.class);

    private T sensorService;

    public ReadSensor(T sensorService)
    {
        if (sensorService instanceof HumiditySensor)
        {
            LOG.debug("Sensor is of HumiditySensor type");
        } else if (sensorService instanceof TemperatureSensor)
        {
            LOG.debug("Sensor is of TemperatureSensor type");
        } else
        {
            LOG.debug("Sensor is of invalid type");
            throw new IllegalArgumentException("Invalid sensor service. Must be either of HumiditySensor or TemperatureSensor");
        }
        this.sensorService = sensorService;
    }

    @Override
    public void run()
    {
        if (sensorService instanceof HumiditySensor)
        {
            Humidity humidity = ((HumiditySensor) sensorService).getHumidity();
            LOG.debug("Adding humidity data : " + humidity);
            DummyDataStorage.addHumidityData(humidity);
        } else if (sensorService instanceof TemperatureSensor)
        {
            Temperature temperature = ((TemperatureSensor) sensorService).getTemperature();
            LOG.debug("Adding temperature data : " + temperature);
            DummyDataStorage.addTemperatureData(temperature);
        }
    }
}
