package com.lbg.persist.pointer;

import java.nio.ByteBuffer;

/**
 * a pointer to an unsigned byte, with a value between 0 and 255.
 * 
 * @author C006011
 */
public class UnsignedBytePointer
{
    private final int position;
    private final ByteBuffer buffer;
    
    public UnsignedBytePointer(ByteBuffer buffer)
    {
        this.buffer = buffer;
        this.position = buffer.position();

        // we need to advance the position to pretend that we read the value
        buffer.get();
    }

    public void set(int value)
    {
        buffer.put(position, (byte)(value & 0xff));
    }

    public int get()
    {
        return buffer.get(position) & (int)0xff;
    }
}
