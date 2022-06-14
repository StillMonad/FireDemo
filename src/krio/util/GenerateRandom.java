package krio.util;
import java.util.Random;


public class GenerateRandom {
    public static int seed = 1;
    private static Random rnd = new Random(seed);

    public static int getRandomInt(int min, int max) {
        return rnd.nextInt((max - min) + 1) + min;
    }
    public static double getRandomDouble(double min, double max) {
        return min + (max - min) * rnd.nextDouble();
    }
}
