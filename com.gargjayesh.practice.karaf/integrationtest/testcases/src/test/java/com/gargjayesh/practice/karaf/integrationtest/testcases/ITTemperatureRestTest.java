package com.gargjayesh.practice.karaf.integrationtest.testcases;

import static org.apache.karaf.tooling.exam.options.KarafDistributionOption.karafDistributionConfiguration;
import static org.junit.Assert.assertNotNull;
import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.workingDirectory;

import javax.inject.Inject;
import java.io.File;

import org.apache.karaf.tooling.exam.options.KarafDistributionConfigurationFilePutOption;
import org.apache.karaf.tooling.exam.options.KarafDistributionConfigurationFileReplacementOption;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.OptionUtils;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerMethod;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationAdmin;

import com.gargjayesh.practice.karaf.sensor.api.TemperatureSensor;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class) //Pax container OSGI RT starter configuration
public class ITTemperatureRestTest
{
    @Inject
    private BundleContext bundleContext;

    @Inject
    private ConfigurationAdmin configurationAdmin;

    @Inject
    private TemperatureSensor sensor;

    @Configuration
    public static Option[] configuration()
    {
        Option[] testBundles = options(
                mavenBundle().groupId("com.gargjayesh.practice.karaf.views").artifactId("entities").version("0.1-SNAPSHOT"),
                mavenBundle().groupId("com.gargjayesh.practice.karaf.sensor").artifactId("api").version("0.1-SNAPSHOT"),
                mavenBundle().groupId("com.gargjayesh.practice.karaf.sensor").artifactId("impl").version("0.1-SNAPSHOT"),
                mavenBundle().groupId("com.gargjayesh.practice.karaf.weatherdisplay").artifactId("api").version("0.1-SNAPSHOT"),
                mavenBundle().groupId("com.gargjayesh.practice.karaf.weatherdisplay").artifactId("impl").version("0.1-SNAPSHOT"),
                mavenBundle().groupId("com.gargjayesh.practice.karaf.displayunit").artifactId("api").version("0.1-SNAPSHOT"),
                mavenBundle().groupId("com.gargjayesh.practice.karaf.displayunit").artifactId("impl").version("0.1-SNAPSHOT"));
        Option[] abc = config();
        Option[] options = OptionUtils.combine(testBundles, abc);
        return options;
    }

    private static Option[] config()
    {
        return new Option[]{
                karafDistributionConfiguration().frameworkUrl(maven().groupId("org.apache.karaf").artifactId("apache-karaf").type("zip").versionAsInProject()).karafVersion("2.3.0").name("Apache Karaf"),
                new KarafDistributionConfigurationFileReplacementOption("etc/org.ops4j.pax.url.mvn.cfg", new File("src/test/resources/org.ops4j.pax.url.mvn.cfg")),
                new KarafDistributionConfigurationFilePutOption("etc/jmsconfig.cfg", "jmstransportconnector.uri", "tcp://0.0.0.0:61616"),
                new KarafDistributionConfigurationFilePutOption("etc/jmsconfig.cfg", "connectionfactory.uri", "vm://localhost"),
                workingDirectory("target/paxrunner/features/")};
    }

    @Test
    public void test() throws Exception
    {
        assertNotNull(sensor.getTemperature());
    }

}
