/******************************************************************************
 * JAlarm
 * @author  : Christophe De Troyer
 * Last edit: 11-sep-2014 19:39:13                                                   
 * Full source can be found on GitHub      :
 * https://github.com/m1dnight/JAlarm                                
 ******************************************************************************/
package utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Parser
{
	/**
	 * Parsers an hour stamp in the form of HH:MM.
	 * @param hourStamp
	 * @return 
	 */
	//TODO make more robust.
	public static DateTime parseHourStamp(String hourStamp)
	{
		// Pick out hour and minutes.
		String[] parts = hourStamp.split(":");
		int alarmHour    = Integer.parseInt(parts[0]);
		int alarmMinutes = Integer.parseInt(parts[1]);
		
		return parseHourStamp(alarmHour, alarmMinutes);
	}
	/**
	 * Creates a DateTime object that represents the next time 
	 * the clock will be at given hour and minutes.
	 * @param hours
	 * @param minutes
	 * @return
	 */
	public static DateTime parseHourStamp(int alarmHour, int alarmMinutes)
	{
		// Calculate seconds to alarm.
		DateTime now            = new DateTime();
		int      currentHour    = now.getHourOfDay();
		int      currentMinutes = now.getMinuteOfHour();
		DateTime alarmDateTime;
		
		// Check if alarm is for tomorrow or today.
		if(alarmHour < currentHour || (alarmHour == currentHour && alarmMinutes < currentMinutes))
			alarmDateTime = new DateTime().plusDays(1);
		else
			alarmDateTime = new DateTime();
		
		//TODO can I do this cleaner? 
		// Create alarm DateTime object.
		DateTimeFormatter dtfOut = DateTimeFormat.forPattern("MM/dd/yyyy");
		String dateString = dtfOut.print(alarmDateTime);
		
		DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyy HH:mm");
		alarmDateTime = formatter.parseDateTime(String.format("%s %02d:%02d", dateString, alarmHour, alarmMinutes));
		
		return alarmDateTime;
	}
}
