/**
 * 
 */
package rky.portfolio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import scala.Array;

/**
 * @author stoked
 *
 */
public class ScoreBoard
{
	static class BoardCell
	{

		public BoardCell(double startBudget, double profit)
		{
			this.startBudget = startBudget;
			this.profit = profit;
		}
		
		public double startBudget;
		public double profit;
		public double sharpeRatio;
		
		public String toString()
		{
			return "[startBudget=" + startBudget + ", profit=" + profit + "]";
		}
	}
	
	Set<Player> players = new HashSet<Player>();
	final GameMode mode;
	ArrayList<Map<Player, BoardCell>> budgets;   //array is indexed by turn number
	ArrayList<Double> returns = new ArrayList<Double>();
	
	public enum GameMode { mode1, mode2 };
	
	public ScoreBoard(GameMode mode, int numTurns, Set<Player> players)
	{
		this.mode = mode;
		this.players = players;
		budgets = new ArrayList<Map<Player, BoardCell>>(numTurns+1);
		for( int i = 0; i < numTurns+1; i++ ) {
			budgets.add(new HashMap<Player, BoardCell>());
		}
		for( Player p : players ) {
			budgets.get(0).put(p, new BoardCell(1.0, 0));
		}
	}
	
	public double getFinalScore( Player player )
	{
		if( mode == GameMode.mode1 )
		{
			double mostRecentScore = Double.NEGATIVE_INFINITY;
			for( Map<Player, BoardCell> map : budgets )
			{
				if( map.containsKey(player) )
					mostRecentScore = map.get(player).startBudget;
			}
			return mostRecentScore;
		}
		else
		{
			
			for( Map<Player, BoardCell> map : budgets )
			{
				if( map.containsKey(player) )
					return map.get(player).sharpeRatio;
			}
			
			return 0.0;
		}
	}
	
	public double caculateSharpeRatio(Player player,Double profit)
	{

		returns.add( profit);
		
		double sum = 0.0;
		for(Double ret:returns)
		{
			sum += ret;
		}
		double returnSum = sum;
		if(returnSum == 0)
		{
			return 0;
		}
		
		double mean = sum/returns.size();
		double var = 0.0;
		for(Double ret:returns)
		{
			var += Math.pow(ret-mean, 2);
		}
		var = Math.sqrt(var/returns.size());
		
		return returnSum / var;
	}
	
	public void add( int turnNumber, Player player, double profit )
	{
		BoardCell cell = budgets.get(turnNumber).get(player);
		cell.profit = profit;
		cell.sharpeRatio = caculateSharpeRatio(player,profit);
		budgets.get(turnNumber+1).put(player, new BoardCell(cell.startBudget + profit, 0));
	}

	public double getBudget(int turnNumber, Player player)
	{
		return budgets.get(turnNumber).get(player).startBudget;
	}
	
	public double getStartBudget( int turnNumber, Player player, GameMode mode )
	{
		if( mode == GameMode.mode1 )
			return 1.0;
		
		return getBudget( turnNumber, player );
	}
	
	public GameMode getMode(){
		return this.mode;
	}
	
	public String toString()
	{
		return budgets.toString();
	}
}
