package rky.portfolio.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rky.portfolio.gambles.Gamble;
import rky.portfolio.gambles.Luck;
import rky.portfolio.gambles.Return;
import rky.util.SetMap;

public class FileManager
{
    private static final String FILE_NAME = "gambles-%d.txt";
    
    public static String writeInputFile(List<Gamble> gambles, SetMap<Integer, Integer> links) 
    {
        try
        {
            File file = new File(String.format(FILE_NAME, System.currentTimeMillis()));
            file.createNewFile();
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            
            bw.write("# gamble_id, class_id, h_ret, h_prob, m_ret, m_prob, l_ret, l_prob\n");
            for( int i = 0; i < gambles.size(); i++ )
            {
                Gamble g = gambles.get(i);
                bw.write(String.format(
                        "%d, %d, %f, %f, %f, %f, %f \n",
                        i, (int)(Math.random() * 16),
                        g.getV(Return.high  ), g.getP(Return.high),
                        g.getV(Return.medium), g.getP(Return.medium),
                        g.getV(Return.low   ), g.getP(Return.low)
                ));
            }
            
            links = SetMap.distinct(links);
            
            bw.write("\n# gi, gj\n");
            for (Integer key : links.keySet())
            {
                for (Integer val : links.get(key))
                {
                    bw.write(String.format("%d, %d \n", key, val));
                }
            }
            
            bw.close();
            
            return file.getAbsolutePath();
        }
        catch (IOException e)
        {
            e.printStackTrace(System.err);
        }

        throw new RuntimeException("Unable to generate input file.");
    }
    
    public static GameData readAllFiles(String inputFile)
    {
        return new GameData();
    }
    
    public static class GameData
    {
        public final Map<Integer, Gamble>   gambles       = new HashMap<Integer, Gamble>();
        public final Map<Gamble, Integer>   ids           = new HashMap<Gamble, Integer>();
        public final SetMap<Gamble, Gamble> links         = new SetMap<Gamble, Gamble>();
        public final Map<Integer, Luck>     classes       = new HashMap<Integer, Luck>();
        public final Map<Gamble, Integer>   gambleClasses = new HashMap<Gamble, Integer>();
    }

}
