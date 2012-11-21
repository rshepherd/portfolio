package rky.portfolio.io;

import rky.portfolio.Player;

/**
 * Kunal, implement this interface on whatever gui class you wish 
 * to be notified of every incoming and outgoing message.
 */

public interface MessageListener
{
    public void received(Player p, Message m);
    
    public void sent(Player p, Message m);
}
