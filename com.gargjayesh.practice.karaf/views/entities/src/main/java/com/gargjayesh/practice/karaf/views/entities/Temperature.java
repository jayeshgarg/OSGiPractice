package com.gargjayesh.practice.karaf.views.entities;

/**
 * Created by egarjay on 21-03-2017.
 */
public class Temperature
{
    private final double temperature;

    private final long timeStamp;

    private Temperature(TemperatureBuilder builder)
    {
        this.temperature = builder.temperature;
        this.timeStamp = builder.timeStamp;
    }

    public static TemperatureBuilder newBuilder()
    {
        return new TemperatureBuilder();
    }

    @Override
    public String toString()
    {
        return "Temperature { timeStamp= " + timeStamp + ", temperature= " + temperature + " }";
    }

    public double getTemperature()
    {
        return temperature;
    }

    public long getTimeStamp()
    {
        return timeStamp;
    }

    public static class TemperatureBuilder
    {
        private double temperature;

        private long timeStamp;

        public TemperatureBuilder()
        {
        }

        public TemperatureBuilder setTemperature(double temperature)
        {
            this.temperature = temperature;
            return this;
        }

        public TemperatureBuilder setTimeStamp(long timeStamp)
        {
            this.timeStamp = timeStamp;
            return this;
        }

        public Temperature build()
        {
            return new Temperature(this);
        }
    }
}
