import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BarrierXPTO
{
    private ReentrantLock l= new ReentrantLock();
    private Condition cond= l.newCondition();
    private boolean open= false;
    private int t_count_current= 0; //Conta Threads do Grupo atual
    private int t_count_next= 0;    //Conta Threads do Grupo seguinte
    private int t_group= 0;         //Grupo ou Etapa da Thread
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
            if (open)
            {
                int my_t_group= t_group++;
                t_count_next++;
                while (my_t_group!= t_group)
                    cond.await();
            }

            else
            {
                t_count_current++;

                if (t_count_current== t_max)
                {
                    cond.signalAll();
                    open= true;
                    t_count--;
                }
                else
                {
                    while (t_count< t_max&& !open)
                        cond.await();
                    t_count_current--;

                    if (t_count_current== 0)
                    {
                        t_count_current= t_count_next;
                        t_count_next= 0;
                        open= false;
                        cond.signalAll();
                        t_group++;
                    }
                }
            }
        }
        finally
        {
            l.unlock();
        }
    }
}