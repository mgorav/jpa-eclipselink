package com.gm.shared.jpa.eclipselink.autoconfigure;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;

import java.util.ArrayList;
import java.util.List;

public class BeanLocator implements BeanFactoryAware {
    private static BeanFactory beanFactory;


    public static <T> T beanOfType(Class<T> type) {
        ListableBeanFactory.class.cast(beanFactory).getBeansOfType(type).values();
        return beanFactory.getBean(type);
    }

    public static <T> List<T> allBeansOfType(Class<T> type) {
        return new ArrayList<>(ListableBeanFactory.class.cast(beanFactory).getBeansOfType(type).values());
    }


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;

    }
}
