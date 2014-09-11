/******************************************************************************
 * JAlarm
 * @author  : Christophe De Troyer
 * Last edit: 11-sep-2014 19:39:13                                                   
 * Full source can be found on GitHub      :
 * https://github.com/m1dnight/JAlarm                                
 ******************************************************************************/

package mp3;

import utils.Printer;

public class Mp3Player
{
	private Mp3PlayerThread playerThread;
	private String songPath;

	public Mp3Player(String songPath)
	{
		this.songPath = songPath;
	}

	/**
	 * Initializes the playerThread with the given song, sets the flag to true
	 * and starts playing the song.
	 */
	public synchronized void play()
	{
		playerThread = new Mp3PlayerThread(songPath, false);
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
