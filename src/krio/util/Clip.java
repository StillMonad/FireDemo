package krio.util;

public class Clip {
    public static <T extends Number> T clip(T a, T min, T max) {
        return a.doubleValue() > min.doubleValue() ? (a.doubleValue() < max.doubleValue() ? a : max) : min;
    }
}
