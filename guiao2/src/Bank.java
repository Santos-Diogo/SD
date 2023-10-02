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
        av[from].lock();   
        try
        {
            if (withdraw(from, value))
            return false;
        }
        finally
        {
            av[from].unlock();
        }
        
        av[to].lock();
        try
        {
            deposit(to, value);
            return true;
        }
        finally
        {
            av[to].unlock();
        }
    }

    int totalBalance()
    {
        int sum= 0;
        for (int i= 0; i< slots; i++)
        {
            sum+= balance(i);
        }

        return sum;
    }
}
