package com.gargjayesh.practice.karaf.integrationtest.testcases;

import static org.junit.Assert.assertNotNull;
import static org.ops4j.pax.exam.CoreOptions.bundle;
import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.features;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.karafDistributionConfiguration;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.keepRuntimeFolder;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.logLevel;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.karaf.options.LogLevelOption.LogLevel;
import org.ops4j.pax.exam.options.MavenArtifactUrlReference;
import org.ops4j.pax.exam.options.MavenUrlReference;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerMethod;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
public class ITTemperatureRestTest
{

    public static final long MAX_CONTAINER_STARTUP_TIME = 8 * 10 * 1000;

    private static final Logger LOG = LoggerFactory.getLogger(ITTemperatureRestTest.class);

    @Inject
    @org.ops4j.pax.exam.util.Filter(timeout = MAX_CONTAINER_STARTUP_TIME)
    protected BundleContext bundleContext;

    @Inject
    @org.ops4j.pax.exam.util.Filter(timeout = MAX_CONTAINER_STARTUP_TIME)
    protected ConfigurationAdmin configurationAdmin;

    @Configuration
    public Option[] config()
    {
        MavenArtifactUrlReference karafUrl = maven().groupId("org.apache.karaf").artifactId("apache-karaf").version("3.0.8").type("tar.gz");
        MavenUrlReference karafStandardRepo = maven().groupId("org.apache.karaf.features").artifactId("standard").classifier("features").version("3.0.8").type("xml");
        return new Option[]{
                //set this to true if we want to enable remote debugging in karaf
                //debugConfiguration("5005", false),

                //option to clean exam folder after TCs execution
                keepRuntimeFolder(),

                //set the default log level here (TRACE < DEBUG < INFO < WARN < ERROR)
                logLevel(LogLevel.DEBUG),

                //option to increase or decrease karaf service lookup timeout time, if yo have more bundles, increase this
                //new RBCLookupTimeoutOption(TimeUnit.MINUTES.toMillis(10)),

                //option to configure where the test karaf will get extracted
                karafDistributionConfiguration().frameworkUrl(karafUrl).unpackDirectory(new File("target/exam")).useDeployFolder(false),

                //list of features on which project depends
                features(karafStandardRepo, "scr", "webconsole", "war", "pax-http"),

                //configureConsole().ignoreLocalConsole(),

                //list of 3rd party project dependencies
                //(below method will load osgi bundle in karaf container)
                mavenBundle().groupId("com.eclipsesource.jaxrs").artifactId("jersey-all").version("2.22.2").start(),
                mavenBundle().groupId("com.eclipsesource.jaxrs").artifactId("publisher").version("5.0").start(),
                mavenBundle().groupId("com.google.code.gson").artifactId("gson").version("2.7"),
                //(below method will load non-osgi jar in karaf container)
                bundle("wrap:mvn:org.apache.httpcomponents/httpclient/4.5.3"),
                bundle("wrap:mvn:org.apache.httpcomponents/httpcore/4.4.6"),

                //list of project bundles under test
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
        LOG.error("Test case started");
        TimeUnit.SECONDS.sleep(10);

        String URL = "http://localhost:8181/weather/temperature";
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet getRequest = new HttpGet(URL);
        getRequest.setHeader("User-Agent", "MySuperUserAgent");
        HttpResponse response = client.execute(getRequest);
        String responseEntity = convertResponseEntityToString(response.getEntity());
        LOG.warn("responseEntity = {}", responseEntity);
        assertNotNull(responseEntity);
    }

    private String convertResponseEntityToString(HttpEntity entity) throws IOException
    {
        BufferedReader rd = new BufferedReader(new InputStreamReader(entity.getContent()));
        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null)
        {
            result.append(line);
        }
        return result.toString();
    }

}
