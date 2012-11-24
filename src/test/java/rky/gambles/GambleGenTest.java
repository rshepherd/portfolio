package rky.gambles;

import java.util.ArrayList;

import org.junit.Test;

import rky.portfolio.gambles.Gamble;
import rky.portfolio.gambles.Gambles;
import rky.portfolio.gambles.Return;

public class GambleGenTest
{
	static final double AVERAGE_EXPECTED = 2.0;
	
	@Test
	public void generateGamblesTest()
	{
		final int numGambles = (int)(Math.random() * 20) + 190;
		
		ArrayList<Double> gambleExpectedReturns = new ArrayList<Double>(numGambles);
		
		double sum = 0;
		for( int i = 0; i < numGambles; i++ )
		{
			double randomValue = Math.random();
			gambleExpectedReturns.add( randomValue );
			sum += randomValue;
		}
		
		for( int i = 0; i < numGambles; i++ )
		{
			gambleExpectedReturns.set( i, gambleExpectedReturns.get(i) * AVERAGE_EXPECTED / sum );
		}
		
		ArrayList<Gamble> gambles = new ArrayList<Gamble>(numGambles);
		
		for( int i = 0; i < numGambles; i++ )
		{
			gambles.add( Gambles.randomGamble(gambleExpectedReturns.get(i)) );
		}
		
		System.out.println("# gamble_id, class_id, h_ret, h_prob, m_ret, m_prob, l_ret, l_prob");
		
		for( int i = 0; i < numGambles; i++ )
		{
			Gamble g = gambles.get(i);
			System.out.println(String.format(
					"%d, %d, %f, %f, %f, %f, %f",
					i, (int)(Math.random() * 16),
					g.getV(Return.high  ), g.getP(Return.high),
					g.getV(Return.medium), g.getP(Return.medium),
					g.getV(Return.low   ), g.getP(Return.low)
					));
		}
	}
}
