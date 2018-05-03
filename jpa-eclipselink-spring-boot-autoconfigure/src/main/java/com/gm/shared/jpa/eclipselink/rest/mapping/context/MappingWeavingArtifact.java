package com.gm.shared.jpa.eclipselink.rest.mapping.context;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.eclipse.persistence.oxm.XMLDescriptor;
import org.eclipse.persistence.oxm.mappings.XMLMapping;

@AllArgsConstructor
@Data
@EqualsAndHashCode
@ToString
public class MappingWeavingArtifact<M extends XMLMapping> {
    private XMLDescriptor xmlDescriptor;
    private M xmlMapping;
}
