import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

class Bank {

    private static class Account 
    {
        private int balance;
        ReentrantLock l= new ReentrantLock();

        Account(int balance)
        {
            this.balance = balance;
        }

        int balance()
        {
            return balance;
        }

        boolean deposit(int value) 
        {
            balance += value;
            return true;
        }

        boolean withdraw(int value) {
            if (value > balance)
                return false;
            balance -= value;
            return true;
        }
    }

    public ReentrantLock GIGALOCK= new ReentrantLock();
    private Map<Integer, Account> map = new HashMap<Integer, Account>();
    private int nextId = 0;

    // create account and return account id
    public int createAccount(int balance)
    {
        Account c = new Account(balance);
        GIGALOCK.lock();
        try
        {
            map.put(nextId, c);
            return nextId;
        }
        finally
        {
            nextId ++;
            GIGALOCK.unlock();
        }
    }

    // close account and return balance, or 0 if no such account
    public int closeAccount(int id)
    {
        Account c;
        GIGALOCK.lock();
        try
        {
            c = map.remove(id);
            if (c == null)
                return 0;
            c.l.lock();
        }
        finally
        {
            GIGALOCK.unlock();
        }
        
        try
        {
            return c.balance();
        }
        finally
        {
            c.l.unlock();
        }
    }

    // account balance; 0 if no such account
    public int balance(int id) {
        Account c = map.get(id);
        if (c == null)
            return 0;
        return c.balance();
    }

    // deposit; fails if no such account
    public boolean deposit(int id, int value) {
        Account c = map.get(id);
        if (c == null)
            return false;
        return c.deposit(value);
    }

    // withdraw; fails if no such account or insufficient balance
    public boolean withdraw(int id, int value) {
        Account c = map.get(id);
        if (c == null)
            return false;
        return c.withdraw(value);
    }

    // transfer value between accounts;
    // fails if either account does not exist or insufficient balance
    public boolean transfer(int from, int to, int value) {
        Account cfrom, cto;
        cfrom = map.get(from);
        cto = map.get(to);
        if (cfrom == null || cto ==  null)
            return false;
        return cfrom.withdraw(value) && cto.deposit(value);
    }

    // sum of balances in set of accounts; 0 if some does not exist
    public int totalBalance(int[] ids) {
        int total = 0;
        for (int i : ids) {
            Account c = map.get(i);
            if (c == null)
                return 0;
            total += c.balance();
        }
        return total;
  }

}