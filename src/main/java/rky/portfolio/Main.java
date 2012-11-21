package rky.portfolio;


public class Main
{

    public static void main(String[] args)
    {
        if (args.length < 2 || args.length > 3) 
        {
            usage();
            System.exit(-1);
        }
        
        int port = Integer.parseInt(args[0]);
        int numPlayers = Integer.parseInt(args[1]);
        boolean gui = args.length == 3 ? true : false;

        System.out.println("port=" + port + ",num-plaers=" + numPlayers + ",gui=" + gui);
    }
    
    private static void usage()
    {
        System.err.println("Invalid/missing arguments.");
        System.err.println("\tusage: java -jar portfolio-1.0.0.jar <port> <num-players> gui");
        System.err.println("\t(The 3rd param is optional. If a value is present, the gui shown.)");
    }

}
