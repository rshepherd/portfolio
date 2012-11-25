package rky.portfolio.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class DumbClient
{
    private static Socket         socket;
    private static PrintWriter    out;
    private static BufferedReader in;

    public static void main(String[] args)
    {
        initSocket(Integer.parseInt(args[0]));
        send("team-"+System.currentTimeMillis()); 
        receive(); //discard game init message
        while (true)
        {
            if (receive().contains("GAMEOVER"))
            {
                break;
            }
            send(makeDistribution());
            if (receive().contains("ERROR"))
            {
                break;
            }
            receive(); // Discard return info
        }
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
    
    private static String makeDistribution()
    {
        Map<Integer, Double> distribMap = new HashMap<Integer, Double>();

        double distribute = Math.random();
        for (int i = 0; i < 190; ++i)
        {
            if (Math.random() > 0.7)
            {
                double val = distribute * Math.random();
                distribute -= val;
                distribMap.put(i, val);
            }
        }

        StringBuilder gambleReturnsStringBuilder = new StringBuilder("[");
        for (Integer gambleId : distribMap.keySet())
        {
            gambleReturnsStringBuilder.append(gambleId);
            gambleReturnsStringBuilder.append(":");
            gambleReturnsStringBuilder.append(distribMap.get(gambleId));
            gambleReturnsStringBuilder.append(", ");
        }
        gambleReturnsStringBuilder.setLength(gambleReturnsStringBuilder.length() - 2);
        gambleReturnsStringBuilder.append("]");

        return gambleReturnsStringBuilder.toString();
    }
    
}
