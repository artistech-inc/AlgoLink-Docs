/*
 * Copyright 2015-2016 ArtisTech, Inc.
 */
package com.artistech.utils.converters;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang3.tuple.ImmutablePair;

/**
 * Convert Color to/from String.
 *
 * @author matta
 */
public class ColorConverter implements Converter, ConverterRegister {

    /**
     * Convert.
     *
     * @param <T>
     * @param type
     * @param value
     * @return
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T convert(Class<T> type, Object value) {
        if (type == String.class) {
            if (value.getClass() == Color.class) {
                Color c = (Color) value;
                return (T) String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
            } else {
                return (T) value;
            }
        } else {
            try {
                return (T) Color.class.getField(value.toString()).get(null);
            } catch (NoSuchFieldException ex) {
                return (T) Color.decode(value.toString());
            } catch (SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            }
            return (T) Color.decode(value.toString());
        }
    }

    /**
     * Register the conversion types.
     *
     * @param convertUtilsBean
     */
    @Override
    public void register(ConvertUtilsBean convertUtilsBean) {
        convertUtilsBean.deregister(String.class);
        convertUtilsBean.deregister(Color.class);
        convertUtilsBean.register(this, String.class);
        convertUtilsBean.register(this, Color.class);
    }

    /**
     * Get provided conversions.
     *
     * @return
     */
    @Override
    public List<ImmutablePair<Class<?>, Class<?>>> getConversions() {
        ArrayList<ImmutablePair<Class<?>, Class<?>>> ret = new ArrayList<>();
        ret.add(new ImmutablePair<Class<?>, Class<?>>(String.class, Color.class));
        ret.add(new ImmutablePair<Class<?>, Class<?>>(Color.class, String.class));
        return ret;
    }

}
