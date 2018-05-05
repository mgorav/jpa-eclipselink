package com.gm.shared.jpa.eclipselink.rest.mapping.context;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.eclipse.persistence.oxm.XMLDescriptor;
import org.eclipse.persistence.oxm.mappings.XMLMapping;

public class MappingWeavingArtifact<M extends XMLMapping> {
    private XMLDescriptor xmlDescriptor;
    private M xmlMapping;

    public MappingWeavingArtifact(XMLDescriptor xmlDescriptor, M xmlMapping) {
        this.xmlDescriptor = xmlDescriptor;
        this.xmlMapping = xmlMapping;
    }

    public XMLDescriptor getXmlDescriptor() {
        return xmlDescriptor;
    }

    public M getXmlMapping() {
        return xmlMapping;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MappingWeavingArtifact<?> that = (MappingWeavingArtifact<?>) o;
        return Objects.equal(xmlDescriptor, that.xmlDescriptor) &&
                Objects.equal(xmlMapping, that.xmlMapping);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(xmlDescriptor, xmlMapping);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("xmlDescriptor", xmlDescriptor)
                .add("xmlMapping", xmlMapping)
                .toString();
    }
}

