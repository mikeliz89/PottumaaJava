package Main;

public class MathHelper {

    public static double roundFloat(float value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }
}
