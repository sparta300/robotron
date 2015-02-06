package com.lbg.persist.pointer;

import java.nio.ByteBuffer;

import com.lbg.persist.SafeCast;

/**
 * a pointer to an unsigned short, with values between 0 and 65535.
 * 
 * @author C006011
 */
public class UnsignedShortPointer
{
    private final int position;
    private final ByteBuffer buffer;
    
    public UnsignedShortPointer(ByteBuffer buffer)
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
