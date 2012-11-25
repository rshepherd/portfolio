package rky.portfolio.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client
{
    private static Socket         socket;
    private static PrintWriter    out;
    private static BufferedReader in;

    public static void main(String[] args)
    {
        initSocket(Integer.parseInt(args[0]));
        send("team-"+System.currentTimeMillis()); 
        receive(); //discard game init message
        
    }
    
    private static void send(String message)
    {
        System.out.println("sent: " + message);
        out.println(message);
    }
    
    private static String receive()
    {
        try
        {
            String message;
            while ((message = in.readLine()) != null)
            {
                if (message.trim().isEmpty())
                {
                    continue;
                }
                System.out.println("received: " + message);
                return message;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace(System.err);
            System.exit(1);
        }
        
        throw new RuntimeException("Problem receiving input from server.");
    }

    private static void initSocket(Integer port)
    {
        try
        {
            socket = new Socket("localhost", port);
            socket.setTcpNoDelay(true);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        catch (IOException e)
        {
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }

}
