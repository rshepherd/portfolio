package rky.portfolio;

import java.util.ArrayList;

public class Player
{

    public final String name;
    
    // Time-tracking properties, each player gets 2 mins
    private long remainingTime = 120 * 1000; 
    private long lastActionStartTime;
    
    public Player(String name)
    {
        this.name = name;
    }
    
    public void startTimer()
    {
        lastActionStartTime = System.currentTimeMillis();
    }

    public boolean pauseTimer()
    {
        long elapsed = System.currentTimeMillis() - lastActionStartTime;
        return (remainingTime -= elapsed) >= 0;
    }
    
    @Override
    public String toString()
    {
        return "Player [name=" + name + "]";
    }

    public static class Players extends ArrayList<Player>
    {
        private static final long serialVersionUID = 1L;
    }
}
