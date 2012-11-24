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

    /**
     * Takes an array of String values and forms a message body
     * is in the format "[v1, v2, v3, v4 ... ]"
     */
    public static Message createVector(String[] s)
    {
        return new Message(formatArray(s));
    }

    public static Message createError(String errorMessage)
    {
        return new Message("ERROR \"" + errorMessage + "\"");
    }
    
    public static Message createGameOver(Double score)
    {
        return new Message("GAMEOVER " + score);
    }

    private static String formatArray(String[] a)
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
