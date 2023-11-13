package g8;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

public class FramedConnection implements AutoCloseable 
{
    private ReentrantLock rl= new ReentrantLock();
    private ReentrantLock wl= new ReentrantLock();
    private InputStream in;
    private OutputStream out;

    public FramedConnection(Socket socket) throws IOException
    {
        in= socket.getInputStream();
        out= socket.getOutputStream();
    }

    public void send(byte[] data) throws IOException
    {
        this.wl.lock();
        try
        {
            this.out.s.writeInt(data.length);
            this.out.s.write(data);
        }
        finally
        {
            this.wl.unlock();
        }
    }

    public byte[] receive() throws IOException
    {
        this.rl.lock();
        try
        {
            int len= this.in.s.readInt();
            return this.in.s.readFully(len);
        }
        finally
        {
            this.rl.unlock();
        }
    }

    public void close() throws IOException
    {
        this.wl.lock();
        this.rl.lock();
        try
        {
            this.in.close();
            this.out.flush();            
            this.out.close();
        }
        finally
        {
            this.wl.unlock();
            this.rl.unlock();
        }    
    }
}