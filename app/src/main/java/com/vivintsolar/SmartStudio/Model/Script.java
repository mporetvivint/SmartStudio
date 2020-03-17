package com.vivintsolar.SmartStudio.Model;

public class Script {

    private String script;
    private int width; //Window Size of controller window
    private int height;
    static private Script instance = new Script();

    private Script() {
        this.script = null;
    }

    public static String getWindow_size() {
        return instance.width + "\t" + instance.height + "\t" ;
    }

    public static void setWindow_size(int width, int height) {
        instance.width = width ;
        instance.height = height;
    }

    public static int getWidth() {
        return instance.width;
    }

    public static int getHeight() {
        return instance.height;
    }

    public static void setScript(String script) {
        instance.script = script;
    }

    public static String getScript() {

        return instance.script;
    }

}
