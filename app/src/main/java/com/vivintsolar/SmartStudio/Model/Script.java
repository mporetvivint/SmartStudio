package com.vivintsolar.SmartStudio.Model;

public class Script {

    private String script;
    static private Script instance = new Script();

    private Script() {
        this.script = null;
    }

    public static void setScript(String script) {
        instance.script = script;
    }

    public static String getScript() {

        return instance.script;
    }

}
