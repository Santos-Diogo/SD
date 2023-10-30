import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SomaServer 
{

    public static void main(String[] args) 
    {
        try 
        {
            ServerSocket ss = new ServerSocket(12345);

            //sum of all the input numbers
            int sum= 0;
            //count of the input numbers
            int count= 0;


            while (true) 
            {
                Socket socket = ss.accept();

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream());

                String line;
                while ((line = in.readLine()) != null) 
                {
                    sum+= Integer.parseInt(line);
                    count++;

                    out.println(sum);
                    out.flush();
                }
                
                //termination message
                out.println(sum/ count);
                out.flush();
                
                socket.shutdownInput();
                socket.shutdownOutput();
                socket.close();
            }
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
}
