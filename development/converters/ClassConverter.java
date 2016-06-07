/*
 * Copyright 2015-2016 ArtisTech, Inc.
 */
package com.artistech.utils.converters;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang3.tuple.ImmutablePair;

/**
 * Converts a string to a Class type.
 *
 * @author matta
 */
public class ClassConverter implements Converter, ConverterRegister {

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
        try {
            return (T) Class.forName(value.toString());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClassConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Get the provided conversions.
     *
     * @param convertUtilsBean
     */
    @Override
    public void register(ConvertUtilsBean convertUtilsBean) {
        convertUtilsBean.deregister(Class.class);
        convertUtilsBean.register(this, Class.class);
    }

    /**
     * Get the conversion types.
     *
     * @return
     */
    @Override
    public List<ImmutablePair<Class<?>, Class<?>>> getConversions() {
        ArrayList<ImmutablePair<Class<?>, Class<?>>> ret = new ArrayList<>();
        ret.add(new ImmutablePair<Class<?>, Class<?>>(String.class, Class.class));
        return ret;
    }
}
