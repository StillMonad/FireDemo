package krio.world;

import krio.util.GenerateUniqueId;
import krio.util.Vec2;

import java.awt.Color;

public class Particle {
    public Vec2 pos;
    public Vec2 vel;
    public double mass;
    public double radius;
    public Color color;
    public double T = 0.0;
    
    public final int id; // unique id

    Particle(double x, double y, double vx, double vy, double rad, double m) {
        pos = new Vec2(x, y);
        vel = new Vec2(vx, vy);
        mass = m;
        radius = rad;
        id = GenerateUniqueId.getUnique();
        color = Color.RED;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + this.id;
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
        final Particle other = (Particle) obj;
        return this.id == other.id;
    }
}
