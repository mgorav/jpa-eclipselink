package com.gm.shared.jpa.eclipselink.asyncpersistence.util;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class CastUtil {

    @SuppressWarnings("unchecked")
    public static <X> List<X> listCast(List<? extends X> target) {

        return (List<X>) target;
    }

    @SuppressWarnings("unchecked")
    public static <X> List<X> rawListCast(List<?> target) {

        return (List<X>) target;
    }

    @SuppressWarnings("unchecked")
    public static <X> List<X> castToList(Object target) {

        return (List<X>) target;
    }

    @SuppressWarnings("unchecked")
    public static <X, Y> Map<X, Y> castToMap(Object target) {

        return (Map<X, Y>) target;
    }

    public static Long toLong(BigDecimal source) {

        if (source != null) {
            return source.longValue();
        }
        return 0L;
    }

    @SuppressWarnings("unchecked")
    public static <T> T uncheckedCast(Object o) {

        return (T) o;
    }
}

