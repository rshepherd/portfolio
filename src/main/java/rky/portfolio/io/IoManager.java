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

	public Players start(int numPlayers)
	{
		players = server.start(numPlayers);
		//server.send(players.matchmaker, "M " + n );
		return players;
	}

	/**
	 * Will shutdown the game, sending the message to both players
	 */
	public void shutdown(Message m)
	{
		//server.send(players.matchmaker, m.toString());
		//server.send(players.person, m.toString() );
	}

	public void send(Player p, Message m)
	{
		System.err.println("S->" + p + ": " + m);
		server.send(p, m.toString());
	}

	public Message receive(Player p)
	{
		String msg = server.receive(p);
		System.err.println("S<-" + p + ": " + msg);
		return new Message(msg);
	}

	/**
	 * A convenience method for a user prompt and reply
	 * 
	 * @param p The player to which the message is to be sent
	 * @param m The prompt to the player ex. WEIGHTS
	 * @return The reply from the player.
	 */
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
