package krio.buffer;

import krio.buffer.PositionBuffer;

import java.util.HashSet;

public class GetNeighbours extends PositionBuffer {
    
    // public ================================================
    public GetNeighbours(int screenW, int screenH, int buffW, int buffH) {
        super(screenW, screenH, buffW, buffH);
    }
    
    public HashSet<Integer> getNeighbours(int px, int py, int dist) {  // px, py - buffer-sized coords
        HashSet<Integer> neighbours = new HashSet<>();
        int[][] buff = getBuffer();
        
        for(int x = clip((px - dist), 0, BUFFER_WIDTH); x < clip((px + dist), 0, BUFFER_WIDTH); ++x){
            for(int y = clip((py - dist), 0, BUFFER_HEIGHT); y < clip((py + dist), 0, BUFFER_HEIGHT); ++y){
                if (buff[x][y] != 0 && (x != px || y != py))  {
                    neighbours.add(buff[x][y]);
                    //System.out.println("+");
                }
            }
        }
        return neighbours;
    }
    
    
    // private ================================================
    private <T extends Number> T clip(T a, T min, T max) {   // just for shortening
        return a.doubleValue() > min.doubleValue() ? (a.doubleValue() < max.doubleValue() ? a : max) : min;
    }
}
