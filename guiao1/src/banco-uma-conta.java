class Bank {

  private static class Account {
    private long balance;
    Account(long balance) { this.balance = balance; }
    long balance() { return balance; }
    boolean deposit(long value) {
      balance += value;
      return true;
    }
  }

  // Our single account, for now
  private Account savings = new Account(0);

  // Account balance
  public long balance() {
    return savings.balance();
  }

  // Deposit
  boolean deposit(long value) {
    return savings.deposit(value);
  }
}

class Depositos implements Runnable{
  private long n_depositos;
  private long valor;
  private Bank b;

  public Depositos (long n_depositos, long valor, Bank b)
  {
    this.n_depositos= n_depositos;
    this.valor= valor;
    this.b= b;
  }

  public void run()
  {
    for (long i = 0; i < this.n_depositos; i++)
      b.deposit(this.valor); 
  }
}

class Main
{
  public static void main (String[] args) throws InterruptedException
  {
    Bank b= new Bank();
    Thread t[]= new Thread[10];

    for (int i= 0; i< 10; i++)
    {
      t[i]= new Thread(new Depositos(1000, 100, b));
    }

    for (int i= 0; i< 10; i++)
    {
      t[i].start();
    }

    for (int i= 0; i< 10; i++)
    {
      t[i].join();
    }

    System.out.println(b.balance());
  }
}