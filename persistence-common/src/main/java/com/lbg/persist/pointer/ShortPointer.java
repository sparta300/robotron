package com.lbg.persist.pointer;

import java.nio.ByteBuffer;

import com.lbg.persist.SafeCast;

/**
 * a pointer to a short, with a value between -32768 and 32767 (inclusive).
 * 
 * @author C006011
 */
public class ShortPointer
{
    private final int position;
    private final ByteBuffer buffer;
    
    public ShortPointer(ByteBuffer buffer)
    {
        this.buffer = buffer;
        this.position = buffer.position();

        // we need to advance the position to pretend that we read the value
        buffer.getShort();
    }

    public void set(int value)
    {
        buffer.putShort(position, SafeCast.fromIntToShort(value));
    }

    public int get()
    {
        return buffer.getShort(position);
    }
}
