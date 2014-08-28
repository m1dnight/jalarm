/******************************************************************************
 * JAlarm
 * @author  : Christophe De Troyer
 * Last edit: 28-aug-2014 19:40:38                                                   
 * Full source can be found on GitHub                                      
 ******************************************************************************/

package mp3;

import java.io.BufferedInputStream;

import utils.Printer;

public class Mp3Player
{
	private Mp3PlayerThread playerThread;
	private BufferedInputStream songStream;

	public Mp3Player(BufferedInputStream songStream)
	{
		this.songStream = songStream;
	}

	/**
	 * Initializes the playerThread with the given song, sets the flag to true
	 * and starts playing the song.
	 */
	public synchronized void play()
	{
		playerThread = new Mp3PlayerThread(songStream);
		new Thread(playerThread).start();
	}

	/**
	 * Pauses the execution of the playerThread.
	 */
	public synchronized void pause()
	{
		Printer.debugMessage(this.getClass(), "paused");
		playerThread.pauseThread();
	}
	/**
	 * Resumes the playerThread by calling the resumtThread() method.
	 */
	public synchronized void resume()
	{
		synchronized (playerThread)
		{
			Printer.debugMessage(this.getClass(), "resuming");
			// playerThread.notify();
			playerThread.resumeThread();
		}
	}
	/**
	 * Stops the playerThread by making it exit the run() method.
	 */
	public synchronized void stop()
	{
		Printer.debugMessage(this.getClass(), "stopping");
		playerThread.abortThread();
		playerThread = null; // Needed or not?
	}
}
