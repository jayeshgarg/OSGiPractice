package com.gargjayesh.practice.karaf.integrationtest.testcases;

import static org.junit.Assert.assertNotNull;
import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.features;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.karafDistributionConfiguration;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.keepRuntimeFolder;

import javax.inject.Inject;
import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.options.MavenArtifactUrlReference;
import org.ops4j.pax.exam.options.MavenUrlReference;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerMethod;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargjayesh.practice.karaf.sensor.api.TemperatureSensor;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class) //Pax container OSGI RT starter configuration
public class ITTemperatureRestTest
{

    private static final Logger LOG = LoggerFactory.getLogger(ITTemperatureRestTest.class);

    @Inject
    private BundleContext bundleContext;

    @Inject
    private ConfigurationAdmin configurationAdmin;

    @Inject
    private TemperatureSensor sensor;

    @Configuration
    public Option[] config()
    {
        MavenArtifactUrlReference karafUrl = maven().groupId("org.apache.karaf").artifactId("apache-karaf").version("3.0.0").type("tar.gz");
        MavenUrlReference karafStandardRepo = maven().groupId("org.apache.karaf.features").artifactId("standard").version("4.1.0").type("xml").classifier("features");
        return new Option[]{
                // KarafDistributionOption.debugConfiguration("5005", true),
                karafDistributionConfiguration().frameworkUrl(karafUrl).unpackDirectory(new File("target/exam")).useDeployFolder(false), keepRuntimeFolder(),
                features(karafStandardRepo, "scr"),
                mavenBundle().groupId("com.eclipsesource.jaxrs").artifactId("jersey-all").version("2.22.2").start(),
                mavenBundle().groupId("com.google.code.gson").artifactId("gson").version("2.8.0").start(),
                mavenBundle().groupId("com.eclipsesource.jaxrs").artifactId("publisher").version("5.0").start(),
                mavenBundle().groupId("com.gargjayesh.practice.karaf.views").artifactId("entities").version("0.1-SNAPSHOT").start(),
                mavenBundle().groupId("com.gargjayesh.practice.karaf.sensor").artifactId("api").version("0.1-SNAPSHOT").start(),
                mavenBundle().groupId("com.gargjayesh.practice.karaf.sensor").artifactId("impl").version("0.1-SNAPSHOT").start(),
                mavenBundle().groupId("com.gargjayesh.practice.karaf.weatherstation").artifactId("api").version("0.1-SNAPSHOT").start(),
                mavenBundle().groupId("com.gargjayesh.practice.karaf.weatherstation").artifactId("impl").version("0.1-SNAPSHOT").start(),
                mavenBundle().groupId("com.gargjayesh.practice.karaf.displayunit").artifactId("api").version("0.1-SNAPSHOT").start(),
                mavenBundle().groupId("com.gargjayesh.practice.karaf.displayunit").artifactId("impl").version("0.1-SNAPSHOT").start()
        };
    }

    @Test
    public void testRestCode() throws Exception
    {
        LOG.debug("Test case started");
        assertNotNull(sensor.getTemperature());
    }

}
