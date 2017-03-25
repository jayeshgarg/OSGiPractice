package com.gargjayesh.practice.karaf.sensor.api;

import com.gargjayesh.practice.karaf.views.entities.Humidity;

/**
 * Created by egarjay on 21-03-2017.
 */
public interface HumiditySensor
{
    Humidity getHumidity();
}
