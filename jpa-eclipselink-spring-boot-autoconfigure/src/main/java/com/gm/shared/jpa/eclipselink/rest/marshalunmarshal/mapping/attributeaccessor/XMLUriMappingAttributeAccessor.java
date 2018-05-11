package com.gm.shared.jpa.eclipselink.rest.marshalunmarshal.mapping.attributeaccessor;

import com.gm.shared.jpa.eclipselink.rest.marshalunmarshal.mapping.hateoas.link.builder.impl.DefaultLinkBuilder;
import com.gm.shared.jpa.eclipselink.rest.marshalunmarshal.mapping.hateoas.link.context.LinkContext;
import com.gm.shared.jpa.eclipselink.rest.persistence.JpaService;
import com.gm.shared.jpa.eclipselink.utils.CastUtil;
import org.eclipse.persistence.exceptions.DescriptorException;
import org.eclipse.persistence.mappings.AttributeAccessor;
import org.eclipse.persistence.sessions.Session;

import static com.gm.shared.jpa.eclipselink.autoconfigure.BeanLocator.beanOfType;
import static com.gm.shared.jpa.eclipselink.rest.constant.JpaEclipseLinkConstant.CURRENT_HOST_URI;
import static com.gm.shared.jpa.eclipselink.rest.constant.JpaEclipseLinkConstant.CURRENT_HTTP_METHOD;
import static com.gm.shared.jpa.eclipselink.rest.marshalunmarshal.mapping.hateoas.link.Link.REL_SELF;
import static org.springframework.http.HttpMethod.resolve;

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
        linkBuilder.setLinkSource(CastUtil.uncheckedCast(object));
        linkBuilder.setRel(uriName.equals("link") ? REL_SELF : uriName);
        JpaService jpaService = beanOfType(JpaService.class);
        Session session = jpaService.getEmf();
        LinkContext linkContext = new LinkContext(session.getProperty(CURRENT_HOST_URI).toString(), resolve(session.getProperty(CURRENT_HTTP_METHOD).toString()));
        return linkBuilder.build(linkContext);
    }
}
