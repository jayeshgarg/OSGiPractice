package com.gargjayesh.practice.karaf.sensor.impl;

import java.util.concurrent.ThreadLocalRandom;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargjayesh.practice.karaf.sensor.api.HumiditySensor;
import com.gargjayesh.practice.karaf.views.entities.Humidity;

/**
 * Created by egarjay on 21-03-2017.
 */
@Component(name = "HumiditySensor", immediate = true, service = HumiditySensor.class)
public class HumiditySensorImpl implements HumiditySensor
{
    private static final Logger LOG = LoggerFactory.getLogger(HumiditySensorImpl.class);

    private static double currentHumidity = 60.0;

    @Activate
    public void activate()
    {
        LOG.debug("HumiditySensorImpl : Activated");
    }

    @Override
    public Humidity getHumidity()
    {
        currentHumidity += ThreadLocalRandom.current().nextDouble(-5.0, 5.0);
        if (currentHumidity > 100)
        {
            currentHumidity = 60.0;
        }
        if (currentHumidity < 0)
        {
            currentHumidity = 60.0;
        }
        return Humidity.newBuilder()
                .setTimeStamp(System.currentTimeMillis())
                .setHumidity(currentHumidity)
                .build();
    }
}
