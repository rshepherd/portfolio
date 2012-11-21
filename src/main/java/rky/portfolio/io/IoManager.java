package rky.portfolio.io;

import rky.portfolio.Player;
import rky.portfolio.Player.Players;

public class IoManager
{
	private final Server server;
	private Players players;

	public IoManager(int port)
	{
		server = new Server(port);
	}

	public Players connect(int numPlayers)
	{
	    return server.start(numPlayers);
	}

	public void send(Player p, Message m)
	{
		System.out.println("S->" + p.name + ": " + m);
		server.send(p, m.toString());
	}

	public Message receive(Player p)
	{
		String msg = server.receive(p);
		System.err.println("S<-" + p.name + ": " + msg);
		return new Message(msg);
	}

	public Message prompt(Player p, Message m)
	{
		server.send(p, m.toString());
		return new Message(server.receive(p));
	}

	public Players getPlayers()
	{
		return players;
	}

}
