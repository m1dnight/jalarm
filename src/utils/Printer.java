/******************************************************************************
 * JAlarm
 * @author  : Christophe De Troyer
 * Last edit: 11-sep-2014 19:39:13                                                   
 * Full source can be found on GitHub      :
 * https://github.com/m1dnight/JAlarm                                
 ******************************************************************************/
package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Printer
{

	private final static boolean messagesEnabled = true;
	private final static boolean errorsEnabled   = true;
	private final static boolean debugEnabled    = true;
	private final static SimpleDateFormat sdf = new SimpleDateFormat("HH:MM a");

	/**
	 * Prints a regular message with the classname prepended to it.
	 * @param c
	 * @param msg
	 */
	public static void debugMessage(@SuppressWarnings("rawtypes") Class c, String msg)
	{
		
		if(debugEnabled)
		{
			System.out.println(String.format("%s :: %15s : %s", sdf.format(new Date()), c.getSimpleName(), msg));
		}
	}
	/**
	 * Prints a regular message with custom classname prepended to it.
	 * @param c
	 * @param msg
	 */
	public static void debugMessage(String sender, String msg)
	{
		if(debugEnabled)
		{
			System.out.println(String.format("%s :: %15s : %s", sdf.format(new Date()), sender, msg));
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
					.println(String.format("%s :: %15s : %s", sdf.format(new Date()), c.getSimpleName(), msg));
		}
	}
	
	public static void message(@SuppressWarnings("rawtypes") Class c, String msg)
	{
		if (messagesEnabled)
		{
			System.out
					.println(String.format("%s :: %15s : %s", sdf.format(new Date()), c.getSimpleName(), msg));
		}
	}
	
}
