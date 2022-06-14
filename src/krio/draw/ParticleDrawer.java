
package krio.draw;

import krio.world.Particle;
import krio.util.Vec2;
import static krio.util.Clip.clip;

import java.awt.*;

public class ParticleDrawer {

    public enum Mode {
        THERMAL,
        REAL,
        REAL_CONTOUR,
        FIRE,
        ECONOMY,
        ECONOMY_ACTION,
        MINIMAL,
        MINIMAL_VECTORS
    }


    public static int drawn = 0;
    public static Mode mode = Mode.MINIMAL_VECTORS;

    public static void draw(Graphics2D gr, Particle p) {
        //p.T = clip(p.T, 0.0, 1.0);
        p.T = clip(p.T, 0.0, 1.5);
        int rad;
        double T;
        float r, g, b, a;
        Color c;
        double vel;
        Vec2 dir;

        switch (mode) {
            case THERMAL :
                if (p.T < 0.20) return;
                T = (p.T - 0.20) * 1.2;
                //int rad = (int)(p.radius);
                rad = (int) (p.radius * 3.0 * (1 - clip((float) T, 0.0f, 1.0f)) + 2 * p.radius);
                r = clip((float) T * 2.0f + 0.2f, 0.0f, 1.0f);
                g = clip(0.7f * (float) Math.pow(T, 0.9), 0.0f, 1.0f);
                b = clip(0.2f * (float) (1 - T) + 0.3f, 0.0f, 1.0f);
                a = clip(1.0f * (float) T + 0.1f, 0.0f, 1.0f);
                c = new Color(r, g, b, a);
                gr.setColor(c);
                gr.fillOval((int) p.pos.x - rad, (int) p.pos.y - rad, rad * 2, rad * 2);
                break;
            case REAL :
                rad = (int) (p.radius);
                T = p.T;
                r = clip((float) T * 3.7f + 0.15f, 0.0f, 1.0f);
                g = clip(0.8f * (float) Math.pow(T, 0.9) + 0.15f, 0.0f, 1.0f);
                b = clip(0.1f * (float) (1 - T) + 0.1f, 0.0f, 1.0f);
                a = clip(1f, 0.0f, 1.0f);
                c = new Color(r, g, b, a);
                gr.setColor(c);
                gr.fillOval((int) p.pos.x - rad, (int) p.pos.y - rad, rad * 2, rad * 2);
                c = c.brighter().brighter();
                gr.setColor(c);
                gr.drawOval((int) p.pos.x - rad, (int) p.pos.y - rad, rad * 2, rad * 2);
                break;
            case REAL_CONTOUR :
                rad = (int) (p.radius);
                T = p.T;
                r = clip((float) T * 1.7f + 0.2f, 0.0f, 1.0f);
                g = clip(0.8f * (float) Math.pow(T, 0.9), 0.0f, 1.0f);
                b = clip(0.4f * (float) (1 - T), 0.0f, 1.0f);
                a = clip(1f, 0.0f, 1.0f);
                c = new Color(r, g, b, a);
                gr.setColor(c);
                gr.drawOval((int) p.pos.x - rad - 1, (int) p.pos.y - rad - 1, rad * 2 - 2, rad * 2 - 2);
                break;
            case FIRE :
                ++drawn;
                //if (drawn % 2 != 0) return;
                if (p.T < 0.20) return;
                T = (p.T - 0.20) * 1.2;
                double middle = 0.9;
                double diff = Math.abs(T - middle);
                double change = clip((float) (1.0 - diff) * 2, 0.5f, 100.0f) * 3;
                //int rad = (int) (2 * p.radius * (1 - clip((float)T, 0.0f, 1.0f)) + change * 2f * p.radius);
                rad = (int) (change * p.radius + Math.abs(1 - T) * 2 * p.radius);
                r = clip((float) T * 1.3f + 0.4f, 0.0f, 1.0f);
                g = clip(0.8f * (float) Math.pow(T, 0.7), 0.0f, 1.0f);
                b = clip(0.1f * (float) (1 - T), 0.0f, 1.0f);
                a = clip(0.7f * (float) (Math.pow(T, 0.8) + 0.1f), 0.0f, 1.0f);
                c = new Color(r, g, b, a);
                gr.setColor(c);
                vel = Vec2.len(p.vel);
                if (vel > 45 && p.T > 0.2985 && p.T < 30) {
                    dir = Vec2.normalize(p.vel);
                    //gr.drawOval((int) p.pos.x - rad - (int)(dir.x * 2), (int) p.pos.y - rad - (int)(dir.y * 2), rad * 2, rad * 2);
                    a = clip(a * 2, 0.0f, 1.0f);
                    c = new Color(r, g, b, a);
                    c = c.brighter().brighter();
                    gr.setColor(c);
                    rad /= vel * 0.05;
                    //gr.fillOval((int) p.pos.x - rad, (int) p.pos.y - rad, rad * 2, rad * 2);
                    vel *= 0.15;
                    for (int i = 0; i < vel; i += 1) {
                        gr.fillOval((int) p.pos.x - rad - (int) (dir.x * i), (int) p.pos.y - rad - (int) (dir.y * i), rad * 2, rad * 2);
                        //rad *= 0.999;
                    }
                } else {
                    gr.fillOval((int) p.pos.x - rad, (int) p.pos.y - rad, rad * 2, rad * 2);
                }
                break;
            case ECONOMY :
                ++drawn;
                //if (drawn % 1 != 0) return;
                if (p.T < 0.05) return;
                T = (p.T - 0.05) * 1.10;
                rad = (int) (p.radius);
                //double T = p.T;
                r = clip((float) T * 1.7f + 0.2f, 0.0f, 1.0f);
                g = clip(0.8f * (float) Math.pow(T, 0.9), 0.0f, 1.0f);
                b = clip(0.4f * (float) (1 - T), 0.0f, 1.0f);
                a = clip(1f * (float) T + 0.2f, 0.0f, 1.0f);
                c = new Color(r, g, b, a);
                gr.setColor(c);
                //gr.drawOval((int) p.pos.x - rad, (int) p.pos.y - rad, rad * 2, rad * 2);
                gr.fillRect((int) p.pos.x - rad, (int) p.pos.y - rad, rad * 2, rad * 2);
                break;
            case ECONOMY_ACTION :
                ++drawn;
                //if (drawn % 1 != 0) return;
                if (p.T < 0.07) return;
                T = (p.T - 0.10) * 1.15;
                rad = (int) (p.radius);
                //double T = p.T;
                r = clip((float) T * 1.6f + 0.2f, 0.0f, 1.0f);
                g = clip(0.8f * (float) Math.pow(T, 0.9), 0.0f, 1.0f);
                //float b = clip(T > 0.15 ? (float)Math.pow(T- 0.4, 2) * 10.0f : 2 * (float)Math.sqrt(T), 0.0f, 1.0f);
                b = clip(0.5f * (float) (1 - T), 0.0f, 1.0f);
                a = clip(1f, 0.0f, 1.0f);
                c = new Color(r, g, b, a);
                gr.setColor(c);
                gr.drawOval((int) p.pos.x - rad, (int) p.pos.y - rad, rad * 2, rad * 2);
                dir = Vec2.normalize(p.vel);
                vel = Vec2.len(p.vel) / 4;
                //gr.drawOval((int) p.pos.x - rad - (int)(dir.x * 2), (int) p.pos.y - rad - (int)(dir.y * 2), rad * 2, rad * 2);
                gr.drawOval((int) p.pos.x - rad / 2 - (int) (dir.x * vel), (int) p.pos.y - rad / 2 - (int) (dir.y * vel), rad * 1, rad * 1);
                break;
            case MINIMAL :
                T = p.T;
                //double T = p.T;
                //if (p.T < 0.1) return;
                r = clip((float) T * 1.6f + 0.2f, 0.0f, 1.0f);
                g = clip(0.8f * (float) Math.pow(T, 0.9), 0.0f, 1.0f);
                //float b = clip(T > 0.15 ? (float)Math.pow(T- 0.4, 2) * 10.0f : 2 * (float)Math.sqrt(T), 0.0f, 1.0f);
                b = clip(0.5f * (float) (1 - T), 0.0f, 1.0f);
                a = clip((float) T * 1f + 0.2f, 0.0f, 1.0f);
                c = new Color(r, g, b, a);
                gr.setColor(c);
                gr.drawRect((int) p.pos.x, (int) p.pos.y, 2, 2);
                break;
            case MINIMAL_VECTORS :
                ++drawn;
                //if (drawn % 1 != 0) return;
                //if (p.T < 0.07) return;
                T = (p.T);// - 0.10) * 1.15;
                rad = (int) (p.radius);
                //double T = p.T;
                r = clip((float) T * 5.0f + 0.15f, 0.0f, 1.0f);
                g = clip(1.0f * (float) Math.pow(T, 0.9) + 0.15f, 0.0f, 1.0f);
                b = clip(0.1f * (float) (1 - T) + 0.1f, 0.0f, 1.0f);
                a = clip(1f, 0.0f, 1.0f);
                c = new Color(r, g, b, a);
                gr.setColor(c);
                gr.drawRect((int) p.pos.x - 1, (int) p.pos.y - 1, 2, 2);
                dir = Vec2.normalize(p.vel);
                vel = 5 + Vec2.len(p.vel) * 1;
                //gr.drawOval((int) p.pos.x - rad - (int)(dir.x * 2), (int) p.pos.y - rad - (int)(dir.y * 2), rad * 2, rad * 2);
                gr.drawLine((int) p.pos.x - rad / 2, (int) p.pos.y - rad / 2, (int) p.pos.x - rad / 2 + (int) (dir.x * vel), (int) p.pos.y - rad / 2 + (int) (dir.y * vel));
                break;
            default:
                break;
        }
        /*if (T > 0.6) {
            double shine = (T - 0.6) * 2.5;
            Point center = new Point((int) p.pos.x, (int) p.pos.y);
            float[] dist = {0.0f, 1.0f};
            Color[] colors = {new Color(r , g , b * 2, 1.0f), new Color(r, g, b * 2, 0.2f)};
            RadialGradientPaint gradient =
                    new RadialGradientPaint(center, (float)shine * 3 * rad, dist, colors);
            gr.setPaint(gradient);
            gr.fill(new Ellipse2D.Double(
                    (int) p.pos.x - (int) ((float)shine * 6 * rad),
                    (int) p.pos.y - (int) ((float)shine * 6 * rad),
                    (int) ((float)shine * 6 * rad * 2),
                    (int) ((float)shine * 6 * rad * 2)));
        }*/
    }
}
