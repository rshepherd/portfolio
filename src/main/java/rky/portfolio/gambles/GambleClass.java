/**
 * 
 */
package rky.portfolio.gambles;

import java.util.HashSet;

/**
 * @author stoked
 *
 */
public class GambleClass extends HashSet<Gamble>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4083778714579887226L;
	
	Luck luck;
	
	
	
	public Luck getLuck()
	{
		return luck;
	}

	public void setLuck(Luck luck)
	{
		this.luck = luck;
	}
}
