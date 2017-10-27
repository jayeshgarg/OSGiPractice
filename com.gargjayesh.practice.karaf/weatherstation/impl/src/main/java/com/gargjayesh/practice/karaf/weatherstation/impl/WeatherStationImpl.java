package com.gargjayesh.practice.karaf.weatherstation.impl;

import java.util.List;
import java.util.Map;
import java.util.Timer;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargjayesh.practice.karaf.sensor.api.HumiditySensor;
import com.gargjayesh.practice.karaf.sensor.api.TemperatureSensor;
import com.gargjayesh.practice.karaf.views.entities.Humidity;
import com.gargjayesh.practice.karaf.views.entities.Temperature;
import com.gargjayesh.practice.karaf.weatherstation.api.WeatherStation;

/**
 * Created by egarjay on 21-03-2017.
 */
@Component(name = "WeatherStation", immediate = true, service = WeatherStation.class, property = {"readInterval=5"})
public class WeatherStationImpl implements WeatherStation
{
    public static final String READ_INTERVAL = "readInterval";

    private static final Logger LOG = LoggerFactory.getLogger(WeatherStationImpl.class);

    private static int readInterval;

    private Timer timerTemperature;

    private Timer timerHumidity;

    private HumiditySensor humiditySensor;

    private TemperatureSensor temperatureSensor;

    @Reference(unbind = "unbindHumiditySensor", cardinality = ReferenceCardinality.MANDATORY)
    protected void bindHumiditySensor(HumiditySensor humiditySensor)
    {
        LOG.debug("HumiditySensor : Binded");
        this.humiditySensor = humiditySensor;
    }

    protected void unbindHumiditySensor(HumiditySensor humiditySensor)
    {
        LOG.debug("HumiditySensor : Unbinded");
        this.humiditySensor = null;
    }

    @Reference(unbind = "unbindTemperatureSensor", cardinality = ReferenceCardinality.MANDATORY)
    protected void bindTemperatureSensor(TemperatureSensor temperatureSensor)
    {
        LOG.debug("TemperatureSensor : Binded");
        this.temperatureSensor = temperatureSensor;
    }

    protected void unbindTemperatureSensor(TemperatureSensor temperatureSensor)
    {
        LOG.debug("TemperatureSensor : Unbinded");
        this.temperatureSensor = null;
    }

    @Activate
    public void activate(Map<String, Object> properties)
    {
        LOG.debug("WeatherStation : Activated");
        updateProperties(properties);
    }

    @Modified
    public void modified(Map<String, Object> properties)
    {
        LOG.debug("WeatherStation : Modified");
        int oldValue = readInterval;
        timerTemperature.cancel();
        timerHumidity.cancel();
        updateProperties(properties);
        LOG.debug("Data read interval updated : Old = " + oldValue + ", New = " + readInterval);
    }

    private void updateProperties(Map<String, Object> properties)
    {
        readInterval = Integer.parseInt(String.valueOf(properties.get(READ_INTERVAL)));
        timerTemperature = new Timer();
        timerTemperature.schedule(new ReadSensor<>(temperatureSensor), 0, readInterval * 1000);
        timerHumidity = new Timer();
        timerHumidity.schedule(new ReadSensor<>(humiditySensor), 0, readInterval * 1000);
    }

    @Override
    public List<Temperature> getTemperatureForDisplay(boolean clearOld)
    {
        List<Temperature> data = clearOld ?
                DummyDataStorage.getTemperatureDataForDisplay() :
                DummyDataStorage.getTemperatureDataForDisplayWithoutClear();
        LOG.debug("Temperature details fetched are : " + data);
        return data;
    }

    @Override
    public List<Humidity> getHumidityForDisplay(boolean clearOld)
    {
        List<Humidity> data = clearOld ?
                DummyDataStorage.getHumidityDataForDisplay() :
                DummyDataStorage.getHumidityDataForDisplayWithoutClear();
        LOG.debug("Humidity details fetched are : " + data);
        return data;
    }
}
