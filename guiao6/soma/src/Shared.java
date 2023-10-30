import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Shared
{
    private int sum= 0;
    private int num= 0;
    ReentrantReadWriteLock l= new ReentrantReadWriteLock();

    public void sum (int n)
    {
        l.writeLock();
        try
        {
            this.sum+= n;
            this.num++;
        }
        finally
        {
            l.writeLock().unlock();
        }
    }

    public int avg ()
    {
        l.readLock();
        try
        {
            return sum/ num;
        }
        finally
        {
            l.readLock().unlock();
        }
    }
}
