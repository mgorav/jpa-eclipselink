package com.gm.shared.jpa.eclipselink.rest.project.mapping.hateoas;

import com.gm.shared.jpa.eclipselink.rest.project.mapping.attributeaccessor.XMLUriMappingAttributeAccessor;
import com.gm.shared.jpa.eclipselink.rest.project.mapping.hateoas.link.impl.DefaultLink;
import com.gm.shared.jpa.eclipselink.rest.project.mapping.weaving.context.MappingWeavingContext;
import org.eclipse.persistence.dynamic.DynamicClassLoader;
import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.oxm.XMLDescriptor;
import org.eclipse.persistence.oxm.mappings.XMLCompositeCollectionMapping;
import org.eclipse.persistence.oxm.mappings.XMLDirectMapping;
import org.springframework.stereotype.Component;

import static com.gm.shared.jpa.eclipselink.rest.project.mapping.classloader.DynamicClassLoaderUtil.getDynamicClassLoader;

@Component
public class LinkMapping {

    public void constructRestLink(MappingWeavingContext<? extends DatabaseMapping> context) {

        DynamicClassLoader dynamicClassLoader = getDynamicClassLoader(context.getServerSession(), null, null);

        // A dynamic class is generated for a Link
        final Class<?> linkClass = dynamicClassLoader.createDynamicClass(
                DefaultLink.class.getPackage().getName() + "." + context.getCurrentClassDescriptor().getJavaClass().getSimpleName()
                        + DefaultLink.class.getSimpleName(),
                DefaultLink.class);

        XMLDescriptor linkCd = context.newXMLDescriptor(linkClass);
        createBasicLinkDescriptorWithMappings(linkCd);


        if (linkCd != null) {

            XMLCompositeCollectionMapping xmlUriMapping = getLinkMapping(linkClass);
            xmlUriMapping.setAttributeAccessor(new XMLUriMappingAttributeAccessor("link"));
            context.addMappingToCurrentXMLDescriptor(xmlUriMapping);

        }

    }


    private void createBasicLinkDescriptorWithMappings(XMLDescriptor defaultDescriptor) {

        XMLDirectMapping href = new XMLDirectMapping();
        href.setWeight(1);
        href.setAttributeName("href");
        href.setXPath("@" + "href" + "/text()");
        href.setIsOptional(true);
        defaultDescriptor.addMapping(href);

        XMLDirectMapping rel = new XMLDirectMapping();
        rel.setWeight(2);
        rel.setAttributeName("rel");
        rel.setXPath("@" + "rel" + "/text()");
        rel.setIsOptional(true);
        defaultDescriptor.addMapping(rel);

        XMLDirectMapping type = new XMLDirectMapping();
        type.setWeight(3);
        type.setAttributeName("mediaType");
        type.setXPath("@" + "type" + "/text()");
        type.setIsOptional(true);
        defaultDescriptor.addMapping(type);

        XMLDirectMapping httpMethod = new XMLDirectMapping();
        httpMethod.setWeight(4);
        httpMethod.setAttributeName("httpMethod");
        httpMethod.setXPath("@" + "httpMethod" + "/text()");
        httpMethod.setIsOptional(true);
        defaultDescriptor.addMapping(httpMethod);

    }

    private XMLCompositeCollectionMapping getLinkMapping(final Class<?> linkClass) {

        XMLCompositeCollectionMapping xmlUriMapping = new XMLCompositeCollectionMapping();
        xmlUriMapping.setIsOptional(true);
        xmlUriMapping.setReferenceClass(linkClass);
        xmlUriMapping.setAttributeName("link");
        xmlUriMapping.setXPath("links/link");
        return xmlUriMapping;
    }
}
