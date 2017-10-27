// **********************************************************************
// Copyright (c) 2015 Telefonaktiebolaget LM gargjayesh, Sweden.
// All rights reserved.
// The Copyright to the computer program(s) herein is the property of
// Telefonaktiebolaget LM gargjayesh, Sweden.
// The program(s) may be used and/or copied with the written permission
// from Telefonaktiebolaget LM gargjayesh or in accordance with the terms
// and conditions stipulated in the agreement/contract under which the
// program(s) have been supplied.
// **********************************************************************
package com.gargjayesh.practice.karaf.displayunit.impl.internal;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.ReaderInterceptor;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ConfigurationAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This OSGI Component starts up and registers the invoicing web services.
 */
@Component(name = "ConfigWebComp", ds = true, immediate = true, metatype = false)
public class ConfigWebComponent
{
    private static final Logger LOG = LoggerFactory.getLogger(ConfigWebComponent.class);

    private static final String CONTEXT_PATH = "/weather";

    private static final String JERSEY_SERVER_PID = "org.glassfish.jersey.core.jersey-server";

    private static final String JAXRS_CONNECTOR_PID = "com.eclipsesource.jaxrs.connector";

    @Activate
    public void start(BundleContext bundleContext) throws IOException
    {
        try
        {
            LOG.info("Activating the ConfigWebComp.");
            updateConfiguration(bundleContext);
            registerInterceptorsAndFilters(bundleContext);
        }
        catch (RuntimeException | IOException e)
        {
            LOG.error("Failed to activate ConfigWebComponent", e);
            throw e;
        }
    }

    private void registerInterceptorsAndFilters(BundleContext bundleContext)
    {
        bundleContext.registerService(ReaderInterceptor.class.getName(), new SimpleInterceptor(), null);
        LOG.error("Interceptor registered");
        bundleContext.registerService(ContainerRequestFilter.class.getName(), new SimpleFilter(), null);
        LOG.error("Filter registered");
    }

    @Deactivate
    public void stop(BundleContext context)
    {
        LOG.info("Service ConfigWebComp stopped.");
    }

    /**
     * Setups up the configuration for the rest server.
     *
     * @param bundleContext the context
     * @throws IOException          if access to persistent storage fails
     * @throws NullPointerException in case the {@link ConfigurationAdmin} is not available
     */
    private void updateConfiguration(BundleContext bundleContext) throws IOException
    {
        ServiceReference<ConfigurationAdmin> serviceReference = bundleContext.getServiceReference(ConfigurationAdmin.class);
        ConfigurationAdmin configurationAdmin = bundleContext.getService(serviceReference);
        updateConfiguration("root", CONTEXT_PATH, JAXRS_CONNECTOR_PID, configurationAdmin);
        LOG.info("Configuration web services will be published on context path: {}", CONTEXT_PATH);
        updateConfiguration("jersey.config.server.wadl.disableWadl", "true", JERSEY_SERVER_PID, configurationAdmin);
    }

    private void updateConfiguration(String key, String value, String pid, ConfigurationAdmin configurationAdmin) throws IOException
    {
        Dictionary<String, Object> properties = new Hashtable<>();
        properties.put(key, value);
        configurationAdmin.getConfiguration(pid, null).update(properties);
    }
}
