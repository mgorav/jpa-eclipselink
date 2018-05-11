package com.gm.shared.jpa.eclipselink.rest.project.mapping.hateoas.link.builder;

import com.gm.shared.jpa.eclipselink.rest.project.mapping.hateoas.link.Link;
import com.gm.shared.jpa.eclipselink.rest.project.mapping.hateoas.link.context.LinkContext;

import java.util.List;

public interface LinkBuilder<T> {

    /**
     * Current state of entity
     */
    void setLinkSource(T entity);

    /**
     * build and return {@link Link}(s)
     */
    List<Link> build(LinkContext linkContext);

    /**
     * Set the rel
     */
    void setRel(String rel);

}
