/**
 * 
 */
package rky.portfolio.gambles;

/**
 * @author stoked
 *
 */
public final class Luck
{
	public static final Luck   favorable = new Luck();
	public static final Luck     neutral = new Luck();
	public static final Luck unfavorable = new Luck();
	
	private Luck()
	{

	}
	
	@Override
	public String toString()
	{
		if( this == favorable   )    return "Luck[FAVORABLE]";
		if( this == neutral     )    return "Luck[NEUTRAL]";
		if( this == unfavorable )    return "Luck[UNFAVORABLE]";
		throw new RuntimeException( "unexpected instance of Luck" );
	}
}
