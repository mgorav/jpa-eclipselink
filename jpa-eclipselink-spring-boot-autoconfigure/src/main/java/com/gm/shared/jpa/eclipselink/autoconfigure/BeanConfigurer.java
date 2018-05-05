package com.gm.shared.jpa.eclipselink.autoconfigure;

import com.gm.shared.jpa.eclipselink.customizer.profiler.PerformanceProfilerCustomizer;
import com.gm.shared.jpa.eclipselink.rest.mapping.visitor.MappingWeavingVisitor;
import com.gm.shared.jpa.eclipselink.rest.mapping.visitor.impl.DirectToFieldMappingWeavingVisitor;
import com.gm.shared.jpa.eclipselink.rest.mapping.visitor.impl.ManyToOneMappingWeavingVisitor;
import com.gm.shared.jpa.eclipselink.rest.mapping.visitor.impl.OneToManyMappingWeavingVisitor;
import com.gm.shared.jpa.eclipselink.rest.mapping.visitor.impl.OneToOneMappingWeavingVisitor;
import com.gm.shared.jpa.eclipselink.rest.mapping.visitor.locator.MappingVisitorLocator;
import com.gm.shared.jpa.eclipselink.rest.mapping.weaver.MappingWeavingCustomizer;
import org.eclipse.persistence.mappings.DatabaseMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfigurer {

    @Bean
    public BeanLocator beanLocator() {

        return new BeanLocator();
    }

    @Bean
    public DirectToFieldMappingWeavingVisitor directToFieldMappingWeavingVisitor() {

        return new DirectToFieldMappingWeavingVisitor();
    }

    @Bean
    public ManyToOneMappingWeavingVisitor manyToOneMappingWeavingVisitor() {
        return new ManyToOneMappingWeavingVisitor();
    }

    @Bean
    public OneToManyMappingWeavingVisitor oneToManyMappingWeavingVisitor() {
        return new OneToManyMappingWeavingVisitor();
    }

    @Bean
    public OneToOneMappingWeavingVisitor oneToOneMappingWeavingVisitor() {

        return new OneToOneMappingWeavingVisitor();
    }

    @Bean
    public <M extends DatabaseMapping, V extends MappingWeavingVisitor<M>> MappingVisitorLocator<M, V> mappingVisitorLocator() {

        return new MappingVisitorLocator<>();
    }

    @Bean
    public <M extends DatabaseMapping, V extends MappingWeavingVisitor<M>> MappingWeavingCustomizer<M, V> mappingWeavingCustomizer() {

        return new MappingWeavingCustomizer<>();
    }

    @Bean
    public PerformanceProfilerCustomizer performanceProfilerCustomizer() {

        return new PerformanceProfilerCustomizer();
    }


}
