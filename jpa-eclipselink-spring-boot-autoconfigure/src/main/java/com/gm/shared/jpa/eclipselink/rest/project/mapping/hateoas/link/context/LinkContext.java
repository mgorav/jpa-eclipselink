package com.gm.shared.jpa.eclipselink.rest.project.mapping.hateoas.link.context;

import org.eclipse.persistence.oxm.MediaType;
import org.springframework.http.HttpMethod;

public class LinkContext {

    private String hostUrl;
    private MediaType mediaType;
    private HttpMethod httpMethod;

    public LinkContext(String hostUrl,HttpMethod httpMethod) {

        this.hostUrl = hostUrl;
        this.mediaType = MediaType.APPLICATION_JSON;
        this.httpMethod = httpMethod;
    }

    public String getHostUrl() {

        return hostUrl;
    }

    public MediaType getMediaType() {

        return mediaType;
    }

    public HttpMethod getHttpMethod() {

        return httpMethod;
    }
}
