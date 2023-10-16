import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class Barrier 
{
    private ReentrantLock l= new ReentrantLock();
    private Condition cond= l.newCondition();
    private boolean open= false;
    private int t_count= 0;
    private final int t_max;

    public Barrier (int N) 
    {
        this.t_max= N;
    }

    void await() throws InterruptedException 
    {
        l.lock();
        try
        {
            while (open)
                cond.await();

            t_count++;    
            if (t_count== t_max)
            {
                cond.signalAll();
                open= true;
                t_count--;
            }
            else
            {
                while (t_count< t_max&& !open)
                cond.await();
                t_count--;
                if (t_count== 0)
                {
                    open= false;
                    cond.signalAll();
                }
            }
        }
        finally
        {
            l.unlock();
        }
    }
}