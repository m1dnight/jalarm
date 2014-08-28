/******************************************************************************
 * JAlarm
 * @author  : Christophe De Troyer
 * Last edit: 28-aug-2014 19:40:38                                                   
 * Full source can be found on GitHub                                      
 ******************************************************************************/
package mp3;

import java.io.BufferedInputStream;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import utils.Printer;

public class Mp3PlayerThread implements Runnable
{
	private volatile boolean playing = false;
	private volatile boolean abort = false;
	
	private Player player;
	private final BufferedInputStream songStream;

	public Mp3PlayerThread(BufferedInputStream songStream)
	{
		this.songStream = songStream;
	}

	@Override
	public void run()
	{
		synchronized (this)
		{
			try
			{
				// Initialize the player and set the play flag to true.
				this.player = new Player(songStream);
				this.playing = true;
				
				while (!player.isComplete() && !abort)
				{
					while (!playing)
						this.wait();
					
					player.play(1);
				}
				Printer.debugMessage(this.getClass(), "end of file or stopped");

			} catch (JavaLayerException e)
			{
				e.printStackTrace();
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
	/**
	 * Pauses the playback of the thread.
	 */
	public void pauseThread()
	{
		Printer.debugMessage(this.getClass(), "paused");
		this.playing = false;
	}
	/**
	 * Resumes playback of thread (if possible).
	 */
	public void resumeThread()
	{
		Printer.debugMessage(this.getClass(), "resuming");

		synchronized (this)
		{
			this.playing = true;
			notify();
		}
	}
	/**
	 * Sets the abort flag to true. Doing so makes the run() method finish
	 * and thus stopping the thread properly.
	 */
	public void abortThread()
	{
		Printer.debugMessage(this.getClass(), "stopping");
		this.abort = true;
	}
}
