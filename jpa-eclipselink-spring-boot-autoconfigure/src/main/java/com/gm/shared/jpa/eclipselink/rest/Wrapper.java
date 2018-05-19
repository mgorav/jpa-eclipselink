/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// Copyright (C) 2009-2016, Oracle Corporation, All rights reserved.
//
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
package com.gm.shared.jpa.eclipselink.rest;

import com.gm.shared.jpa.eclipselink.rest.marshalunmarshal.mapping.hateoas.link.Link;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.lang.String.valueOf;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * INTERNAL
 * To marshal/unmarshal a list with a JAXB (JSR-222) implementation we need to create a wrapper object to hold the list.
 */
final public class Wrapper<T> {

    private int DEFAULT_PAGE_SIZE = 200;

    private Collection<T> items;
    // Link. Framework will create default pagination links. But its possible to add custom links of type Link
    private List<Link> links;
    // Pagination
    private Boolean hasMore = false;
    private String limit = valueOf(DEFAULT_PAGE_SIZE);
    private String offset;
    private String count;

    public Wrapper() {

        items = new ArrayList<>(DEFAULT_PAGE_SIZE);
    }


    public List<Link> getLinks() {

        return links;
    }

    public void setLinks(List<Link> links) {

        this.links = links;
    }

    public void addLink(Link link) {

        if (links == null) {
            // add links apart from self/next/prev
            links = new ArrayList<>();
        }
        links.add(link);
    }

    public Wrapper(List<T> items) {

        this.items = items;
    }

    public Collection<T> getItems() {

        return items;
    }

    public void setItems(Collection<T> items) {

        this.items = items;
    }

    public Class<?> getType() {

        return isEmpty(items) ? null : items.iterator().next().getClass();
    }

    public boolean isHasMore() {

        return hasMore;
    }

    public void setHasMore(boolean hasMore) {

        this.hasMore = hasMore;
    }

    public String getLimit() {

        return limit;
    }

    public void setLimit(String limit) {

        this.limit = limit;
    }

    public String getOffset() {

        return offset;
    }

    public void setOffset(String offset) {

        this.offset = offset;
    }

    public String getCount() {

        return count;
    }

    public void setCount(String count) {

        this.count = count;
    }
}