package krio.world;

import krio.buffer.GetNeighbours;
import krio.util.Vec2;
import krio.world.BufferedParticle;
import krio.world.Particle;
import krio.world.Physics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static krio.util.GenerateRandom.getRandomDouble;

public class CollisionSolver {
    public static boolean solveAsVertlet = false;
    public static int solvedInThisIteration = 0;
    // public ================================================
    public static void solve(HashMap<Integer, BufferedParticle> particles, GetNeighbours buffer, List<Integer> idSortedList) {
        solvedInThisIteration = 0;
        for (int key : idSortedList) {
            BufferedParticle p1 = particles.get(key);
            if (!p1.isBuffered) continue;
            HashSet<Integer> ns = buffer.getNeighbours(p1.bufferedPos.x, p1.bufferedPos.y, (int)(2 * p1.radius * Physics.physicsAccuracy));
            for (Integer ind : ns) {
                BufferedParticle p2 = particles.get(ind);
                boolean collision = !solveAsVertlet ? solveCollision(p1, p2) : solveCollisionVertlet(p1, p2);
                if (collision) {
                    Physics.solveHeat(p1, p2);
                    ++solvedInThisIteration;
                }
            }
            ns.add(p1.id);
            recalcBufferPositions(particles, buffer, ns);
        }
    }

    // private ===============================================
    private static boolean solveCollision(Particle p1, Particle p2) {
        Vec2 dir = Vec2.sub(p1.pos, p2.pos);
        double dist = Vec2.len(dir);
        if(dist == 0) {
            dir = new Vec2(getRandomDouble(-10, 10), getRandomDouble(-10, 10));
        }
        if (dist >= p1.radius + p2.radius) return false;

        double offs = dist - p1.radius - p2.radius;
        Vec2 norm = Vec2.normalize(dir);

        p1.pos = Vec2.add(p1.pos, Vec2.mul(norm ,-0.51 * offs));
        p2.pos = Vec2.add(p2.pos, Vec2.mul(norm,  0.51 * offs));

        Vec2 pFrontVel = Vec2.project(p2.vel, dir);
        Vec2 pTangVel = Vec2.sub(p2.vel, pFrontVel);

        Vec2 thisFrontVel = Vec2.project(p1.vel, dir);
        Vec2 thisTangVel = Vec2.sub(p1.vel, thisFrontVel);

        Vec2 thisFrontVelNew = new Vec2();

        thisFrontVelNew.x = Physics.energyLoss * ((p1.mass - p2.mass) * thisFrontVel.x + 2 * p2.mass * pFrontVel.x) / (p1.mass + p2.mass);
        thisFrontVelNew.y = Physics.energyLoss * ((p1.mass - p2.mass) * thisFrontVel.y + 2 * p2.mass * pFrontVel.y) / (p1.mass + p2.mass);

        Vec2 pFrontVelNew = new Vec2();
        pFrontVelNew.x = Physics.energyLoss * ((p2.mass - p1.mass) * pFrontVel.x + 2 * p1.mass * thisFrontVel.x) / (p1.mass + p2.mass);
        pFrontVelNew.y = Physics.energyLoss * ((p2.mass - p1.mass) * pFrontVel.y + 2 * p1.mass * thisFrontVel.y) / (p1.mass + p2.mass);

        p1.vel = Vec2.add(thisTangVel, thisFrontVelNew);
        p2.vel = Vec2.add(pTangVel, pFrontVelNew);

        return true;
    }

    private static boolean solveCollisionVertlet(Particle p1, Particle p2) {
        Vec2 dir = Vec2.sub(p1.pos, p2.pos);
        double dist = Vec2.len(dir);
        if(dist == 0) {
            dir = new Vec2(getRandomDouble(-10, 10), getRandomDouble(-10, 10));
        }
        if (dist >= p1.radius + p2.radius) return false;

        double offs = dist - p1.radius - p2.radius;
        Vec2 norm = Vec2.normalize(dir);

        p1.pos = Vec2.add(p1.pos, Vec2.mul(norm ,-0.51 * offs));
        p2.pos = Vec2.add(p2.pos, Vec2.mul(norm,  0.51 * offs));

        p1.vel = Vec2.add(p1.vel, Vec2.mul(norm ,-0.51 * offs));
        p2.vel = Vec2.add(p2.vel, Vec2.mul(norm,  0.51 * offs));

        return true;
    }

    // for future creating multiple buffers from one
    /*private GetNeighbours[] splitBuffer(GetNeighbours buffer, int parts) {
        GetNeighbours[] buffers = new GetNeighbours[parts];
        int[][] data = buffer.getBuffer();
        int pos = 0;
        for (int i = 0; i < parts; ++i) {
            buffers[i] = new GetNeighbours(MyPanel.WIDTH,
                                    MyPanel.HEIGHT / parts,
                                            data.length,
                                      data[0].length / parts);
            for (int j = pos; j < data.length / parts; ++j) buffers[i][j - pos] = data[pos];
        }
    }*/

    private static void recalcBufferPositions(HashMap<Integer, BufferedParticle> particles, GetNeighbours buffer, HashSet<Integer> ns) {
        for (int part : ns) {
            BufferedParticle particle = particles.get(ns);
            if (particle != null) {
                buffer.getBuffer()[particle.bufferedPos.x][particle.bufferedPos.y] = 0;
                buffer.add(particle);
            }
        }
    }
}
