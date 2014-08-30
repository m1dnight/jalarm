/******************************************************************************
 * JAlarm
 * @author  : Christophe De Troyer
 * Last edit: 28-aug-2014 19:40:39                                                   
 * Full source can be found on GitHub                                      
 ******************************************************************************/
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import utils.Printer;
import mp3.Mp3Player;

public class Main
{
	public static void main(String[] args) throws InterruptedException
	{
		String songPath = null;
		String alarmTime = null;
		// Build the options
		Options ops = buildOptions();
		// Parse the commandline arguments
		CommandLineParser parser = new GnuParser();
		try
		{
		       CommandLine line = parser.parse(ops, args);
		       if (line.hasOption("help"))
		       {
		            HelpFormatter formatter = new HelpFormatter();
		            formatter.printHelp("JAlarm", ops);
		       } else
		       {
		            if (line.hasOption("f"))
		            {
		            	Printer.debugMessage(Main.class.getClass(), "Found file option");
		            	songPath = line.getOptionValue("f");
		            }
		            else
		            {
		            	// If no music file is specified we can not set the alarm.
		            	System.err.println("No sound file specified. Aborting..\n");
		            	
		            }
		            if (line.hasOption("t"))
		            {
		            	Printer.debugMessage(Main.class.getClass(), "Found time option");
		            	alarmTime = line.getOptionValue("t");
		            }
		            
		            // the following is DEBUG because I don't know
		            // how to tell IDEA that it should start this with
		            // arguments. :)
		            HelpFormatter formatter = new HelpFormatter();
		            //formatter.printHelp("JAlarm", ops);
		            //System.out.println(serverAddr + " " + serverPort + " " + verbose);
		        }
		} catch (ParseException exp)
		{
		    // oops, something went wrong
		    Printer.error(Main.class.getClass(), "Parsing failed. Reason: " + exp.getMessage());
		}
		Printer.debugMessage(Main.class.getClass(), String.format("Alarm set for %s with %s\n", alarmTime, songPath));
	}
	
	public static Options buildOptions()
	{
		Options ops = new Options();
		Option help = new Option("h", "Show help");
		
		//Alarm time option
		OptionBuilder.hasArg(true);
		OptionBuilder.withArgName("timestamp");
		OptionBuilder.withDescription("Time the alarm should be set");
		Option time = OptionBuilder.create("t");
		ops.addOption(time);
		
		// Repeat option
		OptionBuilder.hasArg(false);
		OptionBuilder.withDescription("Repeat alarm");
		Option repeat = OptionBuilder.create("r");
		ops.addOption(repeat);
		
		// Alarm sound
		OptionBuilder.hasArg(true);
		OptionBuilder.withArgName("file");
		OptionBuilder.withDescription("File to play");
		Option song = OptionBuilder.create("f");
		ops.addOption(song);
		return ops;
	}
}