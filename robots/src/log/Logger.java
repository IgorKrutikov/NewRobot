package log;

public final class Logger
{
    private static final LogWindowSource defaultLogSource;
    private static int messagesCount = 0;
    static {
        defaultLogSource = new LogWindowSource(5); // для простоты теста сократим число
    }
    
    private Logger()
    {
    }

    public static void debug(String strMessage)
    {
        defaultLogSource.append(LogLevel.Debug, ++messagesCount +") "+ strMessage);
    }
    
    public static void error(String strMessage)
    {
        defaultLogSource.append(LogLevel.Error, strMessage);
    }

    public static LogWindowSource getDefaultLogSource()
    {
        return defaultLogSource;
    }
}
