package rky.gambles;

import static org.junit.Assert.*;

import org.junit.Test;

import rky.portfolio.gambles.Gamble;
import rky.portfolio.gambles.Gambles;
import rky.portfolio.gambles.Return;

public class GamblesTest {

	@Test
	public void testRandomGen() 
	{
		//Gambles.seedRandomGenerator( 1 );
		boolean VERBOSE = false;
		
		Gamble g;
		
		for( int i = 0; i < 1000; i++ )
		{
			double expectedReturn = Math.random() * 10;
			g = Gambles.randomGamble(expectedReturn);
			double actualExpectedReturn = Gambles.expectedReturn(g);
			if( Math.abs( actualExpectedReturn - expectedReturn ) > 0.000006 )
			{
				fail("Expected Return should be: " + expectedReturn + "but is: " + actualExpectedReturn + "\nfor Gamble: " + g);
			}
			boolean valid = Gambles.isValid(g, true);
			
			if( VERBOSE ) System.out.println( i + " valid:" + valid + " " + g);
			
			if( !valid )
			{
				fail("Gamble invlaid: " + g);
			}
		}
		
	}
	
	
	
	@Test
	public void testPlayGambles()
	{
		Gambles.seedRandomGenerator( 1 );
		
		for( int k = 0; k < 10; k++ )
		{
			
			Gamble g = Gambles.randomGamble( Math.random() * 10 );
			System.out.println( g + " expeted: " + Gambles.expectedReturn(g) );
			
			int Hi = 0;
			int Mi = 0;
			int Li = 0;
			for( int i = 0; i < 100000; i++ )
			{
				Return gReturn = Gambles.playGamble(g);
				
				if( gReturn == Return.high   )  ++Hi;
				if( gReturn == Return.medium )  ++Mi;
				if( gReturn == Return.low    )  ++Li;
			}
			int sumi = Hi + Mi + Li;
			
			System.out.println( String.format( "Played counts: P(H)=%5f P(M)=%5f P(L)=%5f", (double)Hi/sumi, (double)Mi/sumi, (double)Li/sumi ) );
			System.out.println( String.format( "Off by:        P(H)=%5f P(M)=%5f P(L)=%5f", 
					(double)Hi/sumi - g.getP(Return.high),       // if the played ratios are close to the actual probabilities,
					(double)Mi/sumi - g.getP(Return.medium),     // I assume that the playGamble method works reasonably well
					(double)Li/sumi - g.getP(Return.low) ) );
		}
	}

}
