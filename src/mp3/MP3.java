package mp3;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import javazoom.jl.player.Player;

public class MP3
{
	private String filename;
	private Player player;

	/**
	 * MP3 constructor
	 * 
	 * @param filename
	 *            name of input file
	 */
	public MP3(String filename)
	{
		this.filename = filename;
	}

	/**
	 * Creates a new Player
	 */
	public void play()
	{
		try
		{
			FileInputStream fis = new FileInputStream(this.filename);
			BufferedInputStream bis = new BufferedInputStream(fis);

			this.player = new Player(bis);
		} catch (Exception e)
		{
			System.err.printf("%s\n", e.getMessage());
		}

		new Thread()
		{
			@Override
			public void run()
			{
				try
				{
					while(true)
					{
						player.play(1);
					}
				} catch (Exception e)
				{
					System.err.printf("%s\n", e.getMessage());
				}
			}
		}.start();
	}

	/**
	 * Closes the Player
	 */
	public void close()
	{
		if (this.player != null)
		{
			this.player.close();
		}
	}

	// ///////////////////////

	/**
	 * Plays '01 Maenam.mp3' in an infinite loop
	 */
	public static void playMaenam()
	{
		MP3 mp3 = new MP3("C:\\song.mp3");

		mp3.play();
	}
}