package g8;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.InputMap;

public class FrammedConnection implements AutoCloseable 
{
    public static class Frame 
    {
        public final int tag;
        public final byte[] data;

        public Frame (int tag, byte[] data) 
        {
            this.tag = tag;
            this.data = data;
        }
    }

    private ReentrantLock r_l;
    private DataInputStream in;
    private ReentrantLock w_l;
    private DataOutputStream out;

    public TaggedConnection(Socket socket) throws IOException 
    {
        this.r_l= new ReentrantLock();
        this.in= socket.getDataInputStream();
        this.w_l= new ReentrantLock();
        this.out= socket.getDataOutputStream();
    }
    
    public void send(Frame frame) throws IOException 
    {
        try
        {
            w_l.lock();
            out.write(frame);
        }
        finally
        {
            w_l.unlock();
        }
    }

    public void send(int tag, byte[] data) throws IOException 
    {
        try
        {
            w_l.lock();
            out.writeInt(tag);
            out.writeInt(data.length);
            out.write(data);
            out.flush();
        }
        finally
        {
            w_l.unlock();
        }
    }

    public Frame receive() throws IOException 
    {
        try
        {
            r_l.lock();
            int tag= in.readInt();
            byte[] data= in.readNBytes(in.readInt());
            return new Frame (tag, data);
        }
        finally
        {
            r_l.unlock();
        }
    }

    public void close() throws IOException 
    {
        try
        {
            w_l.lock();
            r_l.lock();
            in.close();
            out.close();
        }
        finally
        {
            w_l.unlock();
            r_l.unlock();
        }
    }
}