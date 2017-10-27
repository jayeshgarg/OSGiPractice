package com.gargjayesh.practice.karaf.displayunit.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by egarjay on 22-03-2017.
 */

@Path("/")
public interface RestWeatherDisplay
{
    @Path("/temperature")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Response getTemperatureDetails(@QueryParam("clearOld") boolean clearOld);

    @Path("/humidity")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Response getHumidityDetails(@QueryParam("clearOld") boolean clearOld);

    @POST
    @Path("/tempPost")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response postTemperature(String json);
}
