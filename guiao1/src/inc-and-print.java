class Increment implements Runnable{
  public void run() {
    final long I=100;

    for (long i = 0; i < I; i++)
      System.out.println(i);
  }
}

class Main
{
  public static void main (String[] args) throws InterruptedException
  {
    int n= Integer.parseInt(args[0]);

    Thread[] thread_a= new Thread[n];
    for (int i= 0; i< n; i++)
    {
      thread_a[i]= new Thread (new Increment ());
    }
    
    for (int i= 0; i< n; i++)
    {
      thread_a[i].start();
    }
    
    for (int i= 0; i< n; i++)
    {
      thread_a[i].join();
    }

    System.out.println("Acabou pah!");
  }
}