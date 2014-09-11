/******************************************************************************
 * JAlarm
 * @author  : Christophe De Troyer
 * Last edit: 11-sep-2014 19:39:13                                                   
 * Full source can be found on GitHub      :
 * https://github.com/m1dnight/JAlarm                                
 ******************************************************************************/
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import mp3.Mp3PlayerThread;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.joda.time.DateTime;
import org.joda.time.Seconds;

import utils.Parser;
import utils.Printer;

public class Main
{
	// Parameter values
	private static DateTime alarmTime = null;
	private static File selectedSong = null;
	private static int snoozeTime = 0;
	private static ScheduledExecutorService scheduler;
	private static Mp3PlayerThread playerThread = null;

	public static void main(String[] args) throws InterruptedException
	{
		// Build the options
		Options ops = buildOptions();

		// Parse the commandline arguments
		CommandLineParser parser = new GnuParser();
		try
		{
			CommandLine line = parser.parse(ops, args);
			if (line.hasOption("h"))
			{
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("JAlarm", ops);
				System.exit(0);
			} else
			{
				if (line.hasOption("s"))
				{
					snoozeTime = Integer.parseInt(line.getOptionValue("s"));
				}
				if (line.hasOption("f"))
				{
					selectedSong = new File(line.getOptionValue("f"));
				} else
				{
					// If no music file is specified we can not set the alarm.
					System.err.println("No sound file specified. Aborting..\n");
					System.exit(0);
				}
				if (line.hasOption("t"))
				{
					alarmTime = Parser.parseHourStamp(line.getOptionValue("t"));
				} else
				{
					System.err.println("No time specified.\n");
					System.exit(0);
				}
			}
		} catch (ParseException exp)
		{
			// oops, something went wrong
			Printer.error(Main.class.getClass(), "Parsing failed. Reason: \n"
					+ exp.getMessage());
		}
		System.out.println(String.format("Alarm set for %s with %s\n",
				alarmTime, selectedSong.getName()));

		// Schedule the alarm
		scheduleAlarm(alarmTime);

		// open up standard input
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try
		{
			String input = br.readLine();
			while (input != null && !input.equals("imawakenow"))
			{
				
				System.out.println("Snoozing");
				cancelAlarm();
				scheduleAlarm(new DateTime().plusSeconds(snoozeTime));
				input = br.readLine();
			}
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Exiting..");
		cancelAlarm();
	}

	private static void cancelAlarm()
	{
		playerThread.abortThread();
		scheduler.shutdown();
	}

	private static void scheduleAlarm(DateTime alarmTime)
	{
		// Init playerthread
		playerThread = new Mp3PlayerThread(selectedSong.getAbsolutePath(), true);

		// Schedule runnable.
		Seconds seconds = Seconds.secondsBetween(new DateTime(), alarmTime);
		Printer.debugMessage(
				Main.class.getClass(),
				String.format("alarm set in %d seconds",
						seconds.getSeconds()));
		// Schedule the task to execute at set date.
		scheduler = Executors.newScheduledThreadPool(2);
		scheduler.schedule(playerThread, seconds.getSeconds(),
				TimeUnit.SECONDS);

	}

	public static Options buildOptions()
	{
		Options ops = new Options();

		// Alarm time option
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

		// Alarm sound
		OptionBuilder.hasArg(true);
		OptionBuilder.withArgName("seconds");
		OptionBuilder.withDescription("Seconds to snooze");
		Option snooze = OptionBuilder.create("s");
		ops.addOption(snooze);

		// Help option
		Option help = new Option("h", "Print help options");
		ops.addOption(help);
		return ops;
	}
}