package com.gm.shared.jpa.eclipselink.rest.project.mapping.visitor.locator;

import com.gm.shared.jpa.eclipselink.rest.project.mapping.visitor.MappingWeavingVisitor;
import com.gm.shared.jpa.eclipselink.rest.project.mapping.weaving.context.MappingWeavingContext;
import org.eclipse.persistence.mappings.DatabaseMapping;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gm.shared.jpa.eclipselink.utils.CastUtil.uncheckedCast;
import static org.slf4j.LoggerFactory.getLogger;

@Component
public class MappingVisitorLocator<M extends DatabaseMapping, V extends MappingWeavingVisitor<M>> {
    private static final Logger log = getLogger(MappingVisitorLocator.class);
    private Map<Class<M>, MappingWeavingVisitor<M>> mappingVsVisitors;

    @Autowired
    private List<V> visitors;


    public V visitorForMapping(M mapping) {

        V visitor = uncheckedCast(mappingVsVisitors.get(mapping.getClass()));

        return uncheckedCast(visitor != null ? visitor : new MappingWeavingVisitor<M>() {
            @Override
            public void visit(MappingWeavingContext<M> context) {
                // DO NOTHING

                log.info(String.format("%s#%s not supported, hence just ignoring",
                        context.getCurrentClassDescriptor().getJavaClass().getSimpleName(),
                        context.getCurrentDatabaseMapping().getClass().getSimpleName()));

            }

            @Override
            public Class<M> getDatabaseMapping() {
                return null;
            }
        });
    }


    @PostConstruct
    public void postConstruct() {

        mappingVsVisitors = new HashMap<>(visitors.size());
        visitors.stream().forEach(visitor -> {
            mappingVsVisitors.put(visitor.getDatabaseMapping(), visitor);

        });
    }

}
