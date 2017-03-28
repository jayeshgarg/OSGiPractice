package com.gargjayesh.practice.karaf.sensor.impl;

import java.util.concurrent.ThreadLocalRandom;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargjayesh.practice.karaf.sensor.api.TemperatureSensor;
import com.gargjayesh.practice.karaf.views.entities.Temperature;

/**
 * Created by egarjay on 21-03-2017.
 */
@Component(name = "TemperatureSensor", immediate = true, service = TemperatureSensor.class)
public class TemperatureSensorImpl implements TemperatureSensor
{
    private static final Logger LOG = LoggerFactory.getLogger(TemperatureSensorImpl.class);

    private static double currentTemperature = 25.0;

    @Activate
    public void activate()
    {
        LOG.debug("HumiditySensorImpl : Activated");
    }

    @Override
    public Temperature getTemperature()
    {
        currentTemperature += ThreadLocalRandom.current().nextDouble(-5.0, 5.0);
        if (currentTemperature > 100)
        {
            currentTemperature = 25.0;
        }
        if (currentTemperature < 0)
        {
            currentTemperature = 25.0;
        }
        return Temperature.newBuilder()
                .setTimeStamp(System.currentTimeMillis())
                .setTemperature(currentTemperature)
                .build();
    }
}
