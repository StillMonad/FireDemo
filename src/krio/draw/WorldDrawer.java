
package krio.draw;

import krio.world.BufferedParticle;
import krio.world.World;

import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class WorldDrawer {
    public static void draw(Graphics2D g, World w) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (int key : w.particles.keySet()) {
            BufferedParticle p = w.particles.get(key);
            ParticleDrawer.draw(g, p);
        }
    }
}
