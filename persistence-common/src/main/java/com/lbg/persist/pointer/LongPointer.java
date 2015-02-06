package com.lbg.persist.pointer;

import java.nio.ByteBuffer;

/**
 * @author C006011
 */
public class LongPointer
{    
    private final int position;
    private final ByteBuffer buffer;
    
    public LongPointer(ByteBuffer buffer)
    {
        this.buffer = buffer;
        this.position = buffer.position();
        
        // we need to advance the position to pretend that we read the value
        buffer.getLong();
    }
    
    public void set(long value)
    {
        buffer.putLong(position, value);
    }

    public long get()
    {
        return buffer.getLong(position);
    }
}
