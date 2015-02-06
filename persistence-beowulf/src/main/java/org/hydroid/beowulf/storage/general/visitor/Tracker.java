package org.hydroid.beowulf.storage.general.visitor;


public class Tracker
{
    public Tracker(int slotSize, int byteCount) {
        this(slotSize, byteCount, byteCount, byteCount);
    }
    
    public Tracker(int slotSize, int byteCount, int totalRawSize, int totalStoredSize) {
        this.slotSize = slotSize;
        this.totalRawSize = totalRawSize;
        this.totalStoredSize = totalStoredSize;
        remaining = byteCount;
    }
    
    /**
     * move on to the next chunk
     */
    public void move() {
        if (remaining < slotSize) {
            throw new RuntimeException("no data remaining");
        }
        
        remaining -= slotSize;
        offset += slotSize;
    }
    
    public int getChunkSize() {       
        return (remaining >= slotSize) ? slotSize : remaining;
    }
    
    public int getOffset() {
        return offset;
    }
    
    public int getTotalRawSize() {
        return totalRawSize;
    }

    public int getTotalStoredSize() {
        return totalStoredSize;
    }    
    
    public int getRemaining() {
        return remaining;
    }
    
    public String toString() {
        return "o=" + offset + " r=" + remaining + " s=" + slotSize;
    }
    
    private int offset;
    private int remaining;
    private int slotSize;
    private int totalRawSize;
    private int totalStoredSize;

}
