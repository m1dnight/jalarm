/******************************************************************************
 * JAlarm
 * @author  : Christophe De Troyer
 * Last edit: 28-aug-2014 19:40:39                                                   
 * Full source can be found on GitHub                                      
 ******************************************************************************/
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import mp3.Mp3Player;

public class Main
{
	public static void main(String[] args) throws InterruptedException
	{
		String song = "C:\\song.mp3";
		FileInputStream fis;
		try
		{
			// Read song from disk.
			fis = new FileInputStream(song);
			BufferedInputStream bis = new BufferedInputStream(fis);
			
			// Create new Mp3Player object.
			Mp3Player m = new Mp3Player(bis);
			m.play();
			Thread.sleep(4000);
			m.pause();
			Thread.sleep(4000);
			m.resume();
			Thread.sleep(4000);
			m.stop();
			Thread.sleep(4000);
		} catch (FileNotFoundException e)
		{
			System.out.println("Error reading song file from disk.");
		}
		System.out.println("Done");
	}
}