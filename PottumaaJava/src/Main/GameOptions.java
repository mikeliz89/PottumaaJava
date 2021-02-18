package Main;

public class GameOptions {

    /* Flags */
    public static boolean IS_DEBUG_MODE = false;
    public static boolean IS_PLAY_MUSIC_ON = false;

    /* States */
    public static int FIRST_STATE = 0;

    /* functions */
    public static void ToggleDebugMode() { IS_DEBUG_MODE = !IS_DEBUG_MODE; }
    public static void ToggleMusicOnOff() { IS_PLAY_MUSIC_ON = !IS_PLAY_MUSIC_ON; }
}
