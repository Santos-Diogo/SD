import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread implements Runnable
{
    private Socket socket;
    private Shared shared;

    ServerThread (Socket socket, Shared shared)
    {
        this.socket= socket;
        this.shared= shared;
    }

    public void run ()
    {
        try
        {

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            
            //sum of this client's input numbers
            int sum= 0;
            //count of this clinet's input numbers
            int count= 0;
            int n;

            String line;
            while ((line = in.readLine()) != null) 
            {
                n= Integer.parseInt(line);
                shared.sum(n);
                sum+= n;
                count++;
                
                out.println(sum);
                out.flush();
            }
            
            //termination message
            out.println(shared.avg());
            out.flush();
            
            socket.shutdownInput();
            socket.shutdownOutput();
            socket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}    