package rky.portfolio.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import rky.portfolio.Player;
import rky.portfolio.Player.Players;

public class Server
{
    private final ServerSocket serverSocket;
    private final Map<Player, Client> clients = new HashMap<Player, Client>();

    public Server(int port) 
    {
        try
        {
            serverSocket = new ServerSocket(port);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Unable to bind to port " + port);
        }
    }

    public Players start(int numPlayers)
    {
        Players players = new Players();
        
        try
        {
            for (int i = 0; i < numPlayers; i++)
            {
                Socket p1Socket = serverSocket.accept();
                Client c = new Client(p1Socket);
                Player p = new Player(c.receive());
                clients.put(p, c);
                players.add(p);
                System.out.println(p.name + " connected as player " + i);
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException("Unable to initialize connection with clients.");
        }
        
        return players;
    }
    
    public void send(Player p, String s)
    {
        clients.get(p).send(s);
    }

    public String receive(Player p)
    {
        return clients.get(p).receive();
    }
    
    public void shutdown()
    {
        for (Client c : clients.values())
        {
            try
            {
                c.in.close();
                c.out.close();
            }
            catch (IOException e) { }
        }
    }

    private static class Client
    {
        private final BufferedReader in;
        private final PrintWriter    out;

        public Client(Socket s) throws Exception
        {
            out = new PrintWriter(s.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        }

        public void send(String s)
        {
            try
            {
                out.println(s);
                return;
            }
            catch (Exception e)
            {
                e.printStackTrace(System.err);
            }
            
            throw new RuntimeException("Unable to send message to client.");
        }

        public String receive()
        {
            try
            {
                String message;
                while ((message = in.readLine()) != null)
                {
                    return message;
                }
            }
            catch (IOException e)
            {
                e.printStackTrace(System.err);
            }

            throw new RuntimeException("Unable to receive message from client.");
        }
    }
}
