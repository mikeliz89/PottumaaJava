package Main;

public class GameOptions {

    public static boolean ISDEBUGMODE = false;
    public static boolean IS_PLAY_MUSIC_ON = true;

    /*
     MENUSTATE = 0;
	 LEVEL1STATE = 1;
	 LEVEL2STATE = 2;
	 HELPSTATE = 3;
	 OPTIONSSTATE = 4;
	 MAPEDITORSTATE = 5;
    */
    public static int FIRSTSTATE = 0;

    public static void ToggleDebugMode() {
        ISDEBUGMODE = !ISDEBUGMODE;
    }
}
