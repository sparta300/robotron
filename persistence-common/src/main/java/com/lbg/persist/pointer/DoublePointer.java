package com.lbg.persist.pointer;

import java.nio.ByteBuffer;

/**
 * @author C006011
 */
public class DoublePointer
{    
    private final int position;
    private final ByteBuffer buffer;
    
    public DoublePointer(ByteBuffer buffer)
    {
        this.buffer = buffer;
        this.position = buffer.position();
        
        // we need to advance the position to pretend that we read the value
        buffer.getDouble();
    }
    
    public void set(double value)
    {
        buffer.putDouble(position, value);
    }

    public double get()
    {
        return buffer.getDouble(position);
    }
}
