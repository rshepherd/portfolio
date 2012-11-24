package rky.portfolio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rky.portfolio.gambles.Gamble;
import rky.portfolio.gambles.Gambles;
import rky.portfolio.gambles.Luck;
import rky.portfolio.gambles.Return;
import rky.util.SetMap;

public class GameLoop
{
	Map<Integer, Gamble>   gambles       = new HashMap<Integer, Gamble>();
	Map<Gamble, Integer>   ids           = new HashMap<Gamble, Integer>();
	SetMap<Gamble, Gamble> links         = new SetMap<Gamble, Gamble>();
	Map<Integer, Luck>     classes       = new HashMap<Integer, Luck>();
	Map<Gamble, Integer>   gambleClasses = new HashMap<Gamble, Integer>();
	
	public Map<Integer, Gamble> getGambles() {
		return gambles;
	}

	public void setGambles(Map<Integer, Gamble> gambles) {
		this.gambles = gambles;
	}

	public Map<Gamble, Integer> getIds() {
		return ids;
	}

	public void setIds(Map<Gamble, Integer> ids) {
		this.ids = ids;
	}

	public SetMap<Gamble, Gamble> getLinks() {
		return links;
	}

	public void setLinks(SetMap<Gamble, Gamble> links) {
		this.links = links;
	}

	public Map<Integer, Luck> getClasses() {
		return classes;
	}

	public void setClasses(Map<Integer, Luck> classes) {
		this.classes = classes;
	}

	public Map<Gamble, Integer> getGambleClasses() {
		return gambleClasses;
	}

	public void setGambleClasses(Map<Gamble, Integer> gambleClasses) {
		this.gambleClasses = gambleClasses;
	}
	
	/**
	 * @param gamblesInOrder order in which to play the gambles
	 * @return a mapping of each gamble to its return
	 */
	public Map<Gamble, Return> playGambles(List<Gamble> gamblesInOrder)
	{
		Map<Gamble, Return> played = new HashMap<Gamble, Return>();
		
		for( Gamble g : gamblesInOrder )
		{
			Luck classLuck = classes.get( gambleClasses.get(g) );
			Return ret = Gambles.playGamble( g, classLuck, played, links.get(g) );
			played.put( g, ret );
		}
		
		return played;
	}
	
	/**
	 * Plays gambles in random order.
	 * @return a mapping of each gamble to its return
	 */
	public Map<Gamble, Return> playGambles()
	{
		ArrayList<Gamble> gambleOrder = new ArrayList<Gamble>(ids.keySet());
		Collections.shuffle(gambleOrder);
		return playGambles(gambleOrder);
	}
	
}
