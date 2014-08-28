package mp3;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import utils.Printer;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class Mp3PlayerThread extends Thread
{
	public  volatile boolean playing = false;
	public  volatile boolean abort   = false;
	private Player   player;
	private String   path;

	public Mp3PlayerThread(String file)
	{
		this.path = file;
	}

	public void run()
	{
		synchronized (this)
		{
			try
			{
				FileInputStream fis;
				fis = new FileInputStream(this.path);
				BufferedInputStream bis = new BufferedInputStream(fis);

				this.player = new Player(bis);
				
				while(!abort)
				{
					if(playing)
						player.play(1);
				}
				Printer.debugMessage(this.getClass(), "end of file or stopped");
				
			} catch (FileNotFoundException | JavaLayerException e)
			{
				e.printStackTrace();
			}
		}
	}
}
