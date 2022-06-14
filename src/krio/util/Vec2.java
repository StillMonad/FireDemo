package krio.util;

public class  Vec2 {
    public double x;
    public double y;

    public Vec2 (double x, double y) { this.x = x; this.y = y; }
    public Vec2 () { this(0,0); }
    public static Vec2 add(Vec2 a, Vec2 b) {
        return new Vec2(a.x + b.x, a.y + b.y);
    }
    public static Vec2 sub(Vec2 a, Vec2 b) {
        return new Vec2(a.x - b.x, a.y - b.y);
    }
    public static Vec2 mul(Vec2 a, double b) {
        return new Vec2(a.x * b, a.y * b);
    }
    public static Vec2 div(Vec2 a, double b) {
        return new Vec2(a.x / b, a.y / b);
    }
    public static Vec2 neg(Vec2 a) {
        return new Vec2(-a.x, -a.y);
    }
    public static double lenSq(Vec2 a) {
        return a.x * a.x + a.y * a.y;
    }
    public static double len(Vec2 a) {
        return Math.sqrt(a.x * a.x + a.y * a.y);
    }
    public static double dist(Vec2 a, Vec2 b) {
        Vec2 v = sub(a, b);
        return len(v);
    }
    public static double dot(Vec2 v1, Vec2 v2) {
        return v1.x * v2.x + v1.y * v2.y;
    }
    public static Vec2 normalize(Vec2 p) {
        double magnitude = len(p);
        return new Vec2(p.x / magnitude, p.y / magnitude);
    }
    public static Vec2 project(Vec2 v1, Vec2 v2) {
        return Vec2.mul( v2,Vec2.dot(v1, v2) / Vec2.lenSq(v2));
    }
    public static Vec2 reflect(Vec2 v, Vec2 normal) {
        double dn = 2 * dot(v, normal);
        return sub(v, mul(normal, dn));
    }
    @Override
    public String toString() {
        return "Vec2: " + x + ", " + y + ";";
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        hash = 31 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Vec2 other = (Vec2) obj;
        if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x)) {
            return false;
        }
        return Double.doubleToLongBits(this.y) == Double.doubleToLongBits(other.y);
    }
}
