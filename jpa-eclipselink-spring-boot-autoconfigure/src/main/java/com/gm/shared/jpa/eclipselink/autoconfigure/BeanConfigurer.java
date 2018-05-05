package com.gm.shared.jpa.eclipselink.autoconfigure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfigurer {

    @Bean
    public BeanLocator beanLocator() {

        return new BeanLocator();
    }

}
