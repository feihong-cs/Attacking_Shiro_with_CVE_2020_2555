package com.feihong.template;

import java.io.IOException;

public class BasicCmdTemplate {
    public BasicCmdTemplate() throws IOException {
        Runtime.getRuntime().exec("calc");
    }
}
