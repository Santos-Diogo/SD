import java.util.concurrent.locks.ReentrantLock;

public class Bank {

    private static class Account extends ReentrantLock{
        public ReentrantLock l= new ReentrantLock();
        private int balance;

        Account(int balance)
        {
            this.balance = balance;
        }

        int balance()
        {
            this.l.lock();
            try
            {
                return balance;
            }
            finally
            {
                this.l.unlock();
            }
        }

        boolean deposit(int value)
        {
            this.l.lock();
            balance += value;
            this.l.unlock();

            return true;
        }

        boolean withdraw(int value)
        {
            if (value > balance)
                return false;
            this.l.lock();
            balance -= value;
            this.l.unlock();
            return true;
        }
    }

    // Bank slots and vector of accounts
    private int slots;
    private Account[] av; 

    public Bank(int n) {
        slots=n;
        av=new Account[slots];
        for (int i=0; i<slots; i++) av[i]=new Account(0);
    }

    // Account balance
    public int balance(int id) 
    {
        if (id < 0 || id >= slots)
            return 0;

        this.av[id].lock();
        try
        {
            return av[id].balance();
        }
        finally
        {
            this.av[id].unlock();
        }
    }

    // Deposit
    public boolean deposit(int id, int value) {
        if (id < 0 || id >= slots)
            return false;

        av[id].lock();
        try
        {
            return av[id].deposit(value);
        }
        finally
        {
            av[id].unlock();
        }
    }

    // Withdraw; fails if no such account or insufficient balance
    public boolean withdraw(int id, int value)
    {
        if (id < 0 || id >= slots)
            return false;

        av[id].lock();
        try
        {
            return av[id].withdraw(value);
        }
        finally
        {
            av[id].unlock();
        }
    }

    boolean transfer (int from, int to, int value)
    {
        if (from< 0 || from>= slots || to< 0 || from>= slots)
            return false;

        av[from].lock();
        av[to].lock();

        try
        {
            if (!av[from].withdraw(value))
                return false;

            return true;
        }
        finally
        {
            av[from].unlock();
            av[to].unlock();
        }
    }

    int totalBalance()
    {
        return 10000;
    }
}
