package krio.buffer;

import krio.world.Particle;

import java.awt.Point;

public class PositionBuffer {
    protected int BUFFER_HEIGHT;
    protected int BUFFER_WIDTH;
    
    private int SCREEN_HEIGHT;
    private int SCREEN_WIDTH;
    
    private double resizeW;
    private double resizeH;
    
    private int[][] buffer;

    PositionBuffer(int screenW, int screenH, int buffW, int buffH) {
        SCREEN_WIDTH  = screenW;
        SCREEN_HEIGHT = screenH;
        BUFFER_WIDTH  = buffW;
        BUFFER_HEIGHT = buffH;
        
        resizeW = (double) BUFFER_WIDTH / (double) SCREEN_WIDTH;
        resizeH = (double) BUFFER_HEIGHT / (double) SCREEN_HEIGHT;
        
        buffer = new int[BUFFER_WIDTH][BUFFER_HEIGHT];
    }
    
    public void reset() {
        resizeW = (double) BUFFER_WIDTH / (double) SCREEN_WIDTH;
        resizeH = (double) BUFFER_HEIGHT / (double) SCREEN_HEIGHT;

        buffer = new int[BUFFER_WIDTH][BUFFER_HEIGHT];
    }
    
    public Point add(Particle p) {
        if ((int)p.pos.x > (SCREEN_WIDTH - 1) || (int)p.pos.x < 0) return null;
        if ((int)p.pos.y > (SCREEN_HEIGHT - 1) || (int)p.pos.y < 0) return null;
        
        Point buffPos = getApproxPosition(p.pos.x, p.pos.y);
        buffer[buffPos.x][buffPos.y] = p.id;

        return buffPos;
    }
    
    public int[][] getBuffer() {
        return buffer;
    }
    
    public Point getApproxPosition(double x, double y) {
        Point result = new Point();
        
        result.x = Math.min(Math.max((int)(x * resizeW), 0), BUFFER_WIDTH  - 1);
        result.y = Math.min(Math.max((int)(y * resizeH), 0), BUFFER_HEIGHT - 1);
        
        return result;
    }
    
    public int getId(int x, int y) {
        return buffer[x][y];
    }
}
