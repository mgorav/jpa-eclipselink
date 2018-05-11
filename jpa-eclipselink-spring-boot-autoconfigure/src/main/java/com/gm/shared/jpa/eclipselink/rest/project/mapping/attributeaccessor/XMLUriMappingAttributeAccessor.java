package com.gm.shared.jpa.eclipselink.rest.project.mapping.attributeaccessor;

import com.gm.shared.jpa.eclipselink.rest.project.mapping.hateoas.link.Link;
import com.gm.shared.jpa.eclipselink.rest.project.mapping.hateoas.link.builder.impl.DefaultLinkBuilder;
import com.gm.shared.jpa.eclipselink.rest.project.mapping.hateoas.link.context.LinkContext;
import com.gm.shared.jpa.eclipselink.utils.CastUtil;
import org.eclipse.persistence.exceptions.DescriptorException;
import org.eclipse.persistence.mappings.AttributeAccessor;
import org.springframework.http.HttpMethod;

public class XMLUriMappingAttributeAccessor extends AttributeAccessor {

    private static final long serialVersionUID = 2935399385611111839L;
    private final String uriName;

    public XMLUriMappingAttributeAccessor(String uriName) {
        this.uriName = uriName;
    }


    @Override
    public void setAttributeValueInObject(Object object, Object value) throws DescriptorException {

        // Not used
    }

    @Override
    public Object getAttributeValueFromObject(Object object) throws DescriptorException {


        DefaultLinkBuilder<?> linkBuilder = new DefaultLinkBuilder<>();
        linkBuilder.setLinkSource( CastUtil.uncheckedCast(object));
        linkBuilder.setRel(uriName.equals("link") ? Link.REL_SELF : uriName);
        // TODO
        LinkContext linkContext = new LinkContext("http://localhost:8090/", HttpMethod.GET);
        return linkBuilder.build(linkContext);
    }
}
