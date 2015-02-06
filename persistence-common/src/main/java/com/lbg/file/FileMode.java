package com.lbg.file;

import java.nio.channels.FileChannel;

/**
 * @author smiley
 */
public class FileMode
{
    private FileMode(String symbol, FileChannel.MapMode mode)
    {
        this.symbol = symbol;
        this.mode = mode; 
    }
    
    public static FileMode toFileMode(String s)
    {
        if (s.equals("r"))
        {
            return FileMode.READ_ONLY;
        } 
        else if (s.equals("rw"))
        {
            return FileMode.READ_WRITE;
        }
        
        else
        {
            throw new IllegalArgumentException("expecting 'r' or 'rw'");
        }
    }
    
    public static FileChannel.MapMode toMapMode(String s)
    {
        return toFileMode(s).getMapMode();
    }
    
    public String toString()
    {
        return this.symbol;
    }
    
    public FileChannel.MapMode getMapMode()
    {
        return mode;
    }
    
    public static final FileMode READ_ONLY = new FileMode("r", FileChannel.MapMode.READ_ONLY);
    public static final FileMode READ_WRITE = new FileMode("rw", FileChannel.MapMode.READ_WRITE);
    
    private FileChannel.MapMode mode;
    private String symbol;
}
