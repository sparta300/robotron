/*
 * Created on Apr 30, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.lbg.persist.pointer;

import java.nio.ByteBuffer;

/**
 * @author smiley
 */
public class IntPointer
{
    public IntPointer(ByteBuffer buffer)
    {
        this.buffer = buffer;
        this.position = buffer.position();

        // we need to advance the position to pretend that we read the value
        buffer.getInt();
    }

    public void set(int value)
    {
        buffer.putInt(position, value);
    }

    public int get()
    {
        return buffer.getInt(position);
    }

    private int position;

    private ByteBuffer buffer;
}