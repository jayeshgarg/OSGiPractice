package com.gargjayesh.practice.karaf.weatherstation.impl.internal;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargjayesh.practice.karaf.displayunit.api.RestWeatherDisplay;
import com.gargjayesh.practice.karaf.views.entities.Humidity;
import com.gargjayesh.practice.karaf.views.entities.Temperature;
import com.gargjayesh.practice.karaf.weatherstation.api.WeatherStation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by egarjay on 22-03-2017.
 */
@Service(value = RestWeatherDisplay.class)
@Component(name = "RestWeatherDisplay", label = "RestWeatherDisplay", immediate = true)
public class RestWeatherDisplayImpl implements RestWeatherDisplay
{
    private static final Logger LOG = LoggerFactory.getLogger(RestWeatherDisplayImpl.class);

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Reference(bind = "bindWeatherStation", unbind = "unbindWeatherStation", cardinality = ReferenceCardinality.MANDATORY_UNARY, policy = ReferencePolicy.STATIC)
    private WeatherStation weatherStation;

    protected void bindWeatherStation(WeatherStation weatherStation)
    {
        LOG.debug("WeatherStation : Binded");
        this.weatherStation = weatherStation;
    }

    protected void unbindWeatherStation(WeatherStation weatherStation)
    {
        LOG.debug("WeatherStation : Unbinded");
        this.weatherStation = null;
    }

    @Activate
    public void activate()
    {
        LOG.debug("RestWeatherDisplay : Activated");
    }

    @Override
    public Response getTemperatureDetails(boolean clearOld)
    {
        LOG.debug("Request to fetch temperature details received");
        List<Temperature> temperatureList = weatherStation.getTemperatureForDisplay(clearOld);
        String json = GSON.toJson(temperatureList);
        return Response.ok(json, MediaType.APPLICATION_JSON).build();
    }

    @Override
    public Response getHumidityDetails(boolean clearOld)
    {
        LOG.debug("Request to fetch humidity details received");
        List<Humidity> humidityList = weatherStation.getHumidityForDisplay(clearOld);
        String json = GSON.toJson(humidityList);
        return Response.ok(json, MediaType.APPLICATION_JSON).build();
    }
}
