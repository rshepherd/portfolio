package rky.portfolio.io;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import rky.portfolio.gambles.Luck;
import rky.portfolio.io.GameData.ClassFavorabilityMap;

public class ClassFavoribilityMapTest
{

    @Test
    public void test()
    {
        ClassFavorabilityMap cfm = new ClassFavorabilityMap();
        
        cfm.put(0, 2,  Luck.favorable);
        cfm.put(0, 3,  Luck.favorable);
        cfm.put(0, 12, Luck.favorable);
        
        cfm.put(3, 2,  Luck.unfavorable);
        cfm.put(3, 3,  Luck.unfavorable);
        cfm.put(3, 12, Luck.unfavorable);
        
        assertEquals(Luck.favorable, cfm.get(0, 2));
        assertEquals(Luck.favorable, cfm.get(0, 3));
        assertEquals(Luck.favorable, cfm.get(0, 12));
        
        assertEquals(Luck.favorable, cfm.get(1, 2));
        assertEquals(Luck.favorable, cfm.get(1, 3));
        assertEquals(Luck.favorable, cfm.get(1, 12));
        
        assertEquals(Luck.unfavorable, cfm.get(3, 2));
        assertEquals(Luck.unfavorable, cfm.get(3, 3));
        assertEquals(Luck.unfavorable, cfm.get(3, 12));
        
        assertEquals(Luck.unfavorable, cfm.get(4, 2));
        assertEquals(Luck.unfavorable, cfm.get(4, 3));
        assertEquals(Luck.unfavorable, cfm.get(4, 12));
        
        assertEquals(Luck.neutral, cfm.get(5, 10));
        assertEquals(Luck.neutral, cfm.get(1, 15));
        
        System.out.println(cfm.toString());
    }

}
