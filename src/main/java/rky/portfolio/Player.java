package rky.portfolio;

import java.util.HashSet;

import rky.portfolio.io.IoManager;
import rky.portfolio.io.Message;

public class Player
{
    public final String name;
    
    private final IoManager io;
    private int remainingTime = 120 * 1000; 
    private long lastActionStartTime;
    
    public Player(String name, IoManager io)
    {
        this.name = name;
        this.io = io;
    }
    
    public void send(Message m)
    {
        io.send(this, m);
    }
    
    public Message receive()
    {
        return io.receive(this);
    }
    
    public Message prompt(Message m)
    {
        startTimer();
        io.send(this, m);
        Message response = io.receive(this, remainingTime);
        return pauseTimer() ? response : null;
    }
    
    public long remainingTime()
    {
        return remainingTime;
    }
    
    @Override
    public String toString()
    {
        return "Player [name=" + name + ", remainingTime=" + remainingTime + "]";
    }

    private void startTimer()
    {
        lastActionStartTime = System.currentTimeMillis();
    }

    private boolean pauseTimer()
    {
        long elapsed = System.currentTimeMillis() - lastActionStartTime;
        return (remainingTime -= elapsed) >= 0;
    }

    public static class Players extends HashSet<Player>
    {
        private static final long serialVersionUID = 1L;
        
        // Map<Player, Message> sendAll(Map<Player, Message>)
    }
}
