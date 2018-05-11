package com.gm.shared.jpa.eclipselink.rest.project.mapping.hateoas.link.builder.impl;

import com.gm.shared.jpa.eclipselink.autoconfigure.BeanLocator;
import com.gm.shared.jpa.eclipselink.rest.project.mapping.hateoas.link.Link;
import com.gm.shared.jpa.eclipselink.rest.project.mapping.hateoas.link.builder.LinkBuilder;
import com.gm.shared.jpa.eclipselink.rest.project.mapping.hateoas.link.context.LinkContext;
import com.gm.shared.jpa.eclipselink.rest.project.mapping.hateoas.link.impl.DefaultLink;
import com.gm.shared.jpa.eclipselink.rest.project.persistence.ServerSession;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.util.UriComponentsBuilder.fromPath;

/**
 * A default/sample implementation of LinkBuilder
 */
public class DefaultLinkBuilder<T> implements LinkBuilder<T> {

    private T entity;
    private String rel;


    public DefaultLinkBuilder() {
    }

    public DefaultLinkBuilder(T entity, String rel) {
        this.entity = entity;
        this.rel = rel;
    }

    /**
     * Current state of entity
     *
     * @param entity
     */
    @Override
    public void setLinkSource(T entity) {
        this.entity = entity;

    }

    /**
     * build and return {@link Link}(s)
     *
     * @param linkContext
     */
    @Override
    public List<Link> build(LinkContext linkContext) {

        List<Link> links = new ArrayList<>();
        links.add(getLink(entity, linkContext));

        return links;
    }

    private DefaultLink getLink(T entity, LinkContext linkContext) {

        DefaultLink link = new DefaultLink();

        Object pk = BeanLocator.beanOfType(ServerSession.class).getEmf().getId(entity);

        URI uri = fromPath(linkContext.getHostUrl() + entity.getClass().getSimpleName().toLowerCase())
                .path(pk != null ? pk.toString() : "0")
                .build().toUri();

        link.setHref(uri.toString());
        link.setRel(rel == null ? Link.REL_SELF : rel);

        return link;
    }

    /**
     * Set the rel
     *
     * @param rel
     */
    @Override
    public void setRel(String rel) {

        this.rel = rel;

    }
}
