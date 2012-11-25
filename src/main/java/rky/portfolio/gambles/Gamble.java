/**
 * 
 */
package rky.portfolio.gambles;

/**
 * @author stoked
 *
 */
public class Gamble
{
	/*
	 * Probabilities for high, medium, and low returns
	 */
	private final double probh;
	private final double probm;
	private final double probl;
	
	/*
	 * Values for high, medium, and low returns
	 */
	private final double reth;
	private final double retm;
	private final double retl;
	
	
	public Gamble()
	{
		this( 3, 0.3, 2, 0.4, 1 );
	}
	
	public Gamble( double vh, double ph, double vm, double pm, double vl )
	{
		this( vh, ph, vm, pm, vl, 1.0 - ph - pm );
	}
	
	public Gamble( Gamble gamble )
	{
		this( gamble.reth, gamble.probh, gamble.retm, gamble.probm, gamble.retl, gamble.probl );
	}

	public Gamble( Gamble g, Luck luck )
	{
		reth = g.reth;
		retm = g.retm;
		retl = g.retl;

		probm = g.probm;
		
		if( luck == Luck.favorable )
		{
			probl = g.probl / 2;
			probh = g.probh + probl;
		}
		else if( luck == Luck.unfavorable )
		{
			probh = g.probh / 2;
			probl = g.probl + probh;
		}
		else // if( luck == Luck.neutral )
		{
			probh = g.probh;
			probl = g.probl;
		}
	}

	// package accessible because we don't trust the client enough with the probabilities
	Gamble( double vh, double ph, double vm, double pm, double vl, double pl )
	{
		this.reth = vh;
		this.retm = vm;
		this.retl = vl;
		
		this.probh = ph;
		this.probm = pm;
		this.probl = pl;
	}
	
	public double getV( Return r )
	{
		if( r == Return.high   )  return reth;
		if( r == Return.medium )  return retm;
		if( r == Return.low    )  return retl;
		throw new IllegalArgumentException( "Unknown Return instance" );
	}
	
	public double getP( Return r )
	{
		if( r == Return.high   )  return probh;
		if( r == Return.medium )  return probm;
		if( r == Return.low    )  return probl;
		throw new IllegalArgumentException( "Unknown Return instance" );
	}
	
	public String toString()
	{
		return String.format("Gamble[P(%f, %f, %f) V(%f, %f, %f)]", probh, probm, probl, reth, retm, retl);
	}
	
}
