package krio.buffer;

// Not using this, cause iterating through 2d array is O(n^2). Using sort instead.
// iterating through 2d array, returning just values > 0
public class BufferIterator {
    private int posX;
    private int posY;
    private int[][] ref;

    public BufferIterator(int[][] arr) {
        ref = arr;
        posX = 0;
        posY = 0;
    }

    public BufferIterator(int[][] arr, int x, int y) {
        ref = arr;
        posX = x;
        posY = y;
    }

    public int next() {
        for (; posX < ref.length; ++posX) {
            for (; posY < ref[posX].length; ++posY) {
                if (ref[posX][posY] != 0) {
                    return ref[posX][posY++];
                }
            }
            posY = 0;
        }
        return -1; // -1 is end of buffer
    }
}
