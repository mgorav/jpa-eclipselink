package com.gm.shared.jpa.eclipselink.rest.mapping.visitor;

import org.eclipse.persistence.mappings.DatabaseMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.gm.shared.jpa.eclipselink.asyncpersistence.util.CastUtil.uncheckedCast;

@Component
public class MappingVisitorLocator {

    private List<? extends MappingWeavingVisitor<? extends DatabaseMapping>> visitors;
    private Map<? extends DatabaseMapping, ? extends MappingWeavingVisitor<? extends DatabaseMapping>> mappingVsVisitors;

    @Autowired
    public MappingVisitorLocator(List<? extends MappingWeavingVisitor<? extends DatabaseMapping>> visitors) {
        this.visitors = visitors;

        visitors.stream().forEach( visitor -> {
            // TODO

        });
    }

    public <M extends DatabaseMapping,V extends MappingWeavingVisitor<M>> V visitorForMapping(M mapping) {

        return uncheckedCast(mappingVsVisitors.get(mapping.getClass()));
    }


}