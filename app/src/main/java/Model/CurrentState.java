package Model;

public class CurrentState {


    /*
    * Scroll state controls what direction we are scrolling, and how fast
    * 0 --> stopped
    * 1-3 --> Forward (various speeds, 1 is default speed)
    * -1 - -3 --> Reverse (various speeds)
    * 50 --> sync
    * 60 --> inactive
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

    //Address to fetch sync position from
    private String sync_ip;

    //Stores whether we have synced or not
    private boolean synced;

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
        this.sync_active = false;
        this.teleprompter_active = false;
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
}
