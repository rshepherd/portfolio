/**
 * 
 */
package rky.gambles;

/**
 * @author stoked
 *
 */
public final class Return
{
	public static final Return   high = new Return();
	public static final Return medium = new Return();
	public static final Return    low = new Return();
	

	
	private Return()
	{
		
	}

	@Override
	public String toString()
	{
		if( this == high   )     return "Return[HI]";
		if( this == medium )     return "Return[MED]"; 
		if( this == low    )     return "Return[LOW]";
		throw new RuntimeException( "unexpected instance of Return" );
	}
}
