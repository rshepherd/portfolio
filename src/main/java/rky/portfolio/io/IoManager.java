package rky.portfolio.io;

import java.util.List;

import rky.portfolio.Player;
import rky.portfolio.Player.Players;

public class IoManager
{
    private final Server          server;
    private List<MessageListener> listeners;

    public IoManager(int port)
    {
        server = new Server(port);
    }

    public Players connect(int numPlayers)
    {
        return server.start(numPlayers, this);
    }

    public void send(Player p, Message m)
    {
        System.out.println("S->" + p.name + ": " + m);
        broadcastToListeners(p, m, true);
        server.send(p, m.toString());
    }

    public Message receive(Player p, int timeout)
    {
        Message m = new Message(server.receive(p, timeout));
        broadcastToListeners(p, m, false);
        System.out.println(p.name + "->S" + ": " + m);
        return m;
    }
    
    public Message receive(Player p)
    {
        Message m = new Message(server.receive(p));
        broadcastToListeners(p, m, false);
        System.out.println(p.name + "->S" + ": " + m);
        return m;
    }

    public void registerListener(MessageListener listener)
    {
        listeners.add(listener);
    }

    private void broadcastToListeners(Player p, Message m, boolean outgoing)
    {
        for (MessageListener l : listeners)
        {
            if (outgoing)
            {
                l.sent(p, m);
            }
            else
            {
                l.received(p, m);
            }
        }
    }

}
