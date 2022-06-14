
package krio.world;

import java.awt.Point;

public class BufferedParticle extends Particle {
    public boolean isBuffered = false;
    public Point bufferedPos;
    
    BufferedParticle(double x, double y, double vx, double vy, double rad, double m) {
        super(x, y, vx, vy, rad, m);
        isBuffered = false;
        bufferedPos = new Point(0, 0);
    }
}
