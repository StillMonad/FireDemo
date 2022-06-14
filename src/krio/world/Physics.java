
package krio.world;

import krio.MyPanel;
import krio.buffer.GetNeighbours;
import krio.util.Vec2;
import static krio.util.GenerateRandom.getRandomDouble;
import  static krio.util.Clip.clip;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;

public class Physics {
    public static double G = 0.1;
    public static double timeSpeed = 0.02;
    public static double energyLoss = 0.998;
    public static double heatDiffusionSpeed = 0.015;
    public static double heatTransferSpeed = 0.5;
    public static double heatUpwardForce = 0.7;
    public static double heatIfLow = 0.4;
    public static double physicsAccuracy = 1;

    // particles can't go outside the box, can you?
    public static void applyConstraints(Particle a) {
        Vec2 pos = a.pos;

        if (pos.x < a.radius || pos.x + a.radius > MyPanel.WIDTH) a.vel = Vec2.reflect(Vec2.mul(a.vel, energyLoss), new Vec2(1, 0));
        if (pos.y < a.radius || pos.y + a.radius > MyPanel.HEIGHT) a.vel = Vec2.reflect(Vec2.mul(a.vel, energyLoss), new Vec2(0, 1));
        
        a.pos.x = clip(a.pos.x, a.radius, MyPanel.WIDTH - a.radius);
        a.pos.y = clip(a.pos.y, a.radius, MyPanel.HEIGHT - a.radius);
    }

    // particle falls down
    public static void applyGravity(Particle a, double dt) { a.vel = Vec2.add(a.vel, new Vec2(0, dt * G * timeSpeed)); }

    // particle starts flowing up if hot
    public static void applyHeatUpwardForce(Particle a, double dt) { a.vel = Vec2.add(a.vel, new Vec2(0, -dt * a.T * heatUpwardForce * timeSpeed)); }

    public static void move(Particle a, double dt) { a.pos = Vec2.add(a.pos, Vec2.mul(a.vel, dt * timeSpeed)); }

    // particle is cooling down with time
    public static void looseHeat(Particle a, double dt) {
        //a.T -= dt * heatDiffusionSpeed * a.T;
        a.T -= dt * heatDiffusionSpeed * timeSpeed;
    }

    // increase particle's temperature if Y is below something
    public static void addHeatIfLow(Particle a, double dt) {
        if (a.pos.y > MyPanel.HEIGHT - a.radius * 3.5) a.T += heatIfLow * dt * timeSpeed;
    }

    // change particle's temperature on collision
    public static void solveHeat(Particle a, Particle b) {
        double diff = a.T - b.T;
        a.T -= diff * heatTransferSpeed * timeSpeed;
        b.T += diff * heatTransferSpeed * timeSpeed;
    }
}
