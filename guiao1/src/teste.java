class C extends Thread
{
    @Override
    public void run ()
    {
        System.out.println("Na Thread");
    }
}

class Main
{
    public static void main (String[] args)
    {
        C c= new C();
        c.start();
        System.out.println("Na main");
    }
}