/**
 * 
 */
package rky.gambles;

import java.util.Random;

/**
 * @author stoked
 *
 */
public class Gambles
{
	public static final double MIN_MEDIUM_PROB = 0.4;
	
	/**
	 * @param expectedReturn the value of the expected return on the gamble: prob_h * ret_h + prob_m * ret_m + prob_l * ret_l
	 * @return a random gamble with the specified expected return value and with P(medium return) >= MIN_MEDIUM_PROB
	 */
	public static Gamble randomGamble( double expectedReturn )
	{
		return randomGamble( expectedReturn, true );
	}
	
	/**
	 * @param expectedReturn the value of the expected return on the gamble: prob_h * ret_h + prob_m * ret_m + prob_l * ret_l
	 * @param subjectToMediumRestriction whether the gamble is subject to the restriction that P(medium return) >= MIN_MEDIUM_PROB
	 * @return a random gamble with the specified expected return
	 */
	public static Gamble randomGamble( double expectedReturn, boolean subjectToMediumRestriction )
	{
		double probh = random.nextDouble();
		double probm = random.nextDouble();
		double probl = random.nextDouble();
		
		double sum = probh + probm + probl;
		probh /= sum;
		probm /= sum;
		probl /= sum;
		
		if( subjectToMediumRestriction )
		{
			probh *= (1.0 - MIN_MEDIUM_PROB);
			probl *= (1.0 - MIN_MEDIUM_PROB);
			probm = MIN_MEDIUM_PROB + probm * (1.0 - MIN_MEDIUM_PROB);
		}
		
		double retl = expectedReturn * random.nextDouble();
		double remainsForMediumAndHigh = expectedReturn - retl * probl;
		double expectedDensityForMediumAndHigh = remainsForMediumAndHigh / (probm + probh);
		double retm = (expectedDensityForMediumAndHigh - retl) * random.nextDouble() + retl;
		double remainForHigh = remainsForMediumAndHigh - retm * probm;
		double expectedDensityForHigh = remainForHigh / probh;
		double reth = expectedDensityForHigh;
		
		return new Gamble( reth, probh, retm, probm, retl, probl );
	}
	
	public static double expectedReturn( Gamble g )
	{
		return  g.getV(Return.high)   * g.getP(Return.high) + 
				g.getV(Return.medium) * g.getP(Return.medium) +
				g.getV(Return.low)    * g.getP(Return.low);
	}
	
	/**
	 * Equivalent to calling isValid( g, true )
	 * @param g the Gamble to be validated
	 * @return true if g passes validation including the MIN_MEDIUM_PROB on P(medium return)
	 */
	public static boolean isValid( Gamble g )
	{
		return isValid(g, true);
	}
	
	/**
	 * @param g the Gamble to be validated
	 * @param subjectToMediumRestriction if true, checks whether P(medium return) satisfies MIN_MEDIUM_PROB
	 * @return
	 */
	public static boolean isValid( Gamble g, boolean subjectToMediumRestriction )
	{
		boolean ret = true;
		if( !(g.getV(Return.high) >= g.getV(Return.medium) && g.getV(Return.medium) >= g.getV(Return.low)) )
			ret = false;
		
		if( subjectToMediumRestriction && g.getP(Return.medium) < MIN_MEDIUM_PROB )
			ret = false;
		
		return ret;
	}
	
	
	
	public static void seedRandomGenerator( long seed )
	{
		random.setSeed(seed);
	}
	
	private static final Random random = new Random();
}
