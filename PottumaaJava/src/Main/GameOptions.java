package Main;

public class GameOptions {

    public static boolean ISDEBUGMODE = false;

    /*
     MENUSTATE = 0;
	 LEVEL1STATE = 1;
	 LEVEL2STATE = 2;
	 HELPSTATE = 3;
	 OPTIONSSTATE = 4;
	 MAPEDITORSTATE = 5;
    */
    public static int FIRSTSTATE = 1;

    public static void ToggleDebugMode() {
        ISDEBUGMODE = !ISDEBUGMODE;
    }
}
