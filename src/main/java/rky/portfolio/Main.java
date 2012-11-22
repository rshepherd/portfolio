package rky.portfolio;

import rky.portfolio.io.IoManager;


public class Main
{

    public static void main(String[] args)
    {
        if (args.length < 3 || args.length > 4) 
        {
            usage();
            System.exit(-1);
        }
        
        int port = Integer.parseInt(args[0]);
        int numPlayers = Integer.parseInt(args[1]);
        int gameMode = Integer.parseInt(args[2]);
        boolean gui = args.length == 4 ? true : false;
        
        IoManager io = new IoManager(port);
        //io.connect(numPlayers);

    }
    
    private static void usage()
    {
        System.err.println("Invalid/missing arguments.");
        System.err.println("\tusage: java -jar portfolio-1.0.0.jar <port> <num-players> <game-mode> <display-gui?>");
        System.err.println("\tex: java -jar portfolio-1.0.0.jar 54321 5 2 false");
    }

}
