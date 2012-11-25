package rky.portfolio.io;

import org.junit.Test;

import rky.portfolio.io.GameData.ClassFavorabilityMap;

public class FileManagerTest
{
    
    @Test
    public void classFavorabilityTest()
    {
        String classFavoribilityFile = "src/main/resources/favorability.txt";
        ClassFavorabilityMap map = new ClassFavorabilityMap();
        FileManager.readClassFavoribilityFile(classFavoribilityFile, map);
        System.out.println(map.toString());
    }

}
