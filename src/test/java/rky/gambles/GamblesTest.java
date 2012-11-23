package rky.gambles;

import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import rky.portfolio.gambles.Gamble;
import rky.portfolio.gambles.Gambles;
import rky.portfolio.gambles.Luck;
import rky.portfolio.gambles.Return;
import rky.util.SetMap;

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
	public void testSimplePlayGambles()
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
			
			System.out.println( String.format( "Played counts (P(H), P(M), P(L)): (%8.5f, %8.5f, %8.5f)", (double)Hi/sumi, (double)Mi/sumi, (double)Li/sumi ) );
			System.out.println( String.format( "Off by        (P(H), P(M), P(L)): (%8.5f, %8.5f, %8.5f)",
					(double)Hi/sumi - g.getP(Return.high),       // if the played ratios are close to the actual probabilities,
					(double)Mi/sumi - g.getP(Return.medium),     // I assume that the playGamble method works reasonably well
					(double)Li/sumi - g.getP(Return.low) ) );
		}
	}
	
	@Test
	public void testAdvancedPlayGambles()
	{
		Map<Integer, Gamble>   gambles       = new HashMap<Integer, Gamble>();
		Map<Gamble, Integer>   ids           = new HashMap<Gamble, Integer>();
		SetMap<Gamble, Gamble> links         = new SetMap<Gamble, Gamble>();
		Map<Integer, Luck>     classes       = new HashMap<Integer, Luck>();
		Map<Gamble, Integer>   gambleClasses = new HashMap<Gamble, Integer>();
		
		gambles.put( 0, Gambles.randomGamble(2.0) );
		gambles.put( 1, Gambles.randomGamble(2.0) );
		gambles.put( 2, Gambles.randomGamble(2.0) );
		gambles.put( 3, Gambles.randomGamble(2.0) );
		gambles.put( 4, Gambles.randomGamble(2.0) );
		gambles.put( 5, Gambles.randomGamble(2.0) );
		gambles.put( 6, Gambles.randomGamble(2.0) );
		gambles.put( 7, Gambles.randomGamble(2.0) );
		gambles.put( 8, Gambles.randomGamble(2.0) );
		gambles.put( 9, Gambles.randomGamble(2.0) );
		
		for( Integer i : gambles.keySet() )
			ids.put( gambles.get(i), i );
		
		String[] linkStrings = "4,0 4,1 4,2 4,3 4,5".split(" ");
		for( String l : linkStrings ) {
			Integer id1 = Integer.parseInt(l.split(",")[0]);
			Integer id2 = Integer.parseInt(l.split(",")[1]);
			links.put(gambles.get(id1), gambles.get(id2));
			links.put(gambles.get(id2), gambles.get(id1));
		}
		
		classes.put( 0, Luck.unfavorable );
		classes.put( 1, Luck.neutral     );
		classes.put( 2, Luck.favorable   );
		
		gambleClasses.put( gambles.get(0), 0 );
		gambleClasses.put( gambles.get(1), 0 );
		gambleClasses.put( gambles.get(2), 1 );
		gambleClasses.put( gambles.get(3), 1 );
		gambleClasses.put( gambles.get(4), 2 );
		gambleClasses.put( gambles.get(5), 2 );
		gambleClasses.put( gambles.get(6), 0 );
		gambleClasses.put( gambles.get(7), 0 );
		gambleClasses.put( gambles.get(8), 0 );
		gambleClasses.put( gambles.get(9), 0 );
		
		Map<Gamble, Return> played = new HashMap<Gamble, Return>();
		
		//--------------------------------------------
		
		int id = 4;
		Gamble g = gambles.get( id );
		Luck luck = classes.get( gambleClasses.get( g ) );
		
		int timesPlayed = 100000;
		double sumReturn = 0;
		for( int i = 0; i < timesPlayed; i++ )
		{
			Return ret = Gambles.playGamble(g, luck, played, links.get(g));
			sumReturn += g.getV( ret );
		}
		
		Gamble gnot = new Gamble( g, luck );
		System.out.println( g );
		System.out.println( "class: " + luck );
		System.out.println( "becomes: " + gnot );
		System.out.println( "expected return: " + Gambles.expectedReturn(gnot) );
		System.out.println( " average return: " + sumReturn / timesPlayed );
		
		//-------------------------------------
		
		played.put( gambles.get(1), Return.high);     // these make gamble 4 favorable according to the links
		played.put( gambles.get(2), Return.high );
		played.put( gambles.get(3), Return.low );
		
		sumReturn = 0;
		for( int i = 0; i < timesPlayed; i++ )
		{
			Return ret = Gambles.playGamble(g, luck, played, links.get(g));
			sumReturn += g.getV( ret );
		}
		
		Gamble gnotnot = new Gamble( gnot, Luck.favorable );
		System.out.println( g );
		System.out.println( "links: " + links.get(g) );
		System.out.println( "becomes: " + gnotnot );
		System.out.println( "expected return: " + Gambles.expectedReturn(gnotnot) );
		System.out.println( " average return: " + sumReturn/ timesPlayed );
		
	}

}
