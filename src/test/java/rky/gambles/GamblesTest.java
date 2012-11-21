package rky.gambles;

import static org.junit.Assert.*;

import org.junit.Test;

import rky.gambles.Gamble;
import rky.gambles.Gambles;

public class GamblesTest {

	@Test
	public void test() 
	{
//		Gambles.seedRandomGenerator( 1 );
		
		Gamble g;
		
		for( int i = 0; i < 10000; i++ )
		{
			double expectedReturn = Math.random() * 10;
			g = Gambles.randomGamble(expectedReturn);
			double actualExpectedReturn = Gambles.expectedReturn(g);
			if( Math.abs( actualExpectedReturn - expectedReturn ) > 0.000006 )
			{
				fail("Expected Return should be: " + expectedReturn + "but is: " + actualExpectedReturn + "\nfor Gamble: " + g);
			}
			boolean valid = Gambles.isValid(g, true);
			System.out.println( i + " valid:" + valid + " " + g);
			if( !valid )
			{
				fail("Gamble invlaid: " + g);
			}
		}
		
	}

}
