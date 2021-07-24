package com.sunrun.leaguemediaassistant.Model;

import java.util.Observable;
import java.util.Observer;

public class CurrentState extends Observable {


    /*
    * Scroll state controls what direction we are scrolling, and how fast
    * 0 --> stopped
    * 1-3 --> Forward (various speeds, 1 is default speed)
    * -1 - -3 --> Reverse (various speeds)
    * 50 --> sync
    * 60 --> inactive - Connected
    * 61 --> inactive - not connected (Client only)
    * 70 --> Fling Up
    * 80 --> Fling Down
    */
    private int scroll_state;

    //If we are manually scrolling, indicates how far to scroll
    private float man_scroll;

    //Variable to prevent back to back flings
    private boolean fling_wait;

    //Variable to hold sync scroll position
    private int scroll_position;

    //Variable to hold port number;
    private int port_number;

    //Variable to hold IP address;
    private String ip_address;

    //Address to fetch sync position from
    private String sync_ip;

    //Stores whether we have synced or not
    private boolean synced;

    //Stores whether the no wifi activity has been dropped
    private boolean no_wifi_activity_up;

    //Stores if the app has been opened (To prevent no-wifi from coming up when you haven't opened the app)
    private boolean app_open;

    /*
     * If the teleprompter clients get out of sync, the user can push a sync button that
     * will sit this variable to be true. As long as it remains true, we send the "scroll to top"
     * command when the clients ask. It is set to false when the user resumes normal scrolling.
     */
    private boolean sync_active;

    private boolean teleprompter_active; //This is false until we begin teleprompting

    static private CurrentState instance = new CurrentState();

    private CurrentState() {
        this.scroll_state = 0;
        this.port_number = 0;
        this.sync_active = false;
        this.teleprompter_active = false;
        this.no_wifi_activity_up = false;
        this.app_open = false;
    }

    public static void setApp_open(boolean app_open) {
        instance.app_open = app_open;
    }

    public static boolean isApp_open() {
        return instance.app_open;
    }

    public static void putObserver(Observer o) {
        instance.addObserver(o);
    }

    public static int getScroll_state() {
        return instance.scroll_state;
    }

    public static void setScroll_state(int scroll_state) {
        if (scroll_state!= 0){
           instance.sync_active = false;
        }

        instance.scroll_state = scroll_state;
    }

    public static boolean isSync_active() {
        return instance.sync_active;
    }

    public static void setSync_active(boolean sync_active) {
        instance.sync_active = sync_active;
    }

    public static boolean isTeleprompter_active() {
        return instance.teleprompter_active;
    }

    public static void setTeleprompter_active(boolean teleprompter_active) {
        instance.teleprompter_active = teleprompter_active;
    }

    public static float getMan_scroll() {
        return instance.man_scroll;
    }

    public static void setMan_scroll(float man_scroll) {
        instance.man_scroll = man_scroll;
    }

    public static int getPort_number() {
        return instance.port_number;
    }

    public static void setPort_number(int port_number) {
        instance.port_number = port_number;
    }

    public static void increment_port(){
        instance.port_number++;
    }

    public static String getIp_address() {
        return instance.ip_address;
    }

    public static void setIp_address(String ip_address) {
        instance.ip_address = ip_address;
    }

    public static boolean isFling_wait() {
        return instance.fling_wait;
    }

    public static void setFling_wait(boolean fling_wait) {
        instance.fling_wait = fling_wait;
    }

    public static int getScroll_position() {
        return instance.scroll_position;
    }

    public static void setScroll_position(int scroll_position) {
        instance.scroll_position = scroll_position;
    }

    public static String getSync_ip() {
        return instance.sync_ip;
    }

    public static void setSync_ip(String sync_ip) {
        instance.sync_ip = sync_ip;
    }

    public static boolean isSynced() {
        return instance.synced;
    }

    public static void setSynced(boolean synced) {
        instance.synced = synced;
    }

    public static boolean isNo_wifi_activity_up() {
        return instance.no_wifi_activity_up;
    }

    public static void setNo_wifi_activity_up(boolean no_wifi_activity_up) {
        instance.no_wifi_activity_up = no_wifi_activity_up;
        instance.setChanged();
        instance.notifyObservers();
    }
}
