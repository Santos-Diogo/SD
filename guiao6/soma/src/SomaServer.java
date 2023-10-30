import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SomaServer 
{
    private static ServerSocket ss;
    private static Shared shared= new Shared ();

    public static void main(String[] args) 
    {
        try 
        {
            ss = new ServerSocket(12345);

            while (true) 
            {
                Socket socket = ss.accept();
                new Thread(new ServerThread(socket, shared)).start();;
            }
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                ss.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
