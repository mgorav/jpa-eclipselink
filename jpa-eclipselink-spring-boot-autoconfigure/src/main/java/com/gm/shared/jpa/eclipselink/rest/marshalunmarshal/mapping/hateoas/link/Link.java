package com.gm.shared.jpa.eclipselink.rest.marshalunmarshal.mapping.hateoas.link;

import static org.eclipse.persistence.oxm.MediaType.APPLICATION_JSON;

public interface Link {

    String API_CONTEXT_ROOT = "api";
    String mediaType = APPLICATION_JSON.getMediaType();

    String REL_SELF = "self";
    String REL_PREVIOUS = "prev";
    String REL_NEXT = "next";
    String REL_FILE = "file";


    /**
     * URI to get more detail about a entity
     */
    String getHref();

    /**
     * Set URI
     */
    void setHref(String href);

    /**
     * Get the relation name
     */
    String getRel();

    /**
     * Set the rel
     */
    void setRel(String rel);

    /**
     * Get MediaType
     */
    default String getMediaType() {
        return mediaType;
    }
    default void setMediaType(String mediaType) {
        // DO NOTHING
    }

    /**
     * Give HTTP method
     */
    String getHttpMethod();

    /**
     * Set HTTP method
     */
    void setHttpMethod(String httpMethod);

}