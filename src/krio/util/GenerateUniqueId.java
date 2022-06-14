package krio.util;

public class GenerateUniqueId {
    private static int id = 1;
    static public int getUnique() {
        return ++id;
    }
}
