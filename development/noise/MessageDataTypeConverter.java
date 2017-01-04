/*
 * Copyright 2015 ArtisTech, Inc.
 */
package com.artistech.algolink.orgs.noise;

import com.artistech.utils.converters.ConverterRegister;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang3.tuple.ImmutablePair;

/**
 *
 * @author matta
 */
public class MessageDataTypeConverter implements Converter, ConverterRegister {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T convert(Class<T> type, Object o) {
        String val = o.toString().replaceAll("\"", "");
        return (T) Enum.valueOf(MessageDataType.class, val);
    }

    @Override
    public void register(ConvertUtilsBean convertUtilsBean) {
        convertUtilsBean.deregister(MessageDataType.class);
        convertUtilsBean.register(this, MessageDataType.class);
    }

    @Override
    public List<ImmutablePair<Class<?>, Class<?>>> getConversions() {
        ArrayList<ImmutablePair<Class<?>, Class<?>>> ret = new ArrayList<>();
        ret.add(new ImmutablePair<Class<?>, Class<?>>(String.class, MessageDataType.class));
        return ret;
    }
}
