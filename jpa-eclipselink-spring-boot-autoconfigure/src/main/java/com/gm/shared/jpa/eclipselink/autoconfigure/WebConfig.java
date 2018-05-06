package com.gm.shared.jpa.eclipselink.autoconfigure;

import com.gm.shared.jpa.eclipselink.rest.converter.HttpMessageConverterImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@EnableWebMvc
@Configuration
@ConditionalOnProperty("gm.shared.jpa.spring.rest")
public class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

        converters.add(httpMessageConverter());

        super.configureMessageConverters(converters);

    }

    @Bean
    public <T> HttpMessageConverterImpl<T> httpMessageConverter() {

        return new HttpMessageConverterImpl<>();
    }
}
