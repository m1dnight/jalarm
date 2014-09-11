/******************************************************************************
 * JAlarm
 * @author  : Christophe De Troyer
 * Last edit: 11-sep-2014 19:39:13                                                   
 * Full source can be found on GitHub      :
 * https://github.com/m1dnight/JAlarm                                
 ******************************************************************************/
package mp3;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;

import javax.swing.Timer;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import utils.Printer;

public class Mp3PlayerThread implements Runnable
{
	private volatile boolean playing = false;
	private volatile boolean abort = false;
	
	private Player player;
	private final BufferedInputStream songStream;
	
	private Timer snoozeTimer;

	public Mp3PlayerThread(BufferedInputStream songStream)
	{
		this.songStream = songStream;
	}

	@Override
	public void run()
	{
		Printer.debugMessage(this.getClass(), "started run method");
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
	 * Makes the run() method finish
	 * and thus stopping the thread properly.
	 */
	public void abortThread()
	{
		// If we have a snooze running we have to cancel that as well.
		if(snoozeTimer != null) 
		{
			snoozeTimer.stop();
		}
		
		Printer.debugMessage(this.getClass(), "stopping");
		this.abort = true;	
	}
	
	/**
	 * Returns the status of the player.
	 * @return
	 */
	public boolean isPlaying()
	{
		return !player.isComplete();
	}
	
	/**
	 * Pauses the thread for snoozeInterval miliseconds
	 * using snoozeTimer.
	 * @param snoozeInterval
	 */
	public void snoozeThread(int snoozeInterval)
	{
		Printer.debugMessage(this.getClass(), "Snoozing playback");
		this.pauseThread();
		
		// Cancel any running snoozes.
		if(snoozeTimer != null && snoozeTimer.isRunning()) 
			snoozeTimer.stop();
		
		snoozeTimer = new Timer(snoozeInterval, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Printer.debugMessage(this.getClass(), "Resuming playback");
				Mp3PlayerThread.this.resumeThread();
			}
		});
		snoozeTimer.start();
	}

}
