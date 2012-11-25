package rky.portfolio;

import java.util.HashSet;

public class Player
{
    public final String name;
    
    // TODO - Break this out into a time keeper class
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

    public static class Players extends HashSet<Player>
    {
        private static final long serialVersionUID = 1L;
    }
}
