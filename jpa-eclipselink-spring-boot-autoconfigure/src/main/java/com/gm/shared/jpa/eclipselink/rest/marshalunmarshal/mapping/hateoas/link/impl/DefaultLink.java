package com.gm.shared.jpa.eclipselink.rest.marshalunmarshal.mapping.hateoas.link.impl;

import com.gm.shared.jpa.eclipselink.rest.marshalunmarshal.mapping.hateoas.link.Link;

public class DefaultLink implements Link {

    private String href;
    private String rel = REL_SELF;
    private String httpMethod;
    private String mediaType;

    @Override
    public String getHref() {

        return href;
    }

    @Override
    public void setHref(String href) {

        this.href = href;

    }

    @Override
    public String getRel() {

        return this.rel;
    }

    @Override
    public void setRel(String rel) {

        this.rel = rel;
    }


    @Override
    public String getHttpMethod() {

        return httpMethod;
    }

    @Override
    public void setHttpMethod(String httpMethod) {

        this.httpMethod = httpMethod;
    }

}
