package com.lbg.persist.pointer;

import java.nio.ByteBuffer;

import com.lbg.persist.SafeCast;

/**
 * a pointer to a byte in a buffer, with values between -128 and 127 (inclusive).
 * 
 * @author C006011
 */
public class BytePointer
{
    private final int position;
    private final ByteBuffer buffer;
    
    public BytePointer(ByteBuffer buffer)
    {
        this.buffer = buffer;
        this.position = buffer.position();

        // we need to advance the position to pretend that we read the value
        buffer.get();
    }

    public void set(int value)
    {
        buffer.put(position, SafeCast.fromIntToByte(value));
    }

    public int get()
    {
        return buffer.get(position);
    }
}
