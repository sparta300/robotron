package com.lbg.persist;


import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.GatheringByteChannel;


/**
 * helps with some new i/o file management.
 * 
 * @author smiley
 */
public class NioHelper
{
    static public long createFile(ByteBuffer[] buffers, String fileName, String fileMode) 
    throws IOException
    {
        return createFile(buffers, new RandomAccessFile(fileName, fileMode));
    }
    
    static public long createFile(ByteBuffer bb, String fileName, String fileMode) 
    throws IOException
    {
        return createFile(bb, new RandomAccessFile(fileName, fileMode));
    }
    
    static public long createFile(ByteBuffer bb, File file, String fileMode) 
    throws IOException
    {
        return createFile(bb, new RandomAccessFile(file, fileMode));
    }    
  
    static public long createFile(ByteBuffer[] buffers, RandomAccessFile rafile)
    throws IOException
    {
        GatheringByteChannel writeChannel = rafile.getChannel();
        
        long byteCount = writeChannel.write(buffers);
        writeChannel.close();
        rafile.close();        
        return byteCount;
    }
    
    static public long createFile(ByteBuffer bb, RandomAccessFile rafile)
    throws IOException
    {
        ByteChannel writeChannel = rafile.getChannel();
        
        int byteCount = writeChannel.write(bb);
        writeChannel.close();
        rafile.close();        
        return byteCount;
    }


    /**
     * @param bb
     * @param fileName
     */
    public static int readFile(ByteBuffer bb, String fileName)
    throws IOException
    {
        return readFile(bb, new RandomAccessFile(fileName, "r"));        
    }

    public static int readFile(ByteBuffer bb, File file)
    throws IOException
    {
        return readFile(bb, new RandomAccessFile(file, "r"));        
    }
    
    public static int readFile(ByteBuffer bb, RandomAccessFile rafile)
    throws IOException
    {
        ByteChannel channel = rafile.getChannel();
        int byteCount = channel.read(bb);
        return byteCount;
    }    
    
    public static String toString(ByteBuffer bb)
    {
        return toString("", bb);
    }
    
    public static String toString(String description, ByteBuffer bb)
    {
        StringBuffer buf = new StringBuffer();
        buf.append(description).append(": type=");
        boolean needsSpace = false;
        
        if (bb.isDirect())
        {
            buf.append("D");
            needsSpace = true;
        }
        
        if (bb.isReadOnly())
        {
            buf.append("R");
            needsSpace = true;
        }
        
        if (needsSpace)
        {
            buf.append(" ");
        }
        
        buf.append("position=").append(bb.position());
        buf.append(" limit=").append(bb.limit());
        buf.append(" capacity=").append(bb.capacity());        
        
        return buf.toString();
    }
}
