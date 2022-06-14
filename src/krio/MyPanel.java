package krio;
import krio.draw.ParticleDrawer;
import krio.draw.WorldDrawer;
import krio.world.Particle;
import krio.world.World;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MyPanel extends JPanel implements ActionListener {
    public static int WIDTH  = 800;
    public static int HEIGHT = 800;

    World world;
    Point mouseCoords = new Point(0,0);
    Timer timer;
    
    int fps = 24;
    long start = 1;
    long elapsed = 1;

    MyPanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        world = new World();
        
        timer = new Timer(1000 / fps, this);
        
        this.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent me)
            {
                mouseCoords.x = me.getX();
                mouseCoords.y = me.getY();
            }
        });

        this.addMouseListener(new MouseListener() {
            public void mousePressed(MouseEvent me) {
                ParticleDrawer.mode = ParticleDrawer.Mode.values()[((ParticleDrawer.mode.ordinal() + 1) % ParticleDrawer.Mode.values().length)];
            }
            public void mouseReleased(MouseEvent me) { }
            public void mouseEntered(MouseEvent me) { }
            public void mouseExited(MouseEvent me) { }
            public void mouseClicked(MouseEvent me) { }
        });

        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent a) {
        repaint();
        world.run(30);
    }

    public float clip(float a, float min, float max) {
        return Math.min(max, Math.max(min, a));
    }
    @Override
    public void paint(Graphics gr) {
        super.paint(gr);
        Graphics2D g2d = (Graphics2D) gr;
        WorldDrawer.draw(g2d, world);
        g2d.setPaint(Color.WHITE);
        g2d.drawString("fps: " + 1_000_000_000.0 / ((double)elapsed), 30, 10);
        elapsed = System.nanoTime() - start;
        start = System.nanoTime();
    }
}
