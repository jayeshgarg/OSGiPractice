package com.gargjayesh.practice.karaf.views.entities;

/**
 * Created by egarjay on 21-03-2017.
 */
public class Humidity
{
    private final double humidity;

    private final long timeStamp;

    private Humidity(HumidityBuilder builder)
    {
        this.humidity = builder.humidity;
        this.timeStamp = builder.timeStamp;
    }

    public static HumidityBuilder newBuilder()
    {
        return new HumidityBuilder();
    }

    @Override
    public String toString()
    {
        return "Humidity { timeStamp= " + timeStamp + ", humidity= " + humidity + " }";
    }

    public double getHumidity()
    {
        return humidity;
    }

    public long getTimeStamp()
    {
        return timeStamp;
    }

    public static class HumidityBuilder
    {
        private double humidity;

        private long timeStamp;

        public HumidityBuilder()
        {
        }

        public HumidityBuilder setHumidity(double humidity)
        {
            this.humidity = humidity;
            return this;
        }

        public HumidityBuilder setTimeStamp(long timeStamp)
        {
            this.timeStamp = timeStamp;
            return this;
        }

        public Humidity build()
        {
            return new Humidity(this);
        }
    }
}
