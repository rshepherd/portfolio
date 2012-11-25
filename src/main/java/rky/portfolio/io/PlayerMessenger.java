package rky.portfolio.io;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import rky.portfolio.Player;

public class PlayerMessenger implements Runnable
{

    private final Player  player;
    private final Message outgoing;

    public Message        response;

    public PlayerMessenger(Player p, Message m)
    {
        player = p;
        outgoing = m;
    }

    public void run()
    {
        response = player.prompt(outgoing);
    }
    
    public Player getPlayer()
    {
        return player;
    }

    public Message getResponse()
    {
        return response;
    }

    public static Map<Player, Message> getResponses(Map<Player, Message> playerMessageMap)
    {
        List<PlayerMessenger> pms = new ArrayList<PlayerMessenger>();

        ExecutorService executor = Executors.newFixedThreadPool(playerMessageMap.size());
        for (Entry<Player, Message> e : playerMessageMap.entrySet())
        {
            PlayerMessenger pm = new PlayerMessenger(e.getKey(), e.getValue());
            pms.add(pm);
            executor.execute(pm);
        }

        executor.shutdown();
        while (!executor.isTerminated());
        
        Map<Player, Message> responses = new LinkedHashMap<Player, Message>();
        for (PlayerMessenger pm : pms)
        {
            responses.put(pm.getPlayer(), pm.getResponse());
        }

        return responses;
    }

}
