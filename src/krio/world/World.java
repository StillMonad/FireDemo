
package krio.world;

import krio.MyPanel;
import krio.buffer.GetNeighbours;
import static krio.util.GenerateRandom.getRandomInt;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;

public class World {
    public LinkedHashMap<Integer, BufferedParticle> particles;
    public GetNeighbours buffer;
    public List<Integer> idSortedList;

    public double particleRad = 3.0;
    public double bufferAccuracy = 1.5 / particleRad; // minimal is 1 / particleRad, higher buffer accuracy increases stability but lowers performance
    public int particleAmount = 6000;
    public int dtRecalcIterations = 5;  // subiterations of physics like run(dt/dtRecalcIterations)
    public static boolean solveWhileCollisionExist = false; // solveAllCollisions() while collisionCount > 1

    public World(){
        Physics.physicsAccuracy = bufferAccuracy;
        particles = new LinkedHashMap<Integer, BufferedParticle>();
        buffer = new GetNeighbours(MyPanel.WIDTH,
                                   MyPanel.HEIGHT,
                                   (int)(MyPanel.WIDTH * bufferAccuracy),
                                   (int)(MyPanel.HEIGHT * bufferAccuracy));
        BufferedParticle p;
        for (int i = 0; i < particleAmount; ++i) {
            p = new BufferedParticle(getRandomInt(0, MyPanel.WIDTH), getRandomInt(0, MyPanel.HEIGHT), 0, 0, particleRad, 1);
            particles.put(p.id, p);
        }

        idSortedList = new ArrayList<Integer>(particles.keySet());

        // Pre-solve collisions before start O(n^2) - might take some time on large sets;
        for (int key : particles.keySet()) {
            BufferedParticle pr = particles.get(key);
            pr.bufferedPos = buffer.add(pr);
            pr.isBuffered = pr.bufferedPos != null;
        }
        CollisionSolver.solve(particles, buffer, idSortedList);
    }
    
    public void run(double dt) {
        dt = dt / dtRecalcIterations;

        // sort particles by pos.x for future threading
        Collections.sort(idSortedList, new Comparator<Integer>() {
            @Override
            public int compare(Integer i1, Integer i2) {
                return particles.get(i1).pos.x < particles.get(i2).pos.x ? 0 : 1;
            }
        });

        buffer.reset();
        for (int key : idSortedList) {
            BufferedParticle p = particles.get(key);
            p.bufferedPos = buffer.add(p);
            p.isBuffered = p.bufferedPos != null;
        }
        for (int dtRec = 0; dtRec < dtRecalcIterations; ++dtRec) {
            for (int key : idSortedList) {
                BufferedParticle p = particles.get(key);
                Physics.applyGravity(p, dt);
                Physics.looseHeat(p, dt);
                Physics.addHeatIfLow(p, dt);
                Physics.applyHeatUpwardForce(p, dt);
                Physics.move(p, dt);
                Physics.applyConstraints(p);
            }
            if (solveWhileCollisionExist) {
                int iter = 0;
                do {
                    CollisionSolver.solve(particles, buffer, idSortedList);
                    ++iter;
                }
                while (CollisionSolver.solvedInThisIteration > 0 && iter < 20);
            } else
            {
                CollisionSolver.solve(particles, buffer, idSortedList);
            }
            // tried to use naive new Thread() approach (work out as expected - it is slow, cause creating 200 threads per second is expensive)
            /*List<Integer> l1 = idSortedList.subList(0, idSortedList.size() / 3);
            List<Integer> l2 = idSortedList.subList(idSortedList.size() / 3, 2 * idSortedList.size() / 3);
            List<Integer> l3 = idSortedList.subList(idSortedList.size() / 3, 2 * idSortedList.size() / 3);
            Thread t1 = new Thread(() -> CollisionSolver.solve(particles, buffer, l1));
            Thread t2 = new Thread(() -> CollisionSolver.solve(particles, buffer, l2));
            Thread t3 = new Thread(() -> CollisionSolver.solve(particles, buffer, l2));
            t1.run();
            t2.run();
            t3.run();
            try {
                t1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                t2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                t3.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }
    }
}
