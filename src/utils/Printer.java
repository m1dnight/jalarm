package utils;

public class Printer
{

	private final static boolean messagesEnabled = true;
	private final static boolean errorsEnabled   = true;
	private final static boolean debugEnabled    = true;

	/**
	 * Prints a regular message with the classname prepended to it.
	 * @param c
	 * @param msg
	 */
	public static void debugMessage(@SuppressWarnings("rawtypes") Class c, String msg)
	{
		if(debugEnabled)
		{
			System.out.println(c.getSimpleName() + ": " + msg);
		}
	}
	
	/**
	 * Prints an error message with the class name prepended to it.
	 * @param c
	 * @param msg
	 */
	public static void error(@SuppressWarnings("rawtypes") Class c, String msg)
	{
		if (errorsEnabled)
		{
			System.err
					.println(c.getSimpleName() + ": " + msg);
		}
	}
	
	public static void message(@SuppressWarnings("rawtypes") Class c, String msg)
	{
		if (messagesEnabled)
		{
			System.out
					.println(c.getSimpleName() + ": " + msg);
		}
	}
	
}
