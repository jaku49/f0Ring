package me.f0rant.f0ring.util;

public class FastMath {
    private static final float[] sin = new float[65536];

    static {
        for (int i = 0; i < 65536; ++i) {
            sin[i] = (float) Math.sin((double) i * Math.PI * 2.0D / 65536.0D);
        }
    }

    public static double sin(double rad) {
        float f = (float) rad;
        return sin[(int) (f * 10430.378F) & 65535];
    }

    public static double cos(double rad) {
        float f = (float) rad;
        return sin[(int) (f * 10430.378F + 16384.0F) & 65535];
    }
}