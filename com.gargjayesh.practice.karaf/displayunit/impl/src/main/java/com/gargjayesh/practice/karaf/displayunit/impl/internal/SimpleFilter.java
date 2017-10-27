// **********************************************************************
// Copyright (c) 2017 Telefonaktiebolaget LM Ericsson, Sweden.
// All rights reserved.
// The Copyright to the computer program(s) herein is the property of
// Telefonaktiebolaget LM Ericsson, Sweden.
// The program(s) may be used and/or copied with the written permission
// from Telefonaktiebolaget LM Ericsson or in accordance with the terms
// and conditions stipulated in the agreement/contract under which the
// program(s) have been supplied.
// **********************************************************************

package com.gargjayesh.practice.karaf.displayunit.impl.internal;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PreMatching
@Provider
public class SimpleFilter implements ContainerRequestFilter
{
    private static final Logger LOG = LoggerFactory.getLogger(SimpleFilter.class);

    @Override
    public void filter(ContainerRequestContext context) throws IOException
    {
        LOG.error("Filter worked well!!!. | {}", context.getHeaders());
        MultivaluedMap<String, String> headers = context.getHeaders();
        if (!headers.get("Accept").contains("application/json"))
        {
            context.abortWith(Response.status(406).entity("{ \"result\" : \"Request is not asking for json response\" }").header("Content-Type", "application/json").build());
        }
        if (headers.get("Content-Type").size() != 1 || !headers.get("Content-Type").contains("application/json"))
        {
            context.abortWith(Response.status(415).entity("{ \"result\" : \"Request payload body is not of valid content type\" }").header("Content-Type", "application/json").build());
        }
    }
}
