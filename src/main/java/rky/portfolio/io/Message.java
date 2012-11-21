package rky.portfolio.io;

public class Message
{
    public static final Message ACK = new Message("OK");

    private String body;

    public Message(String body)
    {
        this.body = body;
    }
    
    public Message(String... body)
    {
        for (String b : body)
        {
            this.body += " " + b;
        }
    }

    public static Message createVector(Double[] vector)
    {
        return new Message(arrayToString(vector));
    }

    public static Message createError(String errorMessage)
    {
        return new Message("ERROR \"" + errorMessage + "\"");
    }
    
    public static Message createGameOver(Double score)
    {
        return new Message("GAMEOVER " + score);
    }

    private static String arrayToString(Double[] a)
    {
        StringBuffer result = new StringBuffer();
        result.append("[");
        if (a.length > 0)
        {
            result.append(a[0]);
            for (int i = 1; i < a.length; i++)
            {
                result.append(", ");
                result.append(a[i]);
            }
        }
        result.append("]");
        
        return result.toString();
    }
    
    @Override
    public String toString()
    {
        return body;
    }

}
